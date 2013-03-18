package cn.ohyeah.itvgame.protocol;

/**
 * 常量
 * @author jackey chen
 * @version 1.0
 *
 */
public interface Constant {
	
	/*协议命令*/
	public static final byte SYS_SERV_SYN_TIME = 1;				/*时间同步*/
	public static final byte SYS_SERV_ADD_FAVORITEGD = 2;		/*广东，添加到收藏夹*/
	public static final byte SYS_SERV_GOTO_RECHARGE_PAGE = 3;	/*掌世界少数地区，进入充值页面*/
	public static final byte SYS_SERV_ONLINE = 4;				/*掌世界深圳天威，添加心跳机制*/
	
	public static final byte RECORD_SAVE = 1;					/*保存记录*/
	public static final byte RECORD_READ = 2;					/*读取记录*/
	
	public static final byte ATTAINMENT_SAVE = 1;				/*保存成就*/
	public static final byte ATTAINMENT_READ = 2;				/*读取成就*/
	
	public static final byte ATTAINMENT_QUERY_RANKING_LIST = 3;	/*查询排名列表*/
	
	public static final byte SUBSCRIBE_RECHARGE = 1;				/*充值*/
	public static final byte SUBSCRIBE_QUERY_BALANCE = 2;			/*查询余额*/
	public static final byte SUBSCRIBE_RECHARGE_WINSIDEGD = 3;		/*winsidegd充值*/
	
	public static final byte PURCHASE_PURCHASE_PROP = 1;			/*购买道具*/
	public static final byte PURCHASE_EXPEND = 2;					/*花费金币*/
	
	public static final byte ACCOUNT_USER_LOGIN = 3;				/*用户登录*/
	
	
	/*错误码(Error Code)*/
	public static final byte EC_RECORD_NOT_EXIST = -1;		/*游戏记录不存在*/
	public static final byte EC_ATTAINMENT_NOT_EXIST = -2;	/*游戏成就不存在*/
	
}
