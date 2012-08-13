package cn.ohyeah.itvgame.model;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * 消费记录类
 * @author maqian
 * @version 1.0
 */
public class PurchaseRecord {
	private int propId;
	private int propCount;
	private int amount;
	private String remark;
	private String time;
	
	public int getPropId() {
		return propId;
	}
	public void setPropId(int propId) {
		this.propId = propId;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public int getPropCount() {
		return propCount;
	}
	public void setPropCount(int propCount) {
		this.propCount = propCount;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public void readQueryResponseData(DataInputStream dis) throws IOException {
		propId = dis.readInt();
		propCount = dis.readInt();
		amount = dis.readInt();
		remark = dis.readUTF();
		time = dis.readUTF();
	}
}
