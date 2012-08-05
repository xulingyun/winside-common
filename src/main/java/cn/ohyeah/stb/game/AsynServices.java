package cn.ohyeah.stb.game;

import cn.ohyeah.stb.modelv2.GameAttainment;
import cn.ohyeah.stb.modelv2.GameRecord;
import cn.ohyeah.stb.modelv2.SubscribePayType;
import cn.ohyeah.stb.net.AbstractNetClient;
import cn.ohyeah.stb.servicev2.AccountService;
import cn.ohyeah.stb.servicev2.AsynResult;
import cn.ohyeah.stb.servicev2.AttainmentService;
import cn.ohyeah.stb.servicev2.PropService;
import cn.ohyeah.stb.servicev2.PurchaseService;
import cn.ohyeah.stb.servicev2.RecordService;
import cn.ohyeah.stb.servicev2.SubscribeService;
import cn.ohyeah.stb.servicev2.SystemService;

/**
 * 异步服务包装类
 * 
 * @author maqian
 * @version 2.0
 */
public class AsynServices {
	private static IEngine engine;
	private static ParamManager paramManager;
	private static EngineService engineService;

	private String server;
	private int connType;

	private AsynServices(String server, int connType) {
		this.server = server;
		this.connType = connType;
	}

	public static void registerEngine(IEngine eng) {
		engine = eng;
		engineService = engine.getEngineService();
		paramManager = engineService.getParamManager();
		AsynServiceResult.registerEngineService(engineService);
	}

	public static AsynServices createAsynServices() {
		return createAsynServices(paramManager.server);
	}

	public static AsynServices createAsynServices(String server) {
		return createAsynServices(server, AbstractNetClient.CONN_TYPE_SHORT);
	}

	public static AsynServices createLongConnAsynServices() {
		return createLongConnAsynServices(paramManager.server);
	}

	public static AsynServices createLongConnAsynServices(String server) {
		return createAsynServices(server, AbstractNetClient.CONN_TYPE_LONG);
	}

	private static AsynServices createAsynServices(String server, int connType) {
		return new AsynServices(server, connType);
	}

	public final AsynServiceResult queryAuthorization()
			throws InterruptedException {
		AccountService serv = new AccountService(server, connType);
		AsynResult asynResult = serv.getAuthorization(paramManager.accountId,
				paramManager.productId);
		return new AsynServiceResult(asynResult);
	}

	public final AsynServiceResult querySubscribeProperties()
			throws InterruptedException {
		AccountService serv = new AccountService(server, connType);
		AsynResult asynResult = serv.getSubscribeProperties(
				paramManager.buyURL, paramManager.accountId,
				paramManager.accountName, paramManager.userToken,
				paramManager.productId, paramManager.checkKey);
		return new AsynServiceResult(asynResult);
	}

	public final AsynServiceResult userLogin() throws InterruptedException {
		AccountService serv = new AccountService(server, connType);
		AsynResult asynResult = serv.userLogin(paramManager.buyURL,
				paramManager.userId, paramManager.accountName,
				paramManager.userToken, paramManager.appName,
				paramManager.checkKey);
		return new AsynServiceResult(asynResult);
	}

	public final void queryAttainmentDescList() throws InterruptedException {
		queryAttainmentDescList("desc", null, null);
	}

	public final void queryAttainmentDescList(java.util.Date start,
			java.util.Date end) throws InterruptedException {
		queryAttainmentDescList("desc", start, end);
	}

	public final AsynServiceResult queryAttainmentDescList(String orderCmd,
			java.util.Date start, java.util.Date end)
			throws InterruptedException {
		AttainmentService serv = new AttainmentService(server, connType);
		AsynResult asynResult = serv.queryDescList(paramManager.accountId,
				paramManager.productId, orderCmd, start, end);
		return new AsynServiceResult(asynResult);
	}

	public final void queryRankingList(int offset, int length)
			throws InterruptedException {
		queryRankingList("desc", null, null, offset, length);
	}

	public final void queryRankingList(java.util.Date start,
			java.util.Date end, int offset, int length)
			throws InterruptedException {
		queryRankingList("desc", start, end, offset, length);
	}

	public final AsynServiceResult queryRankingList(String orderCmd,
			java.util.Date start, java.util.Date end, int offset, int length)
			throws InterruptedException {
		AttainmentService serv = new AttainmentService(server, connType);
		AsynResult asynResult = serv.queryRankingList(paramManager.productId,
				orderCmd, start, end, offset, length);
		return new AsynServiceResult(asynResult);
	}

