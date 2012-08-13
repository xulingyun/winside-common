package cn.ohyeah.itvgame.model;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * 订购相关属性类
 * @author maqian
 * @version 1.0
 */
public class SubscribeProperties {
	private boolean supportSubscribe;
	private String subscribeAmountUnit;
	private int subscribeCashToAmountRatio;
	private boolean supportSubscribeByPoints;
	private String pointsUnit;
	private int availablePoints;
	private int cashToPointsRatio;
	private boolean supportRecharge;
	private String expendAmountUnit;
	private int expendCashToAmountRatio;
	private int balance;
	private int rechargeRatio;
	
	public boolean isSupportRecharge() {
		return supportRecharge;
	}
	public void setSupportRecharge(boolean supportRecharge) {
		this.supportRecharge = supportRecharge;
	}
	public boolean isSupportSubscribe() {
		return supportSubscribe;
	}
	public void setSupportSubscribe(boolean supportSubscribe) {
		this.supportSubscribe = supportSubscribe;
	}
	public String getSubscribeAmountUnit() {
		return subscribeAmountUnit;
	}
	public void setSubscribeAmountUnit(String subscribeAmountUnit) {
		this.subscribeAmountUnit = subscribeAmountUnit;
	}
	public boolean isSupportSubscribeByPoints() {
		return supportSubscribeByPoints;
	}
	public void setSupportSubscribeByPoints(boolean supportSubscribeByPoints) {
		this.supportSubscribeByPoints = supportSubscribeByPoints;
	}
	public String getPointsUnit() {
		return pointsUnit;
	}
	public void setPointsUnit(String pointsUnit) {
		this.pointsUnit = pointsUnit;
	}
	public int getAvailablePoints() {
		return availablePoints;
	}
	public void setAvailablePoints(int availablePoints) {
		this.availablePoints = availablePoints;
	}
	public int getCashToPointsRatio() {
		return cashToPointsRatio;
	}
	public void setCashToPointsRatio(int cashToPointsRatio) {
		this.cashToPointsRatio = cashToPointsRatio;
	}

	public void setExpendAmountUnit(String expendAmountUnit) {
		this.expendAmountUnit = expendAmountUnit;
	}
	public String getExpendAmountUnit() {
		return expendAmountUnit;
	}
	public void setRechargeRatio(int rechargeRatio) {
		this.rechargeRatio = rechargeRatio;
	}
	public int getRechargeRatio() {
		return rechargeRatio;
	}
	
	public void setBalance(int balance) {
		this.balance = balance;
	}
	public int getBalance() {
		return balance;
	}
	public void setSubscribeCashToAmountRatio(int cashToAmountRatio) {
		this.subscribeCashToAmountRatio = cashToAmountRatio;
	}
	public int getSubscribeCashToAmountRatio() {
		return subscribeCashToAmountRatio;
	}
	
	public int getExpendCashToAmountRatio() {
		return expendCashToAmountRatio;
	}
	public void setExpendCashToAmountRatio(int expendCashToAmountUnit) {
		this.expendCashToAmountRatio = expendCashToAmountUnit;
	}
	public void readSubscribeProperties(DataInputStream dis) throws IOException {
		supportSubscribe = dis.readBoolean();
		subscribeAmountUnit = dis.readUTF();
		subscribeCashToAmountRatio = dis.readInt();
		supportSubscribeByPoints = dis.readBoolean();
		pointsUnit = dis.readUTF();
		availablePoints = dis.readInt();
		cashToPointsRatio = dis.readInt();
		supportRecharge = dis.readBoolean();
		expendAmountUnit = dis.readUTF();
		expendCashToAmountRatio = dis.readInt();
		balance = dis.readInt();
		rechargeRatio = dis.readInt();
	}

	public void print() {
		System.out.println("supportSubscribe: "+supportSubscribe);
		System.out.println("subscribeAmountUnit: "+subscribeAmountUnit);
		System.out.println("cashToAmountRatio: "+subscribeCashToAmountRatio);
		System.out.println("supportSubscribeByPoints: "+supportSubscribeByPoints);
		System.out.println("pointsUnit: "+pointsUnit);
		System.out.println("availablePoints: "+availablePoints);
		System.out.println("cashToPointsRatio: "+cashToPointsRatio);
		System.out.println("supportRecharge: "+supportRecharge);
		System.out.println("expendAmountUnit: "+expendAmountUnit);
		System.out.println("expendCashToAmountRatio: "+expendCashToAmountRatio);
		System.out.println("balance: "+balance);
		System.out.println("rechargeRatio: "+rechargeRatio);
	}
}
