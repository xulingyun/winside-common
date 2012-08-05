package cn.ohyeah.stb.game;

import cn.ohyeah.stb.buf.ByteBuffer;

/**
 * 协议请求中的一帧数据
 * @author maqian
 * @version 1.0
 */
public class Frame {
	//心跳包
	private static final ByteBuffer TICKS = new ByteBuffer(8);
	
	private ByteBuffer value;
	
	public boolean tryDecode(ByteBuffer buf){
		boolean enough = false;
		if (buf.length() >= 8) {
			int dataLen = buf.getInt(buf.readerIndex()+4);
			if (buf.length() >= dataLen+8) {
				enough = true;
			}
		}
		return enough;
	}
	
	public void clear() {
		value = null;
	}
	
	public ByteBuffer encode(ByteBuffer buf) {
		int dataLen = buf.length()-8;
		buf.setInt(dataLen, buf.readerIndex()+4);
		return buf;
	}
	
	public int decodePercent() {
		if (value == null) {
			return 0;
		}
		return value.length()*100/value.capacity();
	}
	
	public ByteBuffer decode(ByteBuffer buf) {
		ByteBuffer result = value;
		if (result == null) {
			if (buf.length() >= 8) {
				int dataLen = buf.getInt(buf.readerIndex()+4);
				if (dataLen == 0) {
					//心跳包
					result = TICKS;
					result.writeByteBuffer(buf, 8);
					buf.slide();
				}
				else {
					result = new ByteBuffer(dataLen+8);
					if (buf.length() >= dataLen+8) {
						result.writeByteBuffer(buf, dataLen+8);
						buf.slide();
					}
					else {
						result.writeByteBuffer(buf);
						buf.slide();
						value = result;
					}
				}
			}
		}
		else {
			if (buf.length() >= result.availabe()) {
				result.writeByteBuffer(buf, result.availabe());
				buf.slide();
				value = null;
			}
			else {
				result.writeByteBuffer(buf);
				buf.slide();
				result = null;
			}
		}
		return result;
	}
	
}