	public final void readAttainment(int attainmentId)
			throws InterruptedException {
		readAttainment(attainmentId, "desc", null, null);
	}

	public final void readAttainment(int attainmentId, java.util.Date start,
			java.util.Date end) throws InterruptedException {
		readAttainment(attainmentId, "desc", start, end);
	}

	public final AsynServiceResult readAttainment(int attainmentId,
			String orderCmd, java.util.Date start, java.util.Date end)
			throws InterruptedException {
		AttainmentService serv = new AttainmentService(server, connType);
		AsynResult asynResult = serv.read(paramManager.accountId,
				paramManager.productId, attainmentId, orderCmd, start, end);
		return new AsynServiceResult(asynResult);
	}

	public final AsynServiceResult saveAttainment(GameAttainment attainment)
			throws InterruptedException {
		AttainmentService serv = new AttainmentService(server, connType);
		AsynResult asynResult = serv.save(paramManager.accountId,
				paramManager.userId, paramManager.productId, attainment);
		return new AsynServiceResult(asynResult);
	}

	public final AsynServiceResult updateAttainment(GameAttainment attainment)
			throws InterruptedException {
		AttainmentService serv = new AttainmentService(server, connType);
		AsynResult asynResult = serv.update(paramManager.accountId,
				paramManager.userId, paramManager.productId, attainment);
		return new AsynServiceResult(asynResult);
	}

	public final AsynServiceResult queryOwnPropList()
			throws InterruptedException {
		PropService serv = new PropService(server, connType);
		AsynResult asynResult = serv.queryOwnPropList(paramManager.accountId,
				paramManager.productId);
		return new AsynServiceResult(asynResult);
	}

	public final AsynServiceResult queryGamePropList()
			throws InterruptedException {
		PropService serv = new PropService(server, connType);
		AsynResult asynResult = serv.queryPropList(paramManager.productId);
		return new AsynServiceResult(asynResult);
	}

	public final void synProps(int propId, int count)
			throws InterruptedException {
		synProps(new int[] { propId }, new int[] { count });
	}

	public final AsynServiceResult synProps(int[] propIds, int[] counts)
			throws InterruptedException {
		PropService serv = new PropService(server, connType);
		AsynResult asynResult = serv.synProps(paramManager.accountId,
				paramManager.productId, propIds, counts);
		return new AsynServiceResult(asynResult);
	}

	public final void useProps(int propId, int num) throws InterruptedException {
		useProps(new int[] { propId }, new int[] { num });
	}

	public final AsynServiceResult useProps(int[] propIds, int[] nums)
			throws InterruptedException {
		PropService serv = new PropService(server, connType);
		AsynResult asynResult = serv.useProps(paramManager.accountId,
				paramManager.productId, propIds, nums);
		return new AsynServiceResult(asynResult);
	}

	public final AsynServiceResult expend(int amount, String remark)
			throws InterruptedException {
		PurchaseService serv = new PurchaseService(server, connType);
		AsynResult asynResult = serv.expend(paramManager.buyURL,
				paramManager.accountId, paramManager.accountName,
				paramManager.userToken, paramManager.productId, amount, remark,
				paramManager.checkKey);
		return new AsynServiceResult(asynResult,
				AsynServiceResult.ACTION_UPDATE_BALANCE_AFTER_EXPEND);
	}

	public final AsynServiceResult purchaseProp(int propId, int propCount,
			String remark) throws InterruptedException {
		PurchaseService serv = new PurchaseService(server, connType);
		AsynResult asynResult = serv.purchaseProp(paramManager.buyURL,
				paramManager.accountId, paramManager.accountName,
				paramManager.userToken, paramManager.productId, propId,
				propCount, remark, paramManager.checkKey);
		return new AsynServiceResult(asynResult,
				AsynServiceResult.ACTION_UPDATE_BALANCE_AFTER_EXPEND);
	}

	public final AsynServiceResult queryPurchaseRecord(int offset, int length)
			throws InterruptedException {
		PurchaseService serv = new PurchaseService(server, connType);
		AsynResult asynResult = serv.queryPurchasePropRecord(
				paramManager.accountId, paramManager.productId, offset, length);
		return new AsynServiceResult(asynResult);
	}

	public final AsynServiceResult queryRecordDescList()
			throws InterruptedException {
		RecordService serv = new RecordService(server, connType);
		AsynResult asynResult = serv.queryDescList(paramManager.accountId,
				paramManager.productId);
		return new AsynServiceResult(asynResult);
	}

