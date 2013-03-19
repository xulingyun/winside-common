package cn.ohyeah.stb.game;

import cn.ohyeah.itvgame.model.SubscribeProperties;
import cn.ohyeah.stb.util.DateUtil;

/**
 * 引擎服务类，主要是对外提供参数调用的统一接口
 * @author maqian
 * @version 1.0
 */

public final class EngineService {
	
	java.util.Date loginTime;	/*用户登录时间*/
	long loginTimeMillis;		/*登录成功时的机顶盒时间*/

	SubscribeProperties subProps;
	int availablePoints;		/*可用的积分*/
	int balance;				/*当前余额*/
	
	private IEngine engine;
	private ParamManager pm;
	
	private boolean loginResult;
	private String loginMessage;
	public boolean isRechrageSuccess;   //有童锁功能，充值成功后下次就不需要输入密码
	public String passWord;				//充值密码
	
	public EngineService(IEngine engine) {
		this.engine = engine;
		this.pm = new ParamManager(engine);
	}
	
	private void printParams() {
		System.out.println("loginTime: "+DateUtil.formatTimeStr(loginTime));
		System.out.println("server: "+pm.server);
		System.out.println("buyURL: "+pm.buyURL);
		System.out.println("userId: "+pm.userId);
		System.out.println("accountName: "+pm.accountName);
		System.out.println("userToken: "+pm.userToken);
		System.out.println("productName: "+pm.product);
		System.out.println("checkKey: "+pm.checkKey);
		
		subProps.print();
	}
	
	public String toString() {
		String msg = "loginTime"+" ==> "+DateUtil.formatTimeStr(loginTime)+"\n";
		msg += "server"+" ==> "+pm.server+"\n";
		msg += "buyURL"+" ==> "+pm.buyURL+"\n";
		msg += "userId"+" ==> "+pm.userId+"\n";
		msg += "accountName"+" ==> "+pm.accountName+"\n";
		msg += "userToken"+" ==> "+pm.userToken+"\n";
		msg += "productName"+" ==> "+pm.product+"\n";
		msg += "checkKey"+" ==> "+pm.checkKey;
		return msg;
	}
	
	private void assignLoginInfo() {
		/*pm.product = info.getProductName();
		loginTime = info.getSystemTime();
		loginTimeMillis = System.currentTimeMillis();
		assignSubProps(info.getSubProps());*/
		
	}
	
	private void assignSubProps(SubscribeProperties subProps) {
		this.subProps = subProps;
		if (Configurations.getInstance().isTelcomOperatorsTelcomgd()) {
			try {
				availablePoints = Integer.parseInt(pm.myDXScore);
			}
			catch (Exception e) {
				e.printStackTrace();
				availablePoints = 0;
			}
		}
		else {
			availablePoints = subProps.getAvailablePoints();
		}
		balance = subProps.getBalance();
	}
	
	public void setupOfflineParam() {
		loginTime = new java.util.Date();
		loginTimeMillis = System.currentTimeMillis();

		subProps = new SubscribeProperties();
		subProps.setSupportSubscribe(true);
		subProps.setSubscribeAmountUnit("元");
		subProps.setSubscribeCashToAmountRatio(1);
		subProps.setSupportSubscribeByPoints(true);
		subProps.setPointsUnit("积分");
		subProps.setAvailablePoints(0);
		subProps.setCashToPointsRatio(100);
		subProps.setSupportRecharge(true);
		subProps.setExpendAmountUnit("元宝");
		subProps.setExpendCashToAmountRatio(10);
		subProps.setBalance(0);
		subProps.setRechargeRatio(10);
	}
	
	
	public boolean isLoginSuccess() {
		return loginResult;
	}
	
	public String getLoginMessage() {
		return loginMessage;
	}
	
	public boolean userLogin() {
		ServiceWrapper sw = engine.getServiceWrapper();
		sw.userLogin();
		if(sw.isServiceSuccessful()){
			
		}else{
			
		}
		loginMessage = sw.getMessage();
		loginResult = sw.isServiceSuccessful();
		return loginResult;
	}
	
	public String getRechargeCommand() {
		return Configurations.getInstance().getRechargeCmd();
	}
	
	public String getUserId() {
		return pm.userId;
	}
	
	public String getAccountName() {
		return pm.accountName;
	}
	
	public String getProduct() {
		return pm.product;
	}
	
	public boolean isSupportRecharge() {
		return subProps.isSupportRecharge();
	}
	
	public String getExpendAmountUnit() {
		return subProps.getExpendAmountUnit();
	}
	
	public int getRechargeRatio() {
		return subProps.getRechargeRatio();
	}
	
	public boolean isSupportSubscribe() {
		return subProps.isSupportSubscribe();
	}
	
	public String getSubscribeAmountUnit() {
		return subProps.getSubscribeAmountUnit();
	}
	
	public int getSubscribeCashToAmountRatio() {
		return subProps.getSubscribeCashToAmountRatio();
	}
	
	public int getExpendCashToAmountRatio() {
		return subProps.getExpendCashToAmountRatio();
	}
	
	public boolean isSupportSubscribeByPoints() {
		return subProps.isSupportSubscribeByPoints();
	}
	
	public String getPointsUnit() {
		return subProps.getPointsUnit();
	}
	
	public int getAvailablePoints() {
		return availablePoints;
	}
	
	public int getBalance() {
		return balance;
	}
	
	public int getCashToPointsRatio() {
		return subProps.getCashToPointsRatio();
	}
	
	public java.util.Date getLoginTime() {
		return loginTime;
	}
	
	public java.util.Date getCurrentTime() {
		long pastMillis = System.currentTimeMillis() - loginTimeMillis;
		return new java.util.Date(loginTime.getTime()+pastMillis);
	}
	
	public int[] getRechargeAmounts() {
		return pm.rechargeAmounts;
	}
	
	public int calcSubscribeAmount(int amount) {
		return amount*getSubscribeCashToAmountRatio();
	}
	
	public int calcExpendAmount(int amount) {
		return (short)(amount*getExpendCashToAmountRatio()/getRechargeRatio());
	}
	
	public ServiceWrapper getServiceWrapper() {
		return new ServiceWrapper(engine, pm.server);
	}
	
	public ServiceWrapper getServiceWrapper(String server) {
		return new ServiceWrapper(engine, server);
	}
	
	ParamManager getParamManager() {
		return pm;
	}
}
