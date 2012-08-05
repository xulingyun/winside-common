package cn.ohyeah.stb.net;


import java.io.IOException;

import javax.microedition.io.HttpConnection;

import cn.ohyeah.itvgame.service.ServiceException;

/**
 * http¿Í»§¶Ë
 * @author maqian
 * @version 1.0
 */
public class HttpClient extends AbstractNetClient{

	public HttpClient(String server) {
		super(server);
	}
	
	public HttpClient(String server, int retryCount) {
		super(server, retryCount);
	}
	
	public HttpClient(String server, int retryCount, int timeout) {
		super(server, retryCount, timeout);
	}

	protected void setConnOpts(int dataLen) throws IOException {
		HttpConnection conn = (HttpConnection)super.conn;
		conn.setRequestMethod(HttpConnection.POST);
		conn.setRequestProperty("Content-Type", "application/octet-stream");
		conn.setRequestProperty("Content-Length", Integer.toString(dataLen));
		conn.setRequestProperty("Connection", "close");
	}
	
	protected void logicBeforeReceive() throws IOException {
		HttpConnection conn = (HttpConnection)super.conn;
		if (conn.getResponseCode() != HttpConnection.HTTP_OK) {
	        throw new ServiceException(conn.getResponseMessage());
	    }
	}
}
