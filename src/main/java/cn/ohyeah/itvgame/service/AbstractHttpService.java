package cn.ohyeah.itvgame.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.HttpConnection;

/**
 * 抽象Http服务类
 * @author jackey chan
 * @version 1.0
 */
public abstract class AbstractHttpService {
	
	public String addr_login = "login.do";
	
	protected String serviceLocation;
	protected HttpConnection httpConnection;
	protected InputStream inputStream;
	protected OutputStream outputStream;
	protected int result;
	protected String message;
	
	protected AbstractHttpService(String url) {
		if (!url.endsWith("/")) {
			url += "/";
		}
		serviceLocation = url;
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
	
	public void close(){
		if (inputStream != null){
			try {
				inputStream.close();
				inputStream = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (outputStream != null){
			try {
				outputStream.close();
				outputStream = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
			
		if (httpConnection != null){
			try {
				httpConnection.close();
				httpConnection = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
