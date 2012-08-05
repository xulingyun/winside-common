package cn.ohyeah.stb.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

import cn.ohyeah.stb.buf.ByteBuffer;

/**
 * 抽象网络客户端
 * @author maqian
 * @version 1.0
 */
abstract public class AbstractNetClient {
	public static final byte CONN_TYPE_INVALID = -1;
	public static final byte CONN_TYPE_SHORT = 0;
	public static final byte CONN_TYPE_LONG = 1;
	
	public static final byte PROTOCOL_TYPE_INVALID = -1;
	public static final byte PROTOCOL_TYPE_HTTP = 0;
	public static final byte PROTOCOL_TYPE_TCP = 1;
	
	private static final int DEFAULT_RETRY_COUNT = 3;
	private static final int DEFAULT_TIME_OUT = 30000;
	private String server;
	private int retryCount;
	private int timeout;
	private long lastRecvMillis;
	protected StreamConnection conn;
	protected OutputStream connOs;
	protected InputStream connIs;
	
	protected AbstractNetClient(String server) {
		this(server, DEFAULT_RETRY_COUNT);
	}
	
	protected AbstractNetClient(String server, int retryCount) {
		this(server, retryCount, DEFAULT_TIME_OUT);
	}
	
	protected AbstractNetClient(String server, int retryCount, int timeout) {
		this.server = server;
		this.retryCount = retryCount;
		this.timeout = timeout;
	}
	
	protected void closeConnIs() {
		try {
			if (connIs != null) {
				connIs.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			connIs = null;
		}
	}
	
	protected void closeConnOs() {
		try {
			if (connOs != null) {
				connOs.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			connOs = null;
		}
	}
	
	protected void closeConn() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			conn = null;
		}
	}
	
	public void close() {
		closeConnIs();
		closeConnOs();
		closeConn();
	}
	
	protected void openConn() throws IOException {
		int tryCount = 0;
		while (tryCount <= retryCount) {
			try {
				conn = (StreamConnection)Connector.open(server, Connector.READ_WRITE, true);
				break;
			}
			catch (IOException e) {
				++tryCount;
				if (tryCount <= retryCount) {
					System.out.println("[信息] ==> "+"连接服务器失败，第"+tryCount+"次重试");
				}
				else {
					throw e;
				}
			}
		}
	}
	
	protected void openConnIs() throws IOException {
		if (connIs != null) {
			return;
		}
		int tryCount = 0;
		while (tryCount <= retryCount) {
			try {
				connIs = conn.openInputStream();
				break;
			}
			catch (IOException e) {
				++tryCount;
				if (tryCount <= retryCount) {
					System.out.println("[信息] ==> "+"连接服务器失败，第"+tryCount+"次重试");
				}
				else {
					throw e;
				}
			}
		}
	}
	
	protected void openConnOs() throws IOException {
		if (connOs != null) {
			return;
		}
		int tryCount = 0;
		while (tryCount <= retryCount) {
			try {
				connOs = conn.openOutputStream();
				break;
			}
			catch (IOException e) {
				++tryCount;
				if (tryCount <= retryCount) {
					System.out.println("[信息] ==> "+"连接服务器失败，第"+tryCount+"次重试");
				}
				else {
					throw e;
				}
			}
		}
	}
	
	public void connect() throws IOException {
		openConn();
	}
	
	abstract protected void setConnOpts(int dataLen) throws IOException;
	
	public int receive(ByteBuffer buf) throws IOException {
		logicBeforeReceive();
		openConnIs();
		int len = buf.slurp(connIs);
		if (len > 0) {
			lastRecvMillis = System.currentTimeMillis();
		}
		else {
			if (len < 0 || System.currentTimeMillis()-lastRecvMillis > timeout){
				throw new IOException("连接中断");
			}
		}
		return len;
	}
	
	public void send(ByteBuffer buf) throws IOException {
		setConnOpts(buf.length());
		openConnOs();
		buf.spit(connOs);
		lastRecvMillis = System.currentTimeMillis();
	}
	
	protected void logicBeforeReceive() throws IOException {}
}
