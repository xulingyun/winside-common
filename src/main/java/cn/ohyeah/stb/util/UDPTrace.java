package cn.ohyeah.stb.util;

import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.io.Datagram;
import javax.microedition.io.DatagramConnection;

public class UDPTrace {
	public static final int MAX_SIZE = 1024;
	private String connStr;
	private DatagramConnection conn;
	private Datagram packet;
	private StringBuffer buffer;
	
	public UDPTrace(String ip, int port) {
		connStr = "datagram://"+ip+":"+port;
		System.out.println(connStr);
	}
	
	private DatagramConnection createConnection() {
		if (conn == null) {
			try {
				conn = (DatagramConnection)Connector.open(connStr);
				if (conn == null) {
					System.out.println("打开连接失败,"+connStr);
				}
				else {
					System.out.println("打开连接成功,"+connStr);
				}
				packet = conn.newDatagram(MAX_SIZE);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return conn;
	}
	
	public void appendBuffer(String v) {
		buffer.append(v);
	}
	
	public void appendBuffer(int v) {
		buffer.append(v);
	}
	
	public void sendBuffer() {
		if (buffer.length() > 0) {
			send(buffer.toString());
			buffer.setLength(0);
		}
	}
	
	public void send(String v) {
		try {
			if (conn == null) {
				conn = createConnection();
			}
			packet.reset();
			packet.writeUTF(v);
			conn.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void send(int v) {
		try {
			if (conn == null) {
				conn = createConnection();
			}
			packet.reset();
			packet.writeInt(v);
			conn.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
}