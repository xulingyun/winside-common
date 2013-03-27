package cn.ohyeah.stb.game;

import cn.ohyeah.itvgame.model.SubscribeProperties;
import cn.ohyeah.stb.util.DateUtil;

/**
 * 引擎服务类，主要是对外提供参数调用的统一接口
 * @author maqian
 * @version 1.0
 */

public final class EngineService implements Constant{
	
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
		loginTime = getServiceWrapper().querySystemTime();
		loginTimeMillis = System.currentTimeMillis();
		assignSubProps();
	}
	
	private void assignSubProps() {
		subProps = new SubscribeProperties();
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
			availablePoints = 0;
		}
		balance = getServiceWrapper().queryCoin();
		
		subProps.setSubscribeAmountUnit(subscribeAmountUnit);
		subProps.setSubscribeCashToAmountRatio(subscribeCashToAmountRatio);
		subProps.setPointsUnit(pointsUnit);
		subProps.setAvailablePoints(availablePoints);
		subProps.setCashToPointsRatio(cashToPointsRatio);
		subProps.setExpendCashToAmountRatio(expendCashToAmountRatio);
		subProps.setBalance(balance);
		subProps.setRechargeRatio(rechargeRatio);
		
		/*海南不支持充值和订购*/
		if(Configurations.getInstance().isTelcomOperatorsTelcomfj()){
			subProps.setSupportRecharge(isSupportRecharge_hn);
			subProps.setSupportSubscribe(isSupportSubscribe_hn);
		}else{
			subProps.setSupportRecharge(isSupportRecharge);
			subProps.setSupportSubscribe(isSupportSubscribe);
		}
		
		/*广东支持积分订购*/
		if(Configurations.getInstance().isTelcomOperatorsTelcomgd()){
			subProps.setSupportSubscribeByPoints(supportSubscribeByPoints_gd);
		}else{
			subProps.setSupportSubscribeByPoints(supportSubscribeByPoints);
		}
		
		/*天威游戏单位*/
		if(Configurations.getInstance().isTelcomOperatorsTianweiSZ()){
			subProps.setExpendAmountUnit(expendAmountUnit_tw);
		}else{
			subProps.setExpendAmountUnit(expendAmountUnit);
		}
	}
	
	public boolean isLoginSuccess() {
		return loginResult;
	}
	
	public String getLoginMessage() {
		return loginMessage;
	}
	
	public boolean userLogin() {
		ServiceWrapper sw = engine.getServiceWrapper();
		sw.userQuit();
		sw.userLogin();
		if(sw.isServiceSuccessful()){
			assignLoginInfo();
			printParams();
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
