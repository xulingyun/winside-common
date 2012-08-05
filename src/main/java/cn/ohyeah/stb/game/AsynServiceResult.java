package cn.ohyeah.stb.game;

import cn.ohyeah.stb.modelv2.SubscribePayType;
import cn.ohyeah.stb.servicev2.AsynResult;

public class AsynServiceResult {
	static final byte ACTION_NONE = 0;
	static final byte ACTION_UPDATE_BALANCE_AFTER_QUERY = 1;
	static final byte ACTION_UPDATE_BALANCE_AFTER_EXPEND = 2;
	static final byte ACTION_UPDATE_BALANCE_AFTER_RECHARGE = 3;

	private static EngineService engineService;
	private cn.ohyeah.stb.servicev2.AsynResult asynResult;
	private int postAction;
	private boolean postActionExecuted;
	private int payType;
	private int amount;

	//package private
	AsynServiceResult(AsynResult asynResult) {
		this(asynResult, ACTION_NONE);
	}

	//package private
	AsynServiceResult(AsynResult asynResult, int postAction) {
		this.asynResult = asynResult;
		this.postAction = postAction;
	}

	//package private
	static void registerEngineService(EngineService engineServ) {
		engineService = engineServ;
	}

	//package private
	void setPayType(int payType) {
		this.payType = payType;
	}

	//package private
	void setAmount(int amount) {
		this.amount = amount;
	}

	private void updateBalanceAfterQuery(Object result) {
		engineService.balance = ((Integer) result).intValue();
	}

	private void updateBalanceAfterExpend(Object result) {
		int balance = ((Integer) result).intValue();
		if (balance >= 0) {
			engineService.balance -= balance;
		} else {
			engineService.balance += balance;
		}
	}

	private void updateBalanceAfterRecharge(Object result) {
		int balance = ((Integer) result).intValue();
		if (balance > 0) {
			engineService.balance += balance;
		} else {
			engineService.balance = 0;
		}
		if (payType == SubscribePayType.PAY_TYPE_POINTS) {
			engineService.availablePoints -= amount;
		}
	}

	private void postAction(Object result) {
		if (postActionExecuted) {
			return;
		}
		postActionExecuted = true;
		if (postAction == ACTION_NONE) {
			return;
		}
		switch (postAction) {
		case ACTION_UPDATE_BALANCE_AFTER_EXPEND:
			updateBalanceAfterExpend(result);
			break;
		case ACTION_UPDATE_BALANCE_AFTER_QUERY:
			updateBalanceAfterQuery(result);
			break;
		case ACTION_UPDATE_BALANCE_AFTER_RECHARGE:
			updateBalanceAfterRecharge(result);
			break;
		default:
			throw new RuntimeException("不支持的action类型,(action=" + postAction
					+ ")");
		}
	}

	public String getErrorMessage() {
		return asynResult.getErrorMessage();
	}

	public boolean isSuccessful() {
		return asynResult.isSuccessful();
	}

	public boolean isComplete() {
		return asynResult.isComplete();
	}

	public Object getResult() {
		Object result = asynResult.getResult();
		postAction(result);
		return result;
	}

	public Object get() throws InterruptedException {
		Object result = asynResult.get();
		postAction(result);
		return result;
	}
}
