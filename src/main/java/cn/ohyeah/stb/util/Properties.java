package cn.ohyeah.stb.util;

import java.util.Enumeration;
import java.util.Hashtable;

import cn.ohyeah.stb.res.ResourceManager;

/**
 * 属性类，方便操作键值对属性文件<br/>
 * 键值对形式：key=value
 * @author maqian
 * @version 1.0
 */
public class Properties {
	private Hashtable props = new Hashtable();
	public Properties() {
	}
	
	/**
	 * 获取指定键key的值
	 * @param key
	 * @return
	 */
	public String get(String key) {
		return (String)props.get(key);
	}
	
	/**
	 * 移除指定键key的值
	 * @param key
	 * @return
	 */
	public String remove(String key) {
		return (String)props.remove(key);
	}
	
	/**
	 * 添加(key,value)键值对
	 * @param key
	 * @param value
	 */
	public void put(String key, String value) {
		props.put(key, value);
	}
	
	/**
	 * 判读是否包含键key
	 * @param key
	 * @return
	 */
	public boolean containsKey(String key) {
		return props.containsKey(key);
	}

	/**
	 * 加载property文件
	 * @param filePath
	 * @param charset
	 */
	public void parseFile(String filePath, String charset) {
		parseData(ResourceManager.loadString(filePath, charset));
	}
	
	/**
	 * 加载property字符串
	 * @param data
	 */
	public void parseData(String data) {
		int scanPos = 0;
		int searchPos = 0;
		int dataLen = data.length();
		String key = null;
		String value = null;
		while (scanPos < dataLen) {
			while (data.charAt(scanPos) == '#') {
				searchPos = data.indexOf('\n');
				if (searchPos >= 0) {
					scanPos = searchPos+1;
				}
				else {
					break;
				}
			}
			searchPos = data.indexOf('=', scanPos);
			key = data.substring(scanPos, searchPos).trim();
			scanPos = searchPos+1;
			searchPos = data.indexOf('\n', scanPos);
			if (searchPos > 0) {
				if (data.charAt(searchPos-1) == '\r') {
					value = data.substring(scanPos, searchPos-1).trim();
				}
				else {
					value = data.substring(scanPos, searchPos).trim();
				}
				scanPos = searchPos+1;
			}
			else {
				value = data.substring(scanPos).trim();
				scanPos = dataLen;
			}
			if (key!=null && !"".equals(key)) {
				props.put(key, value);
			}
		}
	}
	
	/**
	 * 将properties的键值对打印到标准输出流
	 */
	public void print() {
		Enumeration e = props.keys();
		while (e.hasMoreElements()) {
			String key = (String)e.nextElement();
			System.out.println(key+" ==> "+props.get(key));
		}
	}
}
