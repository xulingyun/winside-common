package cn.ohyeah.stb.modelv2;

import cn.ohyeah.stb.buf.ByteBuffer;

/**
 * ¶©¹º¼ÇÂ¼Àà
 * @author maqian
 * @version 2.0
 */
public class SubscribeRecord {
	private int amount;		/*½ð¶î*/
	private String remark;
	private String time;
	
	public int getAmcount() {
		return amount;
	}
	public void setAmcount(int amcount) {
		this.amount = amcount;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRemark() {
		return remark;
	}
	
	public void readQueryResponseData(ByteBuffer buf) {
		amount = buf.readInt();
		remark = buf.readUTF();
		time = buf.readUTF();
	}
}
