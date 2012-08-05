package cn.ohyeah.stb.util;

import java.util.Random;

/**
 * 随机数工具
 * @author maqian
 * @version 1.0
 */
public class RandomValue {
	private static Random random = new Random();
	
	/**
	 * 返回0<=result<range的随机数整数
	 * @param range
	 * @return
	 */
	public static int getRandInt(int range) {
		return random.nextInt(range);
	}
	
	/**
	 * 返回start<=result<end的随机整数
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getRandInt(int start, int end) {
		return random.nextInt(end-start)+start;
	}
}
