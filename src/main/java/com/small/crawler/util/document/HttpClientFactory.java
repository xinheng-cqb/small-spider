package com.small.crawler.util.document;

import java.io.IOException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author caiqibin
 * @date 2017年6月16日
 * @introduce:通过HttpClient 获取网页
 */
public class HttpClientFactory {
	private static final Log LOGGER = LogFactory.getLog(HttpClientFactory.class);
	private static final HttpClient hc;
	static {
		hc = new HttpClient();
		hc.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		HttpClientParams httparams = new HttpClientParams();
		hc.setParams(httparams);
		hc.getHttpConnectionManager().getParams().setConnectionTimeout(200000);
		hc.getParams().setParameter(HttpMethodParams.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:53.0) Gecko/20100101 Firefox/53.0");// 设置信息
	}

	/**
	 * @introduce:
	 * @param url
	 * @param paramMap
	 * @return String
	 */
	public static String getDocumentStr(CrawlParam crawlParam, Proxy proxy) {
		String responseBody = null;
		GetMethod getMethod = new GetMethod(crawlParam.getUrlStr());
		try {
			getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler()); // 使用系统提供的默认的恢复策略
			if (crawlParam.getRequestHeadMap() != null) {
				NameValuePair[] data = new NameValuePair[crawlParam.getRequestHeadMap().size()];
				int index = 0;
				for (Entry<String, String> entry : crawlParam.getRequestHeadMap().entrySet()) {
					data[index++] = new NameValuePair(entry.getKey(), entry.getValue()); // 获取请求参数
				}
				getMethod.setQueryString(data); // 设置请求参数
			}

			if (crawlParam.getCookie() != null) {
				getMethod.addRequestHeader("Cookie", crawlParam.getCookie());
			}
			HostConfiguration hconfig = new HostConfiguration();
			hconfig.setProxy("139.129.94.241", 3128);
			hc.setHostConfiguration(hconfig);
			int statusCode = hc.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				LOGGER.info("=====get document failure ,error code is " + statusCode + " , request url is " + crawlParam.getUrlStr());
				return responseBody;
			}
			responseBody = getMethod.getResponseBodyAsString();
		} catch (Exception e) {
			LOGGER.error("===get document error,request url is  " + crawlParam.getUrlStr(), e);
		} finally {
			getMethod.releaseConnection(); // 释放链接
		}
		return responseBody;
	}

	/**
	 * @param url
	 * @param para Post请求中携带的参数
	 * @return
	 * @throws IOException
	 */
	public static String post(String url, Map<String, String> para) throws IOException {
		String responseBody = null;
		PostMethod postMethod = new PostMethod(url);
		NameValuePair[] data = new NameValuePair[para.size()];
		int index = 0;
		for (String s : para.keySet()) {
			data[index++] = new NameValuePair(s, para.get(s));
		}
		postMethod.setRequestBody(data); // 设置请求参数
		try {
			HostConfiguration hconfig = new HostConfiguration();
			hconfig.setProxy("139.129.94.241", 3128);
			hc.setHostConfiguration(hconfig);
			int statusCode = hc.executeMethod(postMethod);
			if (statusCode != HttpStatus.SC_OK) {
				LOGGER.error("Method failed: " + postMethod.getStatusLine());
			}
			responseBody = postMethod.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("发生致命的异常，可能是网络连接有问题" + e.getMessage());
		} finally {
			postMethod.releaseConnection(); // 释放链接
		}
		return responseBody;
	}

	/**
	 * @param url
	 * @param para
	 * @param cookie
	 * @return
	 * @throws IOException
	 */
	public static String get(String url, Map<String, String> para, String cookie) throws IOException {
		String responseBody = null;
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler()); // 使用系统提供的默认的恢复策略
		NameValuePair[] data = new NameValuePair[para.size()];
		int index = 0;
		for (String s : para.keySet()) {
			data[index++] = new NameValuePair(s, para.get(s)); // 获取请求参数
		}
		getMethod.setQueryString(data); // 设置请求参数
		if (!cookie.equals("")) {
			getMethod.setRequestHeader("cookie", cookie);
		}
		try {
			int statusCode = hc.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				LOGGER.error("Method failed: " + getMethod.getStatusLine());
			}
			responseBody = getMethod.getResponseBodyAsString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			LOGGER.error("发生致命的异常，可能是网络连接有问题" + e.getMessage());
		} catch (HttpException e) { // 发生致命的异常
			e.printStackTrace();
			LOGGER.error("发生致命的异常" + e.getMessage());
		} catch (IOException e) { // 发生网络异常
			e.printStackTrace();
			LOGGER.error("发生网络异常" + e.getMessage());
		} finally {
			getMethod.releaseConnection(); // 释放链接
		}
		return responseBody;
	}

	/**
	 * @param url
	 * @param para
	 * @param cookie
	 * @return
	 * @throws IOException
	 */
	public static String get(String url, String cookie) throws IOException {
		String responseBody = null;
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler()); // 使用系统提供的默认的恢复策略
		if (!cookie.equals("")) {
			getMethod.setRequestHeader("cookie", cookie);
		}
		try {
			int statusCode = hc.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				LOGGER.error("Method failed: " + getMethod.getStatusLine());
			}
			responseBody = getMethod.getResponseBodyAsString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			LOGGER.error("发生致命的异常，可能是网络连接有问题" + e.getMessage());
		} catch (HttpException e) { // 发生致命的异常
			e.printStackTrace();
			LOGGER.error("发生致命的异常" + e.getMessage());
		} catch (IOException e) { // 发生网络异常
			e.printStackTrace();
			LOGGER.error("发生网络异常" + e.getMessage());
		} finally {
			getMethod.releaseConnection(); // 释放链接
		}
		return responseBody;
	}

	/**
	 * @param url
	 * @param headers
	 * @return
	 * @throws IOException
	 */
	public static String get(String url, Header[] headers) throws IOException {
		String responseBody = null;
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler()); // 使用系统提供的默认的恢复策略
		for (Header h : headers) {
			getMethod.addRequestHeader(h);
		}
		try {
			int statusCode = hc.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				LOGGER.error("Method failed: " + getMethod.getStatusLine());
			}
			responseBody = getMethod.getResponseBodyAsString();
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			LOGGER.error("发生网络超时异常，可能是网络连接有问题" + e.getMessage());
		} catch (UnknownHostException e) {
			e.printStackTrace();
			LOGGER.error("发生致命的异常，可能是网络连接有问题" + e.getMessage());
		} catch (HttpException e) { // 发生致命的异常
			e.printStackTrace();
			LOGGER.error("发生致命的异常" + e.getMessage());
		} catch (IOException e) { // 发生网络异常
			e.printStackTrace();
			LOGGER.error("发生网络异常" + e.getMessage());
		} finally {
			getMethod.releaseConnection(); // 释放链接
		}
		return responseBody;
	}

	/*public static void main(String[] args) {
		Map<String, String> infoMap = new HashMap<String, String>();
		try {
			infoMap.put("areaCode", "330100");
			infoMap.put("carOwner", "傅双方");
			infoMap.put("channelNo", "S000000");
			infoMap.put("companyCode", "epicc");
			infoMap.put("districtCode", "");
			infoMap.put("plateNo", "云CK9070");
			infoMap.put("token", "shell_token_93c675fcb31a4f569a23d9ce445cfe6d_S000000");
			String result = post("https://s-backend.wxb.com.cn/premium/postBaseInfo", infoMap);
			System.out.println(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/

	/**
	 * @param url
	 * @param para
	 * @param cookie
	 * @return
	 * @throws IOException
	 */
	public static String post(String url, Map<String, String> para, String cookie) throws IOException {
		String responseBody = null;
		PostMethod postMethod = new PostMethod(url);
		NameValuePair[] data = new NameValuePair[para.size()];
		int index = 0;
		for (String s : para.keySet()) {
			data[index++] = new NameValuePair(s, para.get(s));
		}
		postMethod.setRequestBody(data); // 设置请求参数
		if (!cookie.equals("")) {
			postMethod.setRequestHeader("cookie", cookie);
		}
		try {
			int statusCode = hc.executeMethod(postMethod);
			if (statusCode != HttpStatus.SC_OK) {
				LOGGER.error("Method failed: " + postMethod.getStatusLine());
			}
			responseBody = postMethod.getResponseBodyAsString();
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			LOGGER.error("发生网络超时异常，可能是网络连接有问题" + e.getMessage());
		} catch (UnknownHostException e) {
			e.printStackTrace();
			LOGGER.error("发生致命的异常，可能是网络连接有问题" + e.getMessage());
		} catch (HttpException e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		} finally {
			postMethod.releaseConnection(); // 释放链接
		}
		return responseBody;
	}

	public static String getCookie() {
		Cookie[] cookies = hc.getState().getCookies();
		String tmpcookies = "";
		for (Cookie c : cookies) {
			tmpcookies += c.toString() + ";";
		}
		return tmpcookies;
	}

	public void clearCookies() {
		hc.getState().clearCookies();
	}

	/**
	 * 返回值为HTTP状态码
	 * 
	 * @param url
	 * @param para
	 * @param cookie
	 * @return
	 * @throws IOException
	 */
	public static int getStatusCode(String url, String cookie) throws IOException {
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler()); // 使用系统提供的默认的恢复策略
		if (!cookie.equals("")) {
			getMethod.setRequestHeader("cookie", cookie);
		}
		int statusCode = 0;
		try {
			statusCode = hc.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				LOGGER.error("Method failed: " + getMethod.getStatusLine());
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			LOGGER.error("发生网络超时异常，可能是网络连接有问题" + e.getMessage());
		} catch (UnknownHostException e) {
			e.printStackTrace();
			LOGGER.error("发生致命的异常，可能是网络连接有问题" + e.getMessage());
		} catch (HttpException e) { // 发生致命的异常
			e.printStackTrace();
			LOGGER.error("发生致命的异常" + e.getMessage());
		} catch (IOException e) { // 发生网络异常
			e.printStackTrace();
			LOGGER.error("发生网络异常" + e.getMessage());
		} finally {
			getMethod.releaseConnection(); // 释放链接
		}
		return statusCode;
	}

	/**
	 * @introduce: 模拟登陆获取cookie，post请求，没有验证码的情况
	 * @return
	 * @return String
	 */
	public static String simulateLoginAcquireCookie(String loginUrl, String nameField, String nameValue, String passField, String passValue) {
		PostMethod postMethod = new PostMethod(loginUrl);
		NameValuePair[] data = { new NameValuePair(nameField, nameValue), new NameValuePair(passField, passValue) };
		postMethod.setRequestBody(data);
		try {
			int statusCode = hc.executeMethod(postMethod);
			Cookie[] cookies = hc.getState().getCookies();
			StringBuffer tmpcookies = new StringBuffer();
			for (Cookie c : cookies) {
				tmpcookies.append(c.toString() + ";");
				System.out.println("cookies = " + c.toString());
			}
			if (statusCode == 302) {
				return tmpcookies.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {

	}
}
