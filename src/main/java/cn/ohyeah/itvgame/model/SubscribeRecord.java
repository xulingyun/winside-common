package cn.ohyeah.itvgame.model;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * ¶©¹º¼ÇÂ¼Àà
 * @author maqian
 * @version 1.0
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
	
	public void readQueryResponseData(DataInputStream dis) throws IOException {
		amount = dis.readInt();
		remark = dis.readUTF();
		time = dis.readUTF();
	}
}
