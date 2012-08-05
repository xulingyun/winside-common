package cn.ohyeah.stb.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * IO工具
 * @author maqian
 * @version 1.0
 */
public class IOUtil {
	
	/**
	 * 从InputStream读取所有byte，返回byte数组
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static byte[] readAllBytesOnly(InputStream is) throws IOException 
	{
		int len = is.available();
		return readBytes(is, len);
	}
	
	/**
	 * 向OutputStream写入byte数组的所有byte
	 * @param os
	 * @param bytes
	 * @throws IOException
	 */
	public static void writeAllBytesOnly(OutputStream os, byte[] bytes) throws IOException {
		os.write(bytes, 0, bytes.length);
	}
	
	/**
	 * 向OutputStream中写入byte数组，包含数组长度
	 * @param os
	 * @param bytes
	 * @throws IOException
	 */
	public static void writeByteArray(OutputStream os, byte[] bytes) throws IOException {
		if (bytes != null) {
			writeInt(os, bytes.length);
			if (bytes.length > 0) {
				writeAllBytesOnly(os, bytes);
			}
		}
		else {
			writeInt(os, -1);
		}
	}
	
	/**
	 * 从InputStream中读取byte数组，包含数组长度
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static byte[] readByteArray(InputStream is) throws IOException {
		int len = readInt(is);
		return readBytes(is, len);
	}
	
	/**
	 * 从InputStream中读取len长度的byte数组
	 * @param is
	 * @param len
	 * @return
	 * @throws IOException
	 */
	public static byte[] readBytes(InputStream is, int len) throws IOException
    {
        byte[] v = null;
        if (len >= 0) {
            v = new byte[len];
            int curRead = 0;
            int totalRead = 0;
            while (totalRead < len) {
                curRead = is.read(v, totalRead, v.length - totalRead);
                if (curRead >= 0) {
                    totalRead += curRead;
                }
                else {
                	throw new IOException("数据长度不够");
                }
            }
        }
        return v;
    }
	
	/**
	 * 想OutputStream中写入byte数组的指定部分
	 * @param os
	 * @param bytes
	 * @param offset
	 * @param len
	 * @throws IOException
	 */
	public static void writeBytes(OutputStream os, byte[] bytes, int offset, int len) throws IOException {
		os.write(bytes, offset, len);
	}
	
	/**
	 * 按照大端字节序读取short
	 * @param is
	 * @return
	 * @throws IOException
	 */
    public static short readShort(InputStream is) throws IOException
    {
        byte[] v = readBytes(is, 2);
        return ConvertUtil.toShortBigEndian(v);
    }
    
    /**
     * 按照大端字节序读取short数组
     * @param is
     * @return
     * @throws IOException
     */
    public static short[] readShortArray(InputStream is) throws IOException
    {
    	short len = readShort(is);
    	short[] value = null;
    	if (len >= 0) {
	    	value = new short[len];
	    	for (int i = 0; i < len; ++i) {
	    		value[i] = readShort(is);
	    	}
    	}
    	return value;
    }
    
    /**
     * 按照大端字节序写入short
     * @param os
     * @param value
     * @throws IOException
     */
    public static void writeShort(OutputStream os, short value) throws IOException
    {
    	byte[] v = ConvertUtil.toBytesBigEndian(value);
    	os.write(v, 0, v.length);
    }
    
    /**
     * 按照大端字节序写入short数组
     * @param os
     * @param value
     * @throws IOException
     */
    public static void writeShortArray(OutputStream os, short[] value) throws IOException
    {
    	if (value != null) {
    		writeShort(os, (short)value.length);
    		for (int i = 0; i < value.length; ++i) {
    			writeShort(os, value[i]);
    		}
    	}
    	else {
    		writeShort(os, (short)-1);
    	}
    }

    /**
     * 按照大端字节序读取int
     * @param is
     * @return
     * @throws IOException
     */
    public static int readInt(InputStream is) throws IOException
    {
        byte[] v = readBytes(is, 4);
        return ConvertUtil.toIntBigEndian(v);
    }
    
    /**
     * 按照大端字节序读取int数组
     * @param is
     * @return
     * @throws IOException
     */
    public static int[] readIntArray(InputStream is) throws IOException
    {
    	short len = readShort(is);
    	int[] value = null;
    	if (len >= 0) {
	    	value = new int[len];
	    	for (int i = 0; i < len; ++i) {
	    		value[i] = readInt(is);
	    	}
    	}
    	return value;
    }
    
    /**
     * 按照大端字节序写入int
     * @param os
     * @param value
     * @throws IOException
     */
    public static void writeInt(OutputStream os, int value) throws IOException
    {
    	byte[] v = ConvertUtil.toBytesBigEndian(value);
    	os.write(v, 0, v.length);
    }
    
