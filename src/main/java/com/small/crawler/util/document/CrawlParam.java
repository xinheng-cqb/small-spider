package com.small.crawler.util.document;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.small.crawler.constants.DocumentUtilConstants;

/**
 * @author caiqibin
 * @date 2017年6月13日
 * @introduce:Http工具类参数对象
 */
public class CrawlParam {
	private final static Random RANDOM = new Random();

	// 访问链接，必须有
	private String urlStr;
	// 间隔时间 单位是毫秒 默认0 控制爬虫访问频率的(单位ms)
	private int interval;
	// 实际的随机时间值
	private int actualRangeValue;
	// 随机时间范围 默认0(单位ms)
	private int intervalRange;
	// 尝试获取次数，默认为1次
	private int tryCount = 1;
	// 请求方式 默认是get请求
	private String requestMethod = DocumentUtilConstants.GET_METHOD;
	// post请求参数，get请求时为空，post请求时不能为空
	private String postParam;
	// cookie值，默认是null
	private String cookie;
	// 编码方式 默认是utf-8
	private String charset = DocumentUtilConstants.UTF_UNICODE;
	// 是否使用代理 默认是false
	private boolean useProxy = false;
	// 是否使用GZip解析获取的数据流，默认是false
	private boolean useGZip = false;
	// 访问网址保存路径 默认null，不为空时最好填写绝对路径
	private String outputPath;
	// 请求头信息（通常不用设置）
	private Map<String, String> requestHeadMap;

	public CrawlParam() {

	}

	public CrawlParam(String urlStr) {
		this.urlStr = urlStr;
	}

	public String getUrlStr() {
		return urlStr;
	}

	public CrawlParam setUrlStr(String urlStr) {
		this.urlStr = urlStr;
		if (this.intervalRange > 0) {
			this.actualRangeValue = RANDOM.nextInt(intervalRange) + 1;
		}
		return this;
	}

	public int getInterval() {
		return interval;
	}

	public CrawlParam setInterval(int interval) {
		this.interval = interval;
		return this;
	}

	public int getActualRangeValue() {
		return actualRangeValue;
	}

	public int getIntervalRange() {
		return intervalRange;
	}

	public CrawlParam setIntervalRange(int intervalRange) {
		this.intervalRange = intervalRange;
		this.actualRangeValue = RANDOM.nextInt(intervalRange) + 1;
		return this;
	}

	public int getTryCount() {
		return tryCount;
	}

	public void setTryCount(int tryCount) {
		this.tryCount = tryCount;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public CrawlParam setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
		return this;
	}

	public String getPostParam() {
		return postParam;
	}

	public CrawlParam setPostParam(String postParam) {
		this.postParam = postParam;
		return this;
	}

	public String getCookie() {
		return cookie;
	}

	public CrawlParam setCookie(String cookie) {
		this.cookie = cookie;
		return this;
	}

	public String getCharset() {
		return charset;
	}

	public CrawlParam setCharset(String charset) {
		this.charset = charset;
		return this;
	}

	public boolean isUseProxy() {
		return useProxy;
	}

	public CrawlParam setUseProxy(boolean useProxy) {
		this.useProxy = useProxy;
		return this;
	}

	public boolean isUseGZip() {
		return useGZip;
	}

	public CrawlParam setUseGZip(boolean useGZip) {
		this.useGZip = useGZip;
		return this;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public CrawlParam setOutputPath(String outputPath) {
		this.outputPath = outputPath;
		return this;
	}

	public Map<String, String> getRequestHeadMap() {
		return requestHeadMap;
	}

	public CrawlParam setRequestHeadInfo(String key, String value) {
		if (this.requestHeadMap == null) {
			this.requestHeadMap = new HashMap<String, String>();
		}
		this.requestHeadMap.put(key, value);
		return this;
	}

	public CrawlParam setRequestHeadInfo(Map<String, String> infoMap) {
		if (this.requestHeadMap == null) {
			this.requestHeadMap = infoMap;
		} else {
			this.requestHeadMap.putAll(infoMap);
		}
		return this;
	}

}
