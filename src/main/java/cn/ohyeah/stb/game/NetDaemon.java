package cn.ohyeah.stb.game;

import java.io.IOException;

import cn.ohyeah.stb.asyn.AsynRequest;
import cn.ohyeah.stb.asyn.AsynResponse;
import cn.ohyeah.stb.asyn.IAsynService;
import cn.ohyeah.stb.buf.ByteBuffer;
import cn.ohyeah.stb.net.AbstractNetClient;
import cn.ohyeah.stb.net.HttpClient;
import cn.ohyeah.stb.net.SocketClient;
import cn.ohyeah.stb.util.BlockingArrayQueue;

/**
 * 网络守护线程
 * @author maqian
 * @version 1.0
 */
public class NetDaemon implements Runnable, IAsynService {
	
	private static final byte STATE_IDLE = 0;
	private static final byte STATE_PREPARING = 1;
	private static final byte STATE_SENDING = 2;
	private static final byte STATE_RECEIVING = 3;
	private static final byte STATE_EXCEPTION = 4;
	
	private static NetDaemon instance;
	private ByteBuffer heartBeat;
	private int interval;
	private long lastSendMillis;
	private BlockingArrayQueue requestQueue;
	private AbstractNetClient client;
	private ByteBuffer recvBuffer;
	private String server;
	private int protocolType;
	private int connType;
	private Frame frame;
	private ByteBuffer requestData;
	private AsynRequest asynRequest;
	private AsynResponse asynResponse;
	private Exception exception;
	private boolean exit;
	private boolean isActive;
	volatile private int state;
	volatile private boolean exitFlag;
	
	private NetDaemon() {
		this(null, -1);
	}
	
	private NetDaemon(ByteBuffer heartBeat, int interval) {
		this.heartBeat = heartBeat;
		this.interval = interval;
		this.requestQueue = new BlockingArrayQueue();
		this.frame = new Frame();
		this.recvBuffer = new ByteBuffer(256);
	}
	
	synchronized public static NetDaemon create(ByteBuffer heartBeat, int interval) {
		if (instance==null) {
			instance = new NetDaemon(heartBeat, interval);
			new Thread(instance).start();
		}
		return instance;
	}
	
	synchronized public static void stop() {
		if (instance != null) {
			instance.exitFlag = true;
		}
	}
	
	synchronized public static void waitStop() throws InterruptedException {
		if (instance != null) {
			instance.exitFlag = true;
			while (!instance.exit) {
				instance.wait();
			}
		}
	}
		
	private boolean isStateIdle() {
		return state == STATE_IDLE;
	}
	
	private boolean isStatePreparing() {
		return state == STATE_PREPARING;
	}
	
	private boolean isStateSending() {
		return state == STATE_SENDING;
	}
	
	private boolean isStateReceiving() {
		return state == STATE_RECEIVING;
	}
	
	private boolean isStateException() {
		return state == STATE_EXCEPTION;
	}
	
	public AsynResponse asynRequest(AsynRequest req) throws InterruptedException {
		if (exitFlag) {
			throw new RuntimeException("thread will exit");
		}
		if (isStateException()) {
			throw new RuntimeException("an error occurred in thread: "+exception.getMessage());
		}
		AsynResponse rsp = new AsynResponse(req);
		requestQueue.put(rsp);
		//唤醒等待的线程
		synchronized (this) {
			isActive = true;
			notify();
		}
		return rsp;
	}
	
	private void checkProtocol(String server) {
		if (server.startsWith("http:")) {
			protocolType = AbstractNetClient.PROTOCOL_TYPE_HTTP;
		}
		else if (server.startsWith("socket:")) {
			protocolType = AbstractNetClient.PROTOCOL_TYPE_TCP;
		}
		else {
			throw new RuntimeException("不支持的协议类型,(server="+server+")");
		}
	}
	
	private void checkConnection(String server, int connType) throws IOException {
		if (connType != AbstractNetClient.CONN_TYPE_LONG
				&& connType != AbstractNetClient.CONN_TYPE_SHORT) {
			throw new RuntimeException("不支持的连接类型,(connType="+connType+")");
		}
		if (this.connType == AbstractNetClient.CONN_TYPE_LONG){
			if (this.connType != connType) {
				throw new RuntimeException("长连接的执行环境中不允许出现短连接的请求");
			}
			if (!this.server.equals(server)) {
				throw new RuntimeException("长连接执行环境中的目的地址与请求的目的地址不同");
			}
		}
		else {
			this.server = server;
			this.connType = connType;
			connectServer();
		}
	}
	