    /**
     * 按照大端字节序写入int数组
     * @param os
     * @param value
     * @throws IOException
     */
    public static void writeIntArray(OutputStream os, int[] value) throws IOException
    {
    	if (value != null) {
    		writeShort(os, (short)value.length);
    		for (int i = 0; i < value.length; ++i) {
    			writeInt(os, value[i]);
    		}
    	}
    	else {
    		writeShort(os, (short)-1);
    	}
    }
    
    /**
     * 按照大端字节序读取long
     * @param is
     * @return
     * @throws IOException
     */
    public static final long readLong(InputStream is) throws IOException {
    	byte[] v = readBytes(is, 8);
        return ConvertUtil.toLongBigEndian(v);
    }
    
    /**
     * 按照大端字节序写入long
     * @param os
     * @param value
     * @throws IOException
     */
    public static final void writeLong(OutputStream os, long value) throws IOException {
    	byte[] v = ConvertUtil.toBytesBigEndian(value);
    	os.write(v, 0, v.length);
    }
    
    /**
     * 写入float
     * @param os
     * @param value
     * @throws IOException
     */
    public static final void writeFloat(OutputStream os, float value) throws IOException {
	    writeInt(os, Float.floatToIntBits(value));
	}
    
    /**
     * 读取float
     * @param is
     * @return
     * @throws IOException
     */
    public static final float readFloat(InputStream is) throws IOException {
    	return Float.intBitsToFloat(readInt(is));
    }
	
    /**
     * 写入double
     * @param os
     * @param value
     * @throws IOException
     */
	public static final void writeDouble(OutputStream os, double value) throws IOException {
	    writeLong(os, Double.doubleToLongBits(value));
	}
	
	/**
     * 读取double
     * @param is
     * @return
     * @throws IOException
     */
	public static final double readDouble(InputStream is) throws IOException {
		return Double.longBitsToDouble(readLong(is));
	}

    
    /**
     * 按照encoding编码读取string
     * @param is
     * @param encoding
     * @return
     * @throws IOException
     */
    public static String readString(InputStream is, String encoding) throws IOException
    {
    	short len = readShort(is);
    	String value = null;
    	if (len >= 0) {
    		if (len > 0) {
    			value = ConvertUtil.toString(readBytes(is, len), encoding);
    		}
    		else {
    			value = "";
    		}
    	}
        return value;
    }

    /**
     * 按照GB2312编码读取string
     * @param is
     * @return
     * @throws IOException
     */
    public static String readString(InputStream is) throws IOException
    {
    	return readString(is, "GB2312");
    }
    
    /**
     * 按照GB2312编码写入string
     * @param os
     * @param value
     * @throws IOException
     */
    public static void writeString(OutputStream os, String value) throws IOException
    {
    	writeString(os, value, "GB2312");
    }
    
    /**
     * 按照encoding编码写入string
     * @param os
     * @param value
     * @param encoding
     * @throws IOException
     */
    public static void writeString(OutputStream os, String value, String encoding) throws IOException
    {
    	if (value != null) {
    		if (value.length() > 0) {
		    	byte[] v = ConvertUtil.toBytes(value, encoding);
		    	writeShort(os, (short)(v.length));
		    	writeAllBytesOnly(os, v);
    		}
    		else {
    			writeShort(os, (short)0);
    		}
    	}
    	else {
    		writeShort(os, (short)-1);
    	}
    }
    
    /**
     * 按照GB2312编码读取string数组
     * @param is
     * @return
     * @throws IOException
     */
    public static String[] readStringArray(InputStream is) throws IOException {
    	return readStringArray(is, "GB2312");
    }
    
    /**
     * 按照encoding编码读取string数组
     * @param is
     * @param encoding
     * @return
     * @throws IOException
     */
    public static String[] readStringArray(InputStream is, String encoding) throws IOException {
    	short len = readShort(is);
    	String[] value = null;
    	if (len >= 0) {
    		value = new String[len];
    		for (int i = 0; i < len; ++i) {
    			value[i] = readString(is, encoding);
    		}
    	}
    	return value;
    }
    
    /**
     * 按照GB2312编码写入string数组
     * @param os
     * @param value
     * @throws IOException
     */
    public static void writeStringArray(OutputStream os, String[] value) throws IOException
    {
    	writeStringArray(os, value, "GB2312");
    }
    
    /**
     * 按照encoding编码写入string数组
     * @param os
     * @param value
     * @param encoding
     * @throws IOException
     */
    public static void writeStringArray(OutputStream os, String[] value, String encoding) throws IOException
    {
    	if (value != null) {
    		writeShort(os, (short)value.length);
    		for (int i = 0; i < value.length; ++i) {
    			writeString(os, value[i], encoding);
    		}
    	}
    	else {
    		writeShort(os, (short)-1);
    	}
    }
}
