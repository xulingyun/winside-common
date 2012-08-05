package cn.ohyeah.stb.modelv2;

import cn.ohyeah.stb.buf.ByteBuffer;

/**
 * µÇÂ¼ÐÅÏ¢
 * @author maqian
 * @version 2.0
 */
public class LoginInfo {
	private int accountId;
	private String userId;
	private int productId;
	private String productName;
	private String appName;
	private java.util.Date systemTime;
	private Authorization auth;
	private SubscribeProperties subProps;
	
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public java.util.Date getSystemTime() {
		return systemTime;
	}
	public void setSystemTime(java.util.Date systemTime) {
		this.systemTime = systemTime;
	}
	public Authorization getAuth() {
		return auth;
	}
	public void setAuth(Authorization auth) {
		this.auth = auth;
	}
	public SubscribeProperties getSubProps() {
		return subProps;
	}
	public void setSubProps(SubscribeProperties subProps) {
		this.subProps = subProps;
	}
	
	public void readLoginInfo(ByteBuffer buf) {
		accountId = buf.readInt();
		userId = buf.readUTF();
		productId = buf.readInt();
		productName = buf.readUTF();
		appName = buf.readUTF();
		systemTime = new java.util.Date(buf.readLong());
		auth = new Authorization();
		auth.readAuthorization(buf);
		subProps = new SubscribeProperties();
		subProps.readSubscribeProperties(buf);
	}
}
