package cn.ohyeah.stb.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间处理工具类
 * @author maqian
 * @version 1.0
 */
public class DateUtil {
	
	/**
	 * 获取东8区的时区对象
	 * @return
	 */
	public static TimeZone getDefaultTimeZone() {
		return TimeZone.getTimeZone("GMT+08:00");
	}
	
	/**
	 * 获取时区为东8区的Calendar对象
	 * @return
	 */
	public static Calendar getDefaultCalendar() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(getDefaultTimeZone());
		return calendar;
	}
	
	/**
	 * 格式化时间字符串："yyyy/MM/dd HH:mm:ss"
	 * @param time
	 * @return
	 */
	public static String formatTimeStr(Date time) {
		Calendar calendar = getDefaultCalendar();
		calendar.setTime(time);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		
		String mstr = (month >= 10)?Integer.toString(month+1):("0"+month);
		String dstr = (day >= 10)?Integer.toString(day):("0"+day);
		String hstr = (hour >= 10)?Integer.toString(hour):("0"+hour);
		String minstr = (minute >= 10)?Integer.toString(minute):("0"+minute);
		String sstr = (second >= 10)?Integer.toString(second):("0"+second);
		return year+"/"+mstr+"/"+dstr+" "+hstr+":"+minstr+":"+sstr;
	}
	
	/**
	 * 判断是否同一年
	 * @param t1
	 * @param t2
	 * @return
	 */
	public static boolean isSameYear(java.util.Date t1, java.util.Date t2) {
		Calendar c = getDefaultCalendar();
		c.setTime(t1);
		int year = c.get(Calendar.YEAR);
		c.setTime(t2);
		if (c.get(Calendar.YEAR) == year) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断是否同一月
	 * @param t1
	 * @param t2
	 * @return
	 */
	public static boolean isSameMonth(java.util.Date t1, java.util.Date t2) {
		Calendar c = getDefaultCalendar();
		c.setTime(t1);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		c.setTime(t2);
		if (c.get(Calendar.YEAR) == year
				&& c.get(Calendar.MONTH) == month) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断是否同一天
	 * @param t1
	 * @param t2
	 * @return
	 */
	public static boolean isSameDay(java.util.Date t1, java.util.Date t2) {
		Calendar c = getDefaultCalendar();
		c.setTime(t1);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		c.setTime(t2);
		if (c.get(Calendar.YEAR) == year
				&& c.get(Calendar.MONTH) == month
				&& c.get(Calendar.DAY_OF_MONTH) == day) {
			return true;
		}
		return false;
	}
	
	/**
	 * 获得时间中的年份
	 * @param t
	 * @return
	 */
	public static int getYear(java.util.Date t) {
		Calendar c = getDefaultCalendar();
		c.setTime(t);
		return c.get(Calendar.YEAR);
	}
	
	/**
	 * 获得时间中的月份(从1开始计数)
	 * @param t
	 * @return
	 */
	public static int getMonth(java.util.Date t) {
		Calendar c = getDefaultCalendar();
		c.setTime(t);
		return c.get(Calendar.MONTH)+1;
	}
	
	/**
	 * 获得时间中的日期(从1开始计数)
	 * @param t
	 * @return
	 */
	public static int getDay(java.util.Date t) {
		Calendar c = getDefaultCalendar();
		c.setTime(t);
		return c.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 时间加n年
	 * @param c
	 * @param n
	 * @return
	 */
	public static Calendar addYear(Calendar c, int n) {
		c.set(Calendar.YEAR, c.get(Calendar.YEAR)+n);
		return c;
	} 
	
	
	public static java.util.Date addYear(java.util.Date date, int n) {
		Calendar c = getDefaultCalendar();
		c.setTime(date);
		return addYear(c, n).getTime();
	}
	
	/**
	 * 时间加n个月
	 * @param c
	 * @param n
	 * @return
	 */
	public static Calendar addMonth(Calendar c, int n) {
		int month = c.get(Calendar.MONTH);
		int incY = (month+n)/12;
		int m = (month+n)%12;
		c.set(Calendar.YEAR, c.get(Calendar.YEAR)+incY);
		c.set(Calendar.MONTH, m);
		return c;
	}
	
	public static java.util.Date addMonth(java.util.Date date, int n) {
		Calendar c = getDefaultCalendar();
		c.setTime(date);
		return addMonth(c, n).getTime();
	}
	
	public static java.util.Date createTime(int year, int month, int day) {
		Calendar c = getDefaultCalendar();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month-1);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	
	public static java.util.Date createTime(int year, int month, int day, int hour, int minute, int second) {
		Calendar c = getDefaultCalendar();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month-1);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, second);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	
	/**
	 * 判断date1是否在date2之前
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean before(java.util.Date date1, java.util.Date date2) {
		return date1.getTime()<date2.getTime();
	}
	
	/**
	 * 判断date1是否在date2之后
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean after(java.util.Date date1, java.util.Date date2) {
		return date1.getTime()>date2.getTime();
	}
	
	/**
	 * 判断date是否在date1和date2之间
	 * @param date
	 * @param date1
	 * @param date1
	 * @return
	 */
	public static boolean between(java.util.Date date, java.util.Date date1, java.util.Date date2) {
		return date.getTime()>=date1.getTime()&&date.getTime()<=date2.getTime();
	}
	
	/**
	 * 设置时间到月初
	 * @param c
	 * @return
	 */
	public static Calendar setBeginningOfMonth(Calendar c) {
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c;
	}
	
	/**
	 * 设置时间到月末
	 * @param c
	 * @return
	 */
	public static Calendar setEndOfMonth(Calendar c) {
		addMonth(c, 1);
		setBeginningOfMonth(c);
		long millis = c.getTime().getTime();
		c.setTime(new java.util.Date(millis-1));
		return c;
	}
	
	/**
	 * 获得月初的时间
	 * @param t
	 * @return
	 */
	public static java.util.Date getMonthStartTime(java.util.Date t) {
		Calendar c = getDefaultCalendar();
		c.setTime(t);
		setBeginningOfMonth(c);
		return c.getTime();
	}
	
	/**
	 * 获得月末的时间
	 * @param t
	 * @return
	 */
	public static java.util.Date getMonthEndTime(java.util.Date t) {
		Calendar c = getDefaultCalendar();
		c.setTime(t);
		setEndOfMonth(c);
		return c.getTime();
	}
	
	/**
	 * 获得下个月月初的时间
	 * @param t
	 * @return
	 */
	public static java.util.Date getNextMonthStartTime(java.util.Date t) {
		Calendar c = getDefaultCalendar();
		c.setTime(t);
		addMonth(c, 1);
		setBeginningOfMonth(c);
		return c.getTime();
	}
}
