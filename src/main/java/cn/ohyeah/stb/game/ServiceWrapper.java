package cn.ohyeah.stb.game;

import java.util.Date;
import cn.ohyeah.itvgame.model.GameRanking;
import cn.ohyeah.itvgame.model.SubscribePayType;
import cn.ohyeah.itvgame.service.AccountService;
import cn.ohyeah.itvgame.service.ConsumeService;
import cn.ohyeah.itvgame.service.PropService;
import cn.ohyeah.itvgame.service.RecordService;
import cn.ohyeah.stb.util.ConvertUtil;

/**
 * 服务包装类，对服务类做了简单的包装，
 * 为了保证线程安全，请先创建新的对象再调用
 * @author maqian
 * @version 1.0
 */
public final class ServiceWrapper {
	private String server;
	private IEngine engine;
	private ParamManager pm;
	private EngineService engineService;
	
	private int result;
	private String message;
	
	public ServiceWrapper(IEngine e, String server) {
		this.engine = e;
		this.engineService = engine.getEngineService();
		this.pm = engineService.getParamManager();
		this.server = server;
	}
	
	/*用户登入*/
	public void userLogin(){
		AccountService as = new AccountService(server);
		as.cmdLogin(pm.userId, pm.accountName, pm.product, true);
		message = as.getMessage();
		result = as.getResult();
	}
	
	/*用户退出*/
	public void userQuit(){
		AccountService as = new AccountService(server);
		as.cmdLogin(pm.userId, pm.accountName, pm.product, false);
		message = as.getMessage();
		result = as.getResult();
	}
	
	/*查询系统时间*/
	public Date querySystemTime(){
		AccountService as = new AccountService(server);
		Date datas = as.querySystemTime(pm.userId, pm.accountName, pm.product, 0);
		message = as.getMessage();
		result = as.getResult();
		if(as.isSuccess() && !datas.equals("0") && !datas.equals("")){
			return datas;
		}else{
			return null;
		}
	}
	
	/*保存游戏全局数据*/
	public void saveGobalData(String data){
		AccountService as = new AccountService(server);
		as.saveGobalData(pm.userId, pm.accountName, pm.product, data);
		message = as.getMessage();
		result = as.getResult();
	}
	
	/*加载游戏全局数据*/
	public String loadGobalData(){
		AccountService as = new AccountService(server);
		String datas = as.loadGobalData(pm.userId, pm.accountName, pm.product);
		message = as.getMessage();
		result = as.getResult();
		if(as.isSuccess() && !datas.equals("0") && !datas.equals("")){
			return datas;
		}else{
			return null;
		}
	}
	
	/*保存游戏记录*/
	public void saveRecord(int index, String datas){
		RecordService rs = new RecordService(server);
		rs.saveRecord(pm.userId, pm.accountName, pm.product, index, datas);
		message = rs.getMessage();
		result = rs.getResult();
	}
	
	/*读取游戏记录*/
	public String loadRecord(int index){
		RecordService rs = new RecordService(server);
		String datas = rs.loadRecord(pm.userId, pm.accountName, pm.product, index);
		message = rs.getMessage();
		result = rs.getResult();
		if(rs.isSuccess() && !datas.equals("0") && !datas.equals("")){
			return datas;
		}else{
			return null;
		}
	}
	
	/*保存排行积分*/
	public void saveScore(int score){
		RecordService rs = new RecordService(server);
		rs.saveScore(pm.userId, pm.accountName, pm.product, score);
		message = rs.getMessage();
		result = rs.getResult();
	}
	
	/*根据服务器返回的排行信息初始化排行数据*/
	public GameRanking[] loadRanking(String datas, int offset, int length){
		GameRanking[] gameRanking = null;
		String[] data = ConvertUtil.split(datas, "|");
		String[] str = ConvertUtil.split(data[data.length-1], ":");
		if(str==null || str.equals("")){
			if(data.length>(length-offset)){
				gameRanking = new GameRanking[10];
			}else{
				gameRanking = new GameRanking[data.length];
			}
		}else{
			if(data.length-1>(length-offset)){
				gameRanking = new GameRanking[(length-offset)];
			}else{
				gameRanking = new GameRanking[data.length-1];
			}
		}
		String[] data2 = null;
		GameRanking grk = null;
		for(int i=0;i<gameRanking.length;i++){
			data2 = ConvertUtil.split(data[i], ",");
			grk = new GameRanking();
			grk.setUserId(data2[0]);
			grk.setScores(Integer.parseInt(data2[2]));
			grk.setRanking(Integer.parseInt(data2[3]));
			gameRanking[i] = grk;
		}
		return gameRanking;
	}
	
	public int getMyRanking(String datas){
		String[] data = ConvertUtil.split(datas, "|");
		String[] str = ConvertUtil.split(data[data.length-1], ":");
		if(str!=null && str[1]!=null){
			String[] myRank = ConvertUtil.split(str[1], ",");
			return Integer.parseInt(myRank[0]);
		}else{
			return -1;
		}
	}
	
