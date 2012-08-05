package cn.ohyeah.stb.modelv2;

import cn.ohyeah.stb.buf.ByteBuffer;

/**
 * 游戏成就类
 * @author maqian
 * @version 2.0
 */
public class GameAttainment {
	private int attainmentId;
	private String userId;
	private int playDuration;	/*游戏时长(单位：秒)*/
	private int scores;			/*游戏积分*/
	private int ranking;
	private String remark = "";
	private String time;
	private byte[] data;
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

	public int getScores() {
		return scores;
	}
	public void setScores(int scores) {
		this.scores = scores;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public void setPlayDuration(int playDuration) {
		this.playDuration = playDuration;
	}
	public int getPlayDuration() {
		return playDuration;
	}
	public void setAttainmentId(int attainmentId) {
		this.attainmentId = attainmentId;
	}
	public int getAttainmentId() {
		return attainmentId;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public byte[] getData() {
		return data;
	}
	
	public void setRanking(int ranking) {
		this.ranking = ranking;
	}
	public int getRanking() {
		return ranking;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void writeData(ByteBuffer buf) {
		if (data!=null && data.length>0) {
			buf.writeInt(data.length);
			buf.writeBytes(data, 0, data.length);
		}
		else {
			buf.writeInt(0);
		}
	}
	
	public void readData(ByteBuffer buf) {
		int len = buf.readInt();
		if (len > 0) {
			data = buf.readBytes(len);
		}
	}

	public void writeSaveRequestData(ByteBuffer buf) {
		buf.writeInt(attainmentId);
		buf.writeInt(playDuration);
		buf.writeInt(scores);
		buf.writeUTF(remark);
		writeData(buf);
		
	}

	public void writeUpdateRequestData(ByteBuffer buf) {
		buf.writeInt(attainmentId);
		buf.writeInt(playDuration);
		buf.writeInt(scores);
		buf.writeUTF(remark);
		writeData(buf);
	}
	
	public void readReadResponseData(ByteBuffer buf) {
		attainmentId = buf.readInt();
		playDuration = buf.readInt();
		scores = buf.readInt();
		ranking = buf.readInt();
		remark = buf.readUTF();
		time = buf.readUTF();
		readData(buf);
	}
	
}