	private void createNetClient(int protocolType) {
		if (protocolType == AbstractNetClient.PROTOCOL_TYPE_HTTP) {
			this.client = new HttpClient(server, 3, 30000);
		}
		else if (protocolType == AbstractNetClient.PROTOCOL_TYPE_TCP){
			this.client = new SocketClient(server, 3, 30000);
		}
		else {
			throw new RuntimeException("不支持的协议类型,(server="+server+")");
		}
	}
	
	private void connectServer() throws IOException {
		createNetClient(protocolType);
		client.connect();
	}
	
	private void prepare() throws IOException {
		asynResponse = (AsynResponse)requestQueue.poll();
		if (asynResponse != null) {
			asynRequest = asynResponse.getRequest();
			asynResponse.setRequest(null);
			requestData = (ByteBuffer)asynRequest.getData();
			checkProtocol(asynRequest.getServer());
			checkConnection(asynRequest.getServer(), asynRequest.getConnType());
			state = STATE_SENDING;
		}
		else {
			if (!exitFlag) {
				if (connType != AbstractNetClient.CONN_TYPE_LONG) {
					synchronized (this) {
						isActive = false;
						state = STATE_IDLE;
					}
				}
				else {
					if (System.currentTimeMillis()-lastSendMillis >= interval) {
						requestData = heartBeat;
						state = STATE_SENDING;
					}
				}
			}
			else {
				closeClient();
				synchronized (this) {
					exit = true;
					notify();
				}
			}
		}
	}
	
	private void sendRequest() throws IOException {
		client.send(frame.encode(requestData));
		lastSendMillis = System.currentTimeMillis();
		state = STATE_RECEIVING;
	}
	
	private boolean isHeartBeat(ByteBuffer rsp) {
		return heartBeat.dataEquals(rsp);
	}
	
	private void closeClient() {
		this.connType = AbstractNetClient.CONN_TYPE_INVALID;
		this.protocolType = AbstractNetClient.PROTOCOL_TYPE_INVALID;
		this.server = null;
		this.client.close();
		this.client = null;
	}
	
	private void logicAfterRecvComplete(ByteBuffer rsp) {
		requestData = null;
		if (isHeartBeat(rsp)) {
			return;
		}
		
		asynResponse.setData(rsp);
		asynResponse.setPercent(100);
		asynResponse.setComplete(true);
		asynResponse.signal();
		asynResponse = null;
		asynRequest = null;
		if (connType != AbstractNetClient.CONN_TYPE_LONG) {
			closeClient();
		}
	}
	
	private void recvResponse() throws IOException {
		while (client.receive(recvBuffer) > 0) {
			ByteBuffer rsp = frame.decode(recvBuffer);
			if (rsp != null) {
				logicAfterRecvComplete(rsp);
				state = STATE_PREPARING;
				break;
			}
			else {
				asynResponse.setPercent(frame.decodePercent());
			}
		}
	}
	
	private void handleException(Exception ex) {
		state = STATE_EXCEPTION;
		exception = ex;
		client.close();
		client = null;
		server = null;
		recvBuffer.clear();
		protocolType = AbstractNetClient.CONN_TYPE_INVALID;
		connType = AbstractNetClient.PROTOCOL_TYPE_INVALID;
		requestData = null;
		asynRequest = null;
		if (asynResponse!=null) {
			asynResponse.setException(ex);
			asynResponse.signal();
		}
		AsynResponse asynRsp = (AsynResponse)requestQueue.poll();
		while (asynRsp != null) {
			asynRsp.setException(ex);
			asynRsp.signal();
		}
		state = STATE_IDLE;
		isActive = false;
		exception = null;
	}
	
	public void run() {
		while (!exit) {
			try {
				if (isStateReceiving()) {
					recvResponse();
				}
				else if (isStatePreparing()) {
					prepare();
				}
				else if (isStateSending()) {
					sendRequest();
				}
				else if (isStateIdle()) {
					synchronized (this) {
						while (!isActive) {
							wait();
						}
						state = STATE_PREPARING;
					}
				}
				else {
					throw new RuntimeException("no such state, (state="+state+")");
				}
				
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
				exitFlag = true;
			} catch (Exception e) {
				e.printStackTrace();
				handleException(e);
			}
		}
	}

}
