package cn.ohyeah.itvgame.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

/**
 * 抽象Http服务类
 * @author jackey chan
 * @version 1.0
 */
public abstract class AbstractHttpService {
	
	public String addr_login = "login.do";							/*登入地址*/
	public String addr_quit = "quit.do";							/*退出地址*/
	public String addr_queryTime = "query_time.do";					/*查询系统时间地址*/
	public String addr_saveGlobalData = "save_global_data.do";		/*保存全局数据地址*/
	public String addr_loadGlobalData = "load_global_data.do";		/*加载全局数据地址*/
	public String addr_saveRecord = "save_record.do";				/*存档地址*/
	public String addr_loadRecord = "load_record.do";				/*读档地址*/
	public String addr_saveScore = "save_score.do";					/*保存积分地址*/
	public String addr_queryRank = "query_rank.do";					/*查询排行地址*/
	public String addr_saveItem = "save_shop_item.do";				/*保存增值道具地址*/
	public String addr_loadItem = "load_shop_item.do";				/*读取增值道具地址*/
	public String addr_log = "log.do";								/*向服务器写购买日志地址*/
	public String addr_heartBeat = "heart_beat.do";					/*心跳命令地址*/
	public String addr_news = "news.do";							/*查询公告地址*/
	
	public String addr_query_coins = "query_coins";					/*元宝查询地址*/
	public String addr_consume_coins = "consume_coins";				/*元宝扣除地址*/
	public String addr_order_coins = "order_coins";					/*元宝充值地址*/
	
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
	 * 返回信息
	 * @return
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * 发送命令
	 */
	public String postViaHttpConnection(String url, String cmd) throws IOException {
		int rc;
		try {
			url += "?" + cmd;
			System.out.println("url:"+url);
			httpConnection = (HttpConnection) Connector.open(url);
			httpConnection.setRequestMethod(HttpConnection.GET);
			rc = httpConnection.getResponseCode();
			if (rc != HttpConnection.HTTP_OK) {
				result = -1;
				throw new IOException("HTTP response code: " + rc);
			}
			System.out.println("request state:"+rc);
			inputStream = httpConnection.openInputStream();
			
			int count = 0;
			while (count == 0) {
				count = inputStream.available();
			}
			byte[] bytes = new byte[count];
			int readCount = 0; // 已经成功读取的字节的个数
			while (readCount < count) {
				readCount += inputStream.read(bytes, readCount, count - readCount);
			}
			
			String str = new String(bytes,"utf-8");
			System.out.println("return message:"+str);
			return str;
		} catch (ClassCastException e) {
			result = -1;
			throw new IllegalArgumentException("Not an HTTP URL");
		} finally {
			close();
		}
	}
	
	private void close(){
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
