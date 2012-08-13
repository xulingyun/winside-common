package cn.ohyeah.itvgame.model;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * 玩家拥有道具信息类
 * @author maqian
 * @version 1.0
 */
public class OwnProp {
	private int propId;		/*道具ID*/
	private int count;		/*数量*/
	
	public int getPropId() {
		return propId;
	}
	public void setPropId(int propId) {
		this.propId = propId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public void readQueryResponseData(DataInputStream dis) throws IOException {
		propId = dis.readInt();
		count = dis.readInt();
	}
	
}
