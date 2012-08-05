package cn.ohyeah.stb.asyn;

import cn.ohyeah.stb.net.AbstractNetClient;

/**
 * 异步请求对象
 * @author maqian
 * @version 1.0
 */
public class AsynRequest {
	private String server;
	private int connType;
	private Object data;
	
	public AsynRequest(String server, Object req) {
		this(server, AbstractNetClient.CONN_TYPE_SHORT, req);
	}
	
	public AsynRequest(String server, int connType, Object req) {
		this.server = server;
		this.connType = connType;
		this.data = req;
	}
	
	public String getServer() {
		return server;
	}
	
	public void setServer(String server) {
		this.server = server;
	}
	
	public Object getData() {
		return data;
	}
	
	public void setData(Object data) {
		this.data = data;
	}

	public void setConnType(int connType) {
		this.connType = connType;
	}

	public int getConnType() {
		return connType;
	}
	
	
}