	public int getMyScore(String datas){
		String[] data = ConvertUtil.split(datas, "|");
		String[] str = ConvertUtil.split(data[data.length-1], ":");
		if(str!=null && str[1]!=null){
			String[] myRank = ConvertUtil.split(str[1], ",");
			return Integer.parseInt(myRank[1]);
		}else{
			return -1;
		}
	}
	
	/*从服务器读取排行数据*/
	public String loadRanking(int type){
		RecordService rs = new RecordService(server);
		String datas = rs.queryRank(pm.userId, pm.accountName, pm.product, type);
		message = rs.getMessage();
		result = rs.getResult();
		if(rs.isSuccess() && !datas.equals("0") && !datas.equals("")){
			return datas;
		}else{
			return null;
		}
	}
	
	/*保存增值道具数据*/
	public void savePropItem(String datas){
		PropService ps = new PropService(server);
		ps.savePropItem(pm.userId, pm.accountName, pm.product, datas);
		message = ps.getMessage();
		result = ps.getResult();
	}
	
	/*加载增值道具数据*/
	public String loadPropItem(){
		PropService ps = new PropService(server);
		String datas = ps.loadPropItem(pm.userId, pm.accountName, pm.product);
		message = ps.getMessage();
		result = ps.getResult();
		if(ps.isSuccess() && !datas.equals("0") && !datas.equals("")){
			return datas;
		}else{
			return null;
		}
	}
	
	/*向服务器写购买道具的日志*/
	public void writePurchaseLog(String contentStr, int contentVal, String memo){
		AccountService as = new AccountService(server);
		as.writePurchaseLog(pm.userId, pm.accountName, pm.product, contentStr, contentVal, memo);
		message = as.getMessage();
		result = as.getResult();
	}
	
	/*向服务器发送心跳命令*/
	public void sendHeartBeat(){
		AccountService as = new AccountService(server);
		as.sendHeartBeat(pm.userId, pm.accountName, pm.product);
		message = as.getMessage();
		result = as.getResult();
	}
	
	/*查询公告*/
	public String queryNews(){
		AccountService as = new AccountService(server);
		String datas = as.queryNews(pm.product);
		message = as.getMessage();
		result = as.getResult();
		if(as.isSuccess() && !datas.equals("0") && !datas.equals("")){
			return datas;
		}else{
			return null;
		}
	}
	
	/*查询用户余额*/
	public int queryCoin(){
		ConsumeService cs = new ConsumeService(pm.buyURL);
		int money = cs.queryCoin(pm.userId);
		message = cs.getMessage();
		result = cs.getResult();
		return money;
	}
	
	/*用户消费*/
	public int consume(int amount, int coins, String info){
		ConsumeService cs = new ConsumeService(pm.buyURL);
		int money = cs.consumeCoin(pm.userId, pm.accountName, pm.checkKey, pm.product, info, amount, coins);
		message = cs.getMessage();
		result = cs.getResult();
		if(cs.isSuccess()){
			engineService.balance -= money;
		}
		return money;
	}
	
	public void recharge(int coins, int payType, String password){
		if(Configurations.getInstance().isTelcomOperatorsTelcomgd()){
			int money = rechargeGd(coins, payType);
			engineService.balance += money;
			if(payType == SubscribePayType.PAY_TYPE_POINTS){
				engineService.availablePoints -= coins;
			}
		}else{
			int money = recharge(coins, password);
			engineService.balance += money;
		}
	}
	
	/*用户通用充值*/
	private int recharge(int coins, String password){
		ConsumeService cs = new ConsumeService(pm.buyURL);
		int money = cs.recharge(pm.userId, pm.accountName, coins, pm.spid, pm.product, pm.userToken, pm.checkKey, password);
		message = cs.getMessage();
		result = cs.getResult();
		if(cs.isSuccess()){
			return money;
		}else{
			return 0;
		}
	}
	
	/*用户广东充值*/
	private int rechargeGd(int coins, int payType){
		ConsumeService cs = new ConsumeService(pm.buyURL);
		int money = cs.rechargeGd(pm.userId, pm.accountName, pm.spid, pm.stbType, pm.product, coins, pm.gameid, pm.enterURL, pm.zyUserToken, pm.checkKey, payType);
		message = cs.getMessage();
		result = cs.getResult();
		if(cs.isSuccess()){
			return money;
		}else{
			return 0;
		}
	}
	
	/*添加收藏*/
	public void addFavor(){
		AccountService as = new AccountService(pm.hosturl);
		as.addFavor(pm.userId, pm.accountName, pm.gameid, pm.spid, pm.code, pm.timeStmp);
		message = as.getMessage();
		result = as.getResult();
	}
	
	/*进入大厅充值界面*/
	public void gotoOrderPage(){
		AccountService as = new AccountService(pm.buyURL);
		as.gotoOrderPage(pm.userId);
		message = as.getMessage();
		result = as.getResult();
	}
	
	public String getMessage(){
		return message;
	}
	
	public int getResult(){
		return result;
	}
	
	public boolean isServiceSuccessful(){
		return result == 0;
	}
}
