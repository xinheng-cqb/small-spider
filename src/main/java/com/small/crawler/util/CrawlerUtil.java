package com.small.crawler.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author caiqibin
 * @date 2017年5月19日
 * @introduce:爬虫常用工具类
 */
public class CrawlerUtil {
	public static void main(String[] args) {
		String s = " \\u003Cspan\\u003E\\/ 31\\u9875\\u003C\\/span\\u003E\\n  ".replace(" ", "");
		System.out.println(s);
		System.out.println(matchNumber(s, "003E\\/"));
	}

	/**
	 * @introduce: 对文本进行URL编码，
	 * @param text
	 * @param charset
	 * @return String
	 */
	public static String urlEncode(String text, String charset) {
		try {
			return URLEncoder.encode(text, charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @introduce: 用默认的utf-8编码对文件进行URL编码
	 * @param text
	 * @return String
	 */
	public static String urlEncode(String text) {
		return urlEncode(text, "utf-8");
	}

	/**
	 * @introduce: 对文本每隔n位插入一个字符，从后到前插入
	 * @param text （ 原文本）
	 * @param n 间隔位
	 * @param chara 字符
	 * @return String
	 */
	public static String divideText(String text, int n, String charStr) {
		int consult = text.length() / n;
		int remainder = text.length() % n;
		if (consult == 0) {
			return text;
		}
		StringBuilder sb = new StringBuilder();
		if (remainder != 0) {
			sb.append(text.subSequence(0, remainder)).append(charStr);
		}
		for (int i = 0; i < consult; i++) {
			sb.append(text.substring(remainder + (i * n), remainder + ((i + 1) * n))).append(charStr);
		}
		sb.setLength(sb.length() - charStr.length());
		return sb.toString();
	}

	/**
	 * @introduce:对数字根据指定小数位进行四舍五入
	 * @param num
	 * @param decimalPlace
	 * @return double
	 */
	public static double formatNumber(double num, int decimalPlace) {
		BigDecimal b = new BigDecimal(num);
		double result = b.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP).doubleValue();
		return result;
	}

	/**
	 * @introduce:匹配数字(包括小数 ,负数)
	 * @param text
	 * @param charactor
	 * @return String
	 */
	public static String matchNumber(String text, String charactor) {
		Pattern pattern = Pattern.compile(charactor + "([-\\d.]+)");
		Matcher match = pattern.matcher(text);
		if (match.find()) {
			return match.group(1);
		}
		return null;
	}

	/**
	 * @introduce:匹配中文
	 * @param info
	 * @param allMatch 是否匹配全部中文
	 * @param splitSymbol allMatch为true时，各个中文片段的分隔符 ，为false不起作用
	 * @return String
	 */
	public static String matchChinese(String info, boolean allMatch, String splitSymbol) {
		Pattern pattern = Pattern.compile("[\u4E00-\u9FA5]+");
		Matcher match = pattern.matcher(info);
		if (!allMatch) {
			if (match.find()) {
				return match.group();
			}
		} else if (allMatch) {
			StringBuilder sb = new StringBuilder();
			while (match.find()) {
				sb.append(match.group()).append(splitSymbol);
			}
			if (sb.length() > 0) {
				sb.setLength(sb.length() - splitSymbol.length());
				return sb.toString();
			}
		}
		return null;
	}

	/**
	 * @introduce: 匹配符号间的内容，可以试{}、""、()、[]等等
	 * @param text 原文本
	 * @param prefix 符号的开始部分比如{ 、[等 ，要保证唯一。
	 *            （小技巧：如果不能保证文本中只有一个要匹配的符号，可以在开头部分前面添加一些匹配的文本来保证唯一，示例：info:[）
	 * @param suffix 符号的结束部分}、]等
	 * @return String
	 */
	public static String matchBetweenSymbol(String text, String prefix, String suffix) {
		Pattern pattern = Pattern.compile(prefix + "([^" + suffix + "]+)");
		Matcher match = pattern.matcher(text);
		if (match.find()) {
			return match.group(1);
		}
		return null;
	}

	/**
	 * @introduce:获取resources目录下的资源文件路径
	 * @param filePath 文件在resources目录下的路径(示例：application.properties or tempDir/temp.txt)
	 * @return String
	 */
	public static String getResourcePath(String filePath) {
		URL url = CrawlerUtil.class.getClassLoader().getResource(filePath);
		if (url != null) {
			return url.getPath().substring(1);
		}
		throw new NullPointerException("filePath 位置不存在文件");
	}

}
