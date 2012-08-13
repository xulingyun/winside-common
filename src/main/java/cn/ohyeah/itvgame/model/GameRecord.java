package cn.ohyeah.itvgame.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 游戏记录类
 * @author maqian
 * @version 1.0
 */
public class GameRecord {
	private int recordId;
	private int playDuration;	/*游戏时长(单位：秒)*/
	private int scores;			/*游戏积分*/
	private String remark = "";
	private String time;
	private byte []data;

	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}
	public int getRecordId() {
		return recordId;
	}
	
	public void setData(byte [] data) {
		this.data = data;
	}
	
	public byte [] getData() {
		return data;
	} 
	
	public void setPlayDuration(int playDuration) {
		this.playDuration = playDuration;
	}
	
	public int getPlayDuration() {
		return playDuration;
	}
	
	public void setScores(int scores) {
		this.scores = scores;
	}
	
	public int getScores() {
		return scores;
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
		dos.writeInt(recordId);
		dos.writeInt(playDuration);
		dos.writeInt(scores);
		dos.writeUTF(remark);
		writeData(dos);
	}
	
	public void writeUpdateRequestData(DataOutputStream dos) throws IOException {
		dos.writeInt(recordId);
		dos.writeInt(playDuration);
		dos.writeInt(scores);
		dos.writeUTF(remark);
		writeData(dos);
	}
	
	public void readReadResponseData(DataInputStream dis) throws IOException {
		recordId = dis.readInt();
		playDuration = dis.readInt();
		scores = dis.readInt();
		remark = dis.readUTF();
		time = dis.readUTF();
		readData(dis);
	}
}
