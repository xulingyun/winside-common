package cn.ohyeah.stb.modelv2;

import cn.ohyeah.stb.buf.ByteBuffer;

/**
 * 游戏道具类
 * @author maqian
 * @version 2.0
 */
public class Prop {
	private int propId;
	private String propName;
	private int price;				/*价格*/
	private String description;		/*描述*/
	
	public int getPropId() {
		return propId;
	}
	public void setPropId(int propId) {
		this.propId = propId;
	}
	public String getPropName() {
		return propName;
	}
	public void setPropName(String propName) {
		this.propName = propName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getPrice() {
		return price;
	}
	public void readQueryResponseData(ByteBuffer buf) {
		propId = buf.readInt();
		propName = buf.readUTF();
		price = buf.readInt();
		description = buf.readUTF();
	}
}
