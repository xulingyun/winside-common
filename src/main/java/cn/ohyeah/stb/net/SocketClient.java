package cn.ohyeah.stb.net;

import java.io.IOException;

import javax.microedition.io.SocketConnection;

/**
 * socket¿Í»§¶Ë
 * @author maqian
 * @version 1.0
 */
public class SocketClient extends AbstractNetClient {
	
	public SocketClient(String server) {
		super(server);
	}
	public SocketClient(String server, int retryCount) {
		super(server, retryCount);
	}
	public SocketClient(String server, int retryCount, int timeout) {
		super(server, retryCount, timeout);
	}

	protected void setConnOpts(int dataLen) throws IOException {
		SocketConnection conn = (SocketConnection)super.conn;
		conn.setSocketOption(SocketConnection.DELAY, 1);
		conn.setSocketOption(SocketConnection.LINGER, 10);
		conn.setSocketOption(SocketConnection.KEEPALIVE, 0);
		conn.setSocketOption(SocketConnection.RCVBUF, 512);
		conn.setSocketOption(SocketConnection.SNDBUF, 512);
	}
}
