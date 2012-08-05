package cn.ohyeah.itvgame.service;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import cn.ohyeah.itvgame.protocol.Constant;
import cn.ohyeah.itvgame.protocol.HeadWrapper;

/**
 * 抽象Http服务类
 * @author maqian
 * @version 1.0
 */
public abstract class AbstractHttpService {
	private static final byte DEFAULT_RETRY_COUNT = 3;
	
	private int retryCount;
	private int tryCount;
	
	protected String serviceLocation;
	protected ByteArrayOutputStream bufferBaos;
	protected DataOutputStream bufferDos;
	protected DataOutputStream connectionDos;
	protected DataInputStream connectionDis;
	protected HttpConnection httpConnection;
	protected HeadWrapper headWrapper;
	protected int result;
	protected String message;
	
	protected AbstractHttpService(String url) {
		serviceLocation = url;
		tryCount = 0;
		retryCount = DEFAULT_RETRY_COUNT;
	}
	
	/**
	 * 返回服务结果
	 * @return 返回值<0，失败；返回值==0，成功
	 */
	public int getResult() {
		return result;
	}
	
	/**
	 * 判断服务是否成功
	 * @return
	 */
	public boolean isSuccess() {
		return result==0;
	}
	
	/**
	 * 返回错误信息
	 * @return
	 */
	public String getMessage() {
		return message;
	}
	
	protected void closeConnectionDataOutputStream() {
		if (connectionDos != null) {
			try {
				connectionDos.flush();
				connectionDos.close();
				connectionDos = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void closeConnectionDataInputStream() {
		if (connectionDis != null) {
			try {
				connectionDis.close();
				connectionDis = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void closeHttpConnection() {
		if (httpConnection != null) {
			try {
				httpConnection.close();
				httpConnection = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void close() {
		closeBufferDataOutputStream();
		closeConnectionDataOutputStream();
		closeConnectionDataInputStream();
		closeHttpConnection();
	}
	
	protected void initHead(int tag, int cmd) {
		this.headWrapper = new HeadWrapper.Builder()
			.version(Constant.PROTOCOL_VERSION).tag(tag).command(cmd).build();;
	}
	
	protected int readResult() throws IOException {
		result = connectionDis.readInt();
		if (result < 0) {
			message = connectionDis.readUTF();
		}
		return result;
	}
	
	protected void checkHead() throws IOException {
		int rc = httpConnection.getResponseCode();
		if (rc != HttpConnection.HTTP_OK) {
	        throw new ServiceException("Http Response Code: " + rc);
	    }
	    openConnectionDataInputStream();
	    HeadWrapper rspHeadWrapper = new HeadWrapper();
	    rspHeadWrapper.setHead(connectionDis.readInt());
	    if (rspHeadWrapper.getVersion() != headWrapper.getVersion()) {
	    	throw new ServiceException("协议版本不一致");
	    }
	    if (rspHeadWrapper.getTag() != headWrapper.getTag()) {
	    	throw new ServiceException("协议标识不一致");
	    }
	    if (rspHeadWrapper.getCommand() != headWrapper.getCommand()) {
	    	throw new ServiceException("协议命令不一致");
	    }
	}
	
	protected DataOutputStream openConnectionDataOutputStream() {
		if (connectionDos != null) {
			return connectionDos;
		}
		try {
			tryCount = 0;
			while (tryCount <= retryCount) {
				try {
					connectionDos = httpConnection.openDataOutputStream();
					break;
				}
				catch (IOException e) {
					++tryCount;
					if (tryCount <= retryCount) {
						System.out.println("[信息] ==> 连接服务器失败，第"+tryCount+"次重试");
					}
					else {
						throw e;
					}
				}
			}
			return connectionDos;
		} catch (IOException e) {
			throw new ServiceException("连接服务器失败");
		}
	}
	
	protected DataInputStream openConnectionDataInputStream() {
		if (connectionDis != null) {
			return connectionDis;
		}
		try {
			tryCount = 0;
			while (tryCount <= retryCount) {
				try {
					connectionDis = httpConnection.openDataInputStream();
					break;
				}
				catch (IOException e) {
					++tryCount;
					if (tryCount <= retryCount) {
						System.out.println("[信息] ==> 连接服务器失败，第"+tryCount+"次重试");
					}
					else {
						throw e;
					}
				}
			}
			return connectionDis;
		} catch (IOException e) {
			throw new ServiceException("连接服务器失败");
		}
	}
	
	protected void openConnection() {
		try {
			httpConnection = null;
			connectionDis = null;
			connectionDos = null;
			result = -1;
			message = null;
			
			tryCount = 0;
			while (tryCount <= retryCount) {
				try {
					httpConnection = (HttpConnection)Connector.open(serviceLocation, Connector.READ_WRITE, true);
					break;
				}
				catch (IOException e) {
					++tryCount;
					if (tryCount <= retryCount) {
						System.out.println("[信息] ==> 连接服务器失败，第"+tryCount+"次重试");
					}
					else {
						throw e;
					}
				}
			}
			httpConnection.setRequestMethod(HttpConnection.POST);
			httpConnection.setRequestProperty("Content-Type", "application/octet-stream");
			httpConnection.setRequestProperty("Connection", "close");
			
		} catch (IOException e) {
			throw new ServiceException("连接服务器失败");
		}
	}
	
	protected void writeData(byte[] data) throws IOException {
		openConnection();
		setContentLength(data.length);
		openConnectionDataOutputStream();
		connectionDos.write(data, 0, data.length);
		closeConnectionDataOutputStream();
	}
	
	protected void setContentLength(int len) {
		try {
			httpConnection.setRequestProperty("Content-Length", Integer.toString(len));
		} catch (IOException e) {
			throw new ServiceException("连接服务器失败");
		}
	}
	
	protected void openBufferDataOutputStream() {
		bufferBaos = new ByteArrayOutputStream();
		bufferDos = new DataOutputStream(bufferBaos);
	}
	
	protected void closeBufferDataOutputStream() {
		if (bufferDos != null) {
			try {
				bufferDos.close();
				bufferDos = null;
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (bufferBaos != null) {
			try {
				bufferBaos.close();
				bufferBaos = null;
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setTryCount(int tryCount) {
		this.tryCount = tryCount;
	}

	public int getTryCount() {
		return tryCount;
	}
}
