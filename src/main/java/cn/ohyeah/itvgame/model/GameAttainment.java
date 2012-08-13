package cn.ohyeah.itvgame.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 游戏成就类
 * @author maqian
 * @version 1.0
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
	
	public void writeData(DataOutputStream dos) throws IOException {
		if (data!=null && data.length>0) {
			dos.writeInt(data.length);
			dos.write(data, 0, data.length);
		}
		else {
			dos.writeInt(0);
		}
	}
	
	public void readData(DataInputStream dis) throws IOException {
		int totalLen = dis.readInt();
		if (totalLen > 0) {
			data = new byte[totalLen];
			int readLen = 0;
			int curReadLen;
			while (readLen < totalLen) {
				curReadLen = dis.read(data, readLen, totalLen-readLen);
				if (curReadLen > 0) {
					readLen += curReadLen;
				}
			}
		}
	}

	public void writeSaveRequestData(DataOutputStream dos) throws IOException {
		dos.writeInt(attainmentId);
		dos.writeInt(playDuration);
		dos.writeInt(scores);
		dos.writeUTF(remark);
		writeData(dos);
		
	}

	public void writeUpdateRequestData(DataOutputStream dos) throws IOException {
		dos.writeInt(attainmentId);
		dos.writeInt(playDuration);
		dos.writeInt(scores);
		dos.writeUTF(remark);
		writeData(dos);
	}

	public void readReadResponseData(DataInputStream dis) throws IOException {
		attainmentId = dis.readInt();
		playDuration = dis.readInt();
		scores = dis.readInt();
		ranking = dis.readInt();
		remark = dis.readUTF();
		time = dis.readUTF();
		readData(dis);
	}
	
}
