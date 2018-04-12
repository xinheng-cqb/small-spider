package com.small.crawler.util;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * @author caiqibin
 * @date 2017年7月4日
 * @introduce:与时间日期相关的工具类
 */
public class DateTimeUtil {

	// 测试
	public static void main(String[] args) {
		System.out.println(getBeforeTime("yyyy-MM-dd", 1, 3, 1));
	}

	/**
	 * 将String型格式化,比如想要将2011-11-11格式化成2011年11月11日,就StringPattern("2011-11-11","yyyy-MM-dd",
	 * "yyyy年MM月dd日").
	 * 
	 * @param date String 想要格式化的日期
	 * @param oldPattern String 想要格式化的日期的现有格式
	 * @param newPattern String 想要格式化成什么格式
	 * @return String
	 */
	public static String StringPattern(String date, String oldPattern, String newPattern) {
		if (date == null || oldPattern == null || newPattern == null)
			return "";
		SimpleDateFormat sdf1 = new SimpleDateFormat(oldPattern); // 实例化模板对象
		SimpleDateFormat sdf2 = new SimpleDateFormat(newPattern); // 实例化模板对象
		Date d = null;
		try {
			d = sdf1.parse(date); // 将给定的字符串中的日期提取出来
		} catch (Exception e) { // 如果提供的字符串格式有错误，则进行异常处理
			e.printStackTrace(); // 打印异常信息
		}
		return sdf2.format(d);
	}

	/**
	 * @introduce:根据日期格式和偏移量获取相应日期的字符串
	 * @param formatStr 需要的日期格式 实例：yyyy-MM-dd 、yyyy-MM-dd HH:mm:ss
	 * @param offset 日期偏移量 实例：0代表今天，1代表明天，-1代表昨天 以此类推
	 * @return String
	 */
	public static String getTime(String formatStr, int offset) {
		LocalDateTime nowDate = LocalDateTime.now().plusDays(offset);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatStr);
		return nowDate.format(formatter);
	}

	/**
	 * @introduce:将毫秒数转换为格式日期
	 * @param millis 毫秒数
	 * @param formatStr 需要的日期格式 实例：yyyy-MM-dd 、yyyy-MM-dd HH:mm:ss
	 * @return String
	 */
	public static String parseMillis2Time(Long millis, String formatStr) {
		Instant instant = Instant.ofEpochSecond(millis / 1000);
		ZoneId zone = ZoneId.systemDefault();
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatStr);
		return localDateTime.format(formatter);
	}

	/**
	 * @introduce:根据年龄 获取相应的日期（基于当前时间）
	 * @param age 年龄偏移量
	 * @param format 样例:"yyyyMMdd" "yyyy-MM-dd"
	 * @return String
	 */
	public static String getBeforeTime(String format, int age) {
		return getBeforeTime(format, age, 0, 0);
	}

	/**
	 * @introduce: 根据年龄、月份、天数 获取相应的日期（基于当前时间）
	 * @param format 样例:"yyyyMMdd" "yyyy-MM-dd"
	 * @param age 年份偏移量
	 * @param month 月份偏移量
	 * @param day 天数偏移量
	 * @return String
	 */
	public static String getBeforeTime(String format, int age, int month, int day) {
		DateFormat df = new SimpleDateFormat(format);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -age);
		calendar.add(Calendar.MONTH, -month);
		calendar.add(Calendar.DAY_OF_MONTH, -day);
		String time = df.format(calendar.getTime());
		return time;
	}

	/**
	 * @introduce:获取指定字符串(不含小时信息)对应日期的毫秒数
	 * @param time
	 * @param formatStr 不指定时是 yyyy-MM-dd 形式
	 * @return Long
	 */
	public static Long parseStr2Millis(String time, String formatStr) {
		if (formatStr == null || "".equals(formatStr)) {
			formatStr = "yyyy-MM-dd";
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatStr);
		LocalDate originDate = LocalDate.parse(time, formatter);
		return originDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}

	/**
	 * @introduce:获取指定字符串(要求包括小时信息)对应日期的毫秒数 , 且第一个参数和第一个参数要求对应
	 * @param time
	 * @param formatStr 必须指定，至少精确到小时
	 * @return Long
	 */
	public static Long parseStr2MillisWithTime(String time, String formatStr) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatStr);
		LocalDateTime originDate = LocalDateTime.parse(time, formatter);
		return originDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}

	/**
	 * @introduce: 根据传进去的日期获取对应所在的周日期范围
	 * @param timeStr
	 * @return String
	 */
	public static String getWeekByDate(String timeStr) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式

		Calendar cal = Calendar.getInstance();
		try {
			Date time = sdf.parse(timeStr);
			cal.setTime(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		// System.out.println("要计算日期为:" + sdf.format(cal.getTime())); // 输出要计算日期
		cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		String imptimeBegin = sdf.format(cal.getTime());
		cal.add(Calendar.DATE, 6);
		String imptimeEnd = sdf.format(cal.getTime());
		return MessageFormat.format("{0}至{1}", imptimeBegin, imptimeEnd);
	}
}
