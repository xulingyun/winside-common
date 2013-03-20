package cn.ohyeah.stb.game;

public interface Constant {

	public static final boolean isSupportSubscribe = true;
	public static final String subscribeAmountUnit = "元";
	public static final int subscribeCashToAmountRatio = 1;
	public static final boolean supportSubscribeByPoints = false;
	public static final String pointsUnit = "积分";
	public static final int cashToPointsRatio = 100;
	public static final boolean isSupportRecharge = true;
	public static final String expendAmountUnit = "元宝";
	public static final int expendCashToAmountRatio = 10;
	public static final int rechargeRatio = 10;
	
	/*天威货币单位*/
	public static final String expendAmountUnit_tw = "v币";
	
	/*海南不支持充值*/
	public static final boolean isSupportSubscribe_hn = false;
	public static final boolean isSupportRecharge_hn = false;
	
	/*广东支持积分订购*/
	public static final boolean supportSubscribeByPoints_gd = true;
}
