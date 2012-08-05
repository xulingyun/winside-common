package cn.ohyeah.stb.buf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.ohyeah.stb.util.ConvertUtil;

public class LoopByteBuffer {
	private static final String MSG_OVER_FLOW = "buffer over flow";
	
	private byte[] buffer;
	private int length;
	private int readerIndex;
	private int writerIndex;
	
	public LoopByteBuffer() {
		this(512);
	}
	
	public LoopByteBuffer(int size) {
		this.buffer = new byte[size];
	}
		
	public LoopByteBuffer(byte[] bytes) {
		this.buffer = bytes;
		this.length = bytes.length;
		this.readerIndex = 0;
		this.writerIndex = 0;
	}
	
	static void checkBounds(int off, int len, int size) { // package-private
        if ((off | len | (off + len) | (size - (off + len))) < 0) {
        	throw new IndexOutOfBoundsException();
        }
    }
	
	public void writeByte(byte value) {
		if (length+1 > buffer.length) {
			throw new RuntimeException(MSG_OVER_FLOW);
		}
		buffer[writerIndex] = value;
		++length;
		++writerIndex;
		if (writerIndex >= buffer.length) {
			writerIndex = 0;
		}
	}
	
	public void setByte(byte value, int index) {
		buffer[index] = value;
	}
	
	public void writeShort(short value) {
		byte[] v = ConvertUtil.toBytesBigEndian(value);
		set(v, 0, v.length);
	}
	
	public void setShort(short value, int index) {
		byte[] v = ConvertUtil.toBytesBigEndian(value);
		setDirect(v, 0, v.length, index);
	}
	
	public void writeInt(int value) {
		byte[] v = ConvertUtil.toBytesBigEndian(value);
		set(v, 0, v.length);
	}
	
	public void setInt(int value, int index) {
		byte[] v = ConvertUtil.toBytesBigEndian(value);
		setDirect(v, 0, v.length, index);
	}
	
	public void writeLong(long value) {
		byte[] v = ConvertUtil.toBytesBigEndian(value);
		set(v, 0, v.length);
	}
	
	public void setLong(long value, int index) {
		byte[] v = ConvertUtil.toBytesBigEndian(value);
		setDirect(v, 0, v.length, index);
	}
	
	public void writeString(String value) {
		byte[] v = ConvertUtil.toBytes(value);
		if (length+2+v.length > buffer.length) {
			throw new RuntimeException(MSG_OVER_FLOW);
		}
		byte[] vlen = ConvertUtil.toBytesBigEndian((short)v.length);
		set(vlen, 0, vlen.length);
		set(v, 0, v.length);
	}
	
	public void setString(String value, int index) {
		byte[] v = ConvertUtil.toBytes(value);
		byte[] vlen = ConvertUtil.toBytesBigEndian((short)v.length);
		setDirect(vlen, 0, vlen.length, index);
		setDirect(v, 0, v.length, index);
	}
	
	public void writeBytes(byte[] bytes, int offset, int len) {
		checkBounds(offset, len, bytes.length);
		set(bytes, offset, len);
	}
	
	public void writeBytes(byte[] bytes) {
		set(bytes, 0, bytes.length);
	}
	
	private int readStream(byte[] buf, int off, int len, InputStream is) throws IOException {
		int readLen = 0;
		int curLen = 0;
		while ((readLen<len) && ((curLen=is.read(buf, off+readLen, len-readLen))>0)) {
			readLen += curLen;
		}
		return readLen;
	}
	
	public int slurp(InputStream is) throws IOException {
		int isLen = is.available();
		int readLen = 0;
		if (isLen > 0) {
			int expLen = availabe();
			if (expLen > isLen) {
				expLen = isLen;
			}
			if (writerIndex >= readerIndex) {
				int eLen = buffer.length-writerIndex;
				if (eLen < expLen) {
					readLen = readStream(buffer, writerIndex, eLen, is);
					if (readLen >= eLen) {
						length += readLen;
						writerIndex = 0;
						int readLen2 = readStream(buffer, 0, expLen-eLen ,is);
						if (readLen2 > 0) {
							readLen += readLen2;
							length += readLen2;
							writerIndex += readLen2;
						}
					}
					else {
						if (readLen > 0) {
							length += readLen;
							writerIndex += readLen;
						} 
					}
				}
				else {
					readLen = readStream(buffer, writerIndex, expLen, is);
					if (readLen > 0) {
						length += readLen;
						writerIndex += readLen;
					}
				}
			}
			else {
				readLen = readStream(buffer, writerIndex, expLen, is);
				if (readLen > 0) {
					length += readLen;
					writerIndex += readLen;
				}
			}
		}
		else {
			readLen = isLen;
		}
		return readLen;
	}
	
