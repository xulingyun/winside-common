package cn.ohyeah.itvgame.model;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * 游戏排行类
 * @author maqian
 * @version 1.0
 */
public class GameRanking {
	private int accountId;
	private String userId;
	private int playDuration;	/*游戏时长(单位：秒)*/
	private int scores;			/*游戏积分*/
	private int ranking;
	private String remark;
	private String time;
	
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getPlayDuration() {
		return playDuration;
	}
	public void setPlayDuration(int playDuration) {
		this.playDuration = playDuration;
	}
	public int getScores() {
		return scores;
	}
	public void setScores(int scores) {
		this.scores = scores;
	}
	public int getRanking() {
		return ranking;
	}
	public void setRanking(int ranking) {
		this.ranking = ranking;
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
		accountId = dis.readInt();
		userId = dis.readUTF();
		playDuration = dis.readInt();
		scores = dis.readInt();
		ranking = dis.readInt();
		remark = dis.readUTF();
		time = dis.readUTF();
	}
	
}
