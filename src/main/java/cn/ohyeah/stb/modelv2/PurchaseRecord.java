package cn.ohyeah.stb.modelv2;

import cn.ohyeah.stb.buf.ByteBuffer;

/**
 * 消费记录类
 * @author maqian
 * @version 2.0
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
	public void readQueryResponseData(ByteBuffer buf) {
		propId = buf.readInt();
		propCount = buf.readInt();
		amount = buf.readInt();
		remark = buf.readUTF();
		time = buf.readUTF();
	}
}