	public final AsynServiceResult readRecord(int recordId)
			throws InterruptedException {
		RecordService serv = new RecordService(server, connType);
		AsynResult asynResult = serv.read(paramManager.accountId,
				paramManager.productId, recordId);
		return new AsynServiceResult(asynResult);
	}

	public final AsynServiceResult saveRecord(GameRecord record)
			throws InterruptedException {
		RecordService serv = new RecordService(server, connType);
		AsynResult asynResult = serv.save(paramManager.accountId,
				paramManager.productId, record);
		return new AsynServiceResult(asynResult);
	}

	public final AsynServiceResult updateRecord(GameRecord record)
			throws InterruptedException {
		RecordService serv = new RecordService(server, connType);
		AsynResult asynResult = serv.update(paramManager.accountId,
				paramManager.productId, record);
		return new AsynServiceResult(asynResult);
	}

	public final AsynServiceResult queryBalance() throws InterruptedException {
		SubscribeService serv = new SubscribeService(server, connType);
		AsynResult asynResult = serv.queryBalance(paramManager.buyURL,
				paramManager.accountId, paramManager.accountName,
				paramManager.productId);
		return new AsynServiceResult(asynResult,
				AsynServiceResult.ACTION_UPDATE_BALANCE_AFTER_QUERY);
	}

	public final AsynServiceResult querySubscribeRecord(int offset, int length)
			throws InterruptedException {
		SubscribeService serv = new SubscribeService(server, connType);
		AsynResult asynResult = serv.querySubscribeRecord(paramManager.userId,
				paramManager.productId, offset, length);
		return new AsynServiceResult(asynResult);
	}

	public final void recharge(int amount, String remark, String password)
			throws InterruptedException {
		recharge(amount, SubscribePayType.PAY_TYPE_BILL, remark, password);
	}

	public final AsynServiceResult recharge(int amount, int payType,
			String remark, String password) throws InterruptedException {
		AsynResult asynResult = null;
		SubscribeService serv = new SubscribeService(server, connType);
		if (Configurations.getInstance().isTelcomOperatorsTelcomgd()) {
			asynResult = serv.rechargeWinsidegd(paramManager.buyURL,
					paramManager.accountId, paramManager.accountName,
					paramManager.userToken, paramManager.productId, amount,
					engineService.subProps.getRechargeRatio(), payType, remark,
					paramManager.checkKey, paramManager.spid,
					paramManager.gameid, paramManager.enterURL,
					paramManager.stbType, password);
		} else {
			asynResult = serv.recharge(paramManager.buyURL,
					paramManager.accountId, paramManager.accountName,
					paramManager.userToken, paramManager.productId, amount,
					engineService.subProps.getRechargeRatio(), payType, remark,
					paramManager.checkKey, paramManager.spid, password);
		}
		AsynServiceResult servResult = new AsynServiceResult(asynResult,
				AsynServiceResult.ACTION_UPDATE_BALANCE_AFTER_RECHARGE);
		servResult.setAmount(amount);
		servResult.setPayType(payType);
		return servResult;
	}

	public final void subscribeProduct(String subscribeType, String remark)
			throws InterruptedException {
		subscribeProduct(subscribeType, SubscribePayType.PAY_TYPE_BILL, remark);
	}

	public final AsynServiceResult subscribeProduct(String subscribeType,
			int payType, String remark) throws InterruptedException {
		SubscribeService serv = new SubscribeService(server, connType);
		AsynResult asynResult = serv.subscribeProduct(paramManager.buyURL,
				paramManager.accountId, paramManager.accountName,
				paramManager.userToken, paramManager.productId, subscribeType,
				payType, remark, paramManager.checkKey);
		return new AsynServiceResult(asynResult);
	}

	public final AsynServiceResult querySystemTime()
			throws InterruptedException {
		SystemService serv = new SystemService(server, connType);
		AsynResult asynResult = serv.getSystemTime();
		return new AsynServiceResult(asynResult);
	}

	public final AsynServiceResult addFavoritegd() throws InterruptedException {
		SystemService serv = new SystemService(server, connType);
		AsynResult asynResult = serv.addFavoritegd(paramManager.hosturl,
				paramManager.accountId, paramManager.userId,
				paramManager.accountName, paramManager.productId,
				paramManager.gameid, paramManager.spid, paramManager.code,
				paramManager.timeStmp);
		return new AsynServiceResult(asynResult);
	}
}