	private void setDirect(byte[] bytes, int offset, int len, int index) {
		int tl = buffer.length-index;
		if (tl >= len) {
			System.arraycopy(bytes, offset, buffer, index, len);
		}
		else {
			System.arraycopy(bytes, offset, buffer, index, tl);
			System.arraycopy(bytes, offset+tl, buffer, 0, len-tl);
		}
	}
	
	private void set(byte[] bytes, int offset, int len) {
		if (length+len > buffer.length) {
			throw new RuntimeException(MSG_OVER_FLOW);
		}
		if (writerIndex >= readerIndex) {
			int l1 = buffer.length-writerIndex;
			if (l1 >= len) {
				System.arraycopy(bytes, offset, buffer, writerIndex, len);
				writerIndex += len;
				if (writerIndex >= buffer.length) {
					writerIndex = 0;
				}
			}
			else {
				System.arraycopy(bytes, offset, buffer, writerIndex, l1);
				System.arraycopy(bytes, offset+l1, buffer, 0, len-l1);
				writerIndex = len-l1;
			}
		}
		else {
			System.arraycopy(bytes, offset, buffer, writerIndex, len);
			writerIndex += len;
		}
		length += len;
	}
	
	public byte readByte() {
		if (length < 1) {
			throw new RuntimeException(MSG_OVER_FLOW);
		}
		byte v = buffer[readerIndex];
		--length;
		++readerIndex;
		if (readerIndex >= buffer.length) {
			readerIndex = 0;
		}
		return v;
	}
	
	public byte getByte(int index) {
		return buffer[index];
	}
	
	public short readShort() {
		byte[] v = get(2);
		return ConvertUtil.toShortBigEndian(v);
	}
	
	public short getShort(int index) {
		byte[] v = getDirect(2, index);
		return ConvertUtil.toShortBigEndian(v);
	}
	
	public int readInt() {
		byte[] v = get(4);
		return ConvertUtil.toIntBigEndian(v);
	}
	
	public int getInt(int index) {
		byte[] v = getDirect(4, index);
		return ConvertUtil.toIntBigEndian(v);
	}
	
	public long readLong() {
		byte[] v = get(8);
		return ConvertUtil.toLongBigEndian(v);
	}
	
	public long getLong(int index) {
		byte[] v = getDirect(8, index);
		return ConvertUtil.toIntBigEndian(v);
	}
	
	public String readString() {
		if (length < 2) {
			throw new RuntimeException(MSG_OVER_FLOW);
		}
		short len = getShort(readerIndex);
		if (length < 2+len) {
			throw new RuntimeException(MSG_OVER_FLOW);
		}
		get(2);
		return ConvertUtil.toString(get(len));
	}
	
	public String getString(int index) {
		short len = getShort(index);
		return ConvertUtil.toString(getDirect(len, index+2));
	}
	
	public byte[] readBytes() {
		return get(length);
	}
	
	public byte[] readBytes(int len) {
		return get(len);
	}
	
	public void spit(OutputStream os) throws IOException {
		if (length > 0) {
			if (writerIndex >= readerIndex) {
				os.write(buffer, readerIndex, length);
			}
			else {
				os.write(buffer, writerIndex, buffer.length-readerIndex);
				os.write(buffer, 0, writerIndex);
			}
			length = 0;
			readerIndex = writerIndex;
		}
	}
	
	private byte[] get(int len) {
		if (length < len) {
			throw new RuntimeException(MSG_OVER_FLOW);
		}
		byte[] v = new byte[len];
		if (writerIndex > readerIndex) {
			System.arraycopy(buffer, readerIndex, v, 0, len);
			readerIndex += len;
		}
		else {
			int tl = buffer.length-readerIndex;
			if (tl >= len) {
				System.arraycopy(buffer, readerIndex, v, 0, len);
				readerIndex += len;
				if (readerIndex >= buffer.length) {
					readerIndex = 0;
				}
			}
			else {
				System.arraycopy(buffer, readerIndex, v, 0, tl);
				System.arraycopy(buffer, 0, v, tl, len-tl);
				readerIndex = len-tl;
			}
		}
		length -= len;
		return v;
	}
	
	private byte[] getDirect(int len, int index) {
		byte[] v = new byte[len];
		int tl = buffer.length-index;
		if (tl >= len) {
			System.arraycopy(buffer, index, v, 0, len);
		}
		else {
			System.arraycopy(buffer, index, v, 0, tl);
			System.arraycopy(buffer, 0, v, tl, len-tl);
		}
		return v;
	}
	
	public int getReaderIndex() {
		return readerIndex;
	}
	
	public int getWriterIndex() {
		return writerIndex;
	}
	
	public int capacity() {
		return buffer.length;
	}
	
	public int length() {
		return length;
	}
	
	public int availabe() {
		return buffer.length-length;
	}
	
	public void clear() {
		length = 0;
		readerIndex = 0;
		writerIndex = 0;
	}
}
