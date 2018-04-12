package com.small.crawler.util.document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.small.crawler.constants.DocumentUtilConstants;
import com.small.crawler.util.proxy.ProxyUtil;

/**
 * @author caiqibin
 * @date 2017年6月16日
 * @introduce:通过HttpURlConnection方式获取网页，如果要使用代理，建议先根据目标网站链接获取可用的代理。参考ProxyTestClient
 */
public class HttpURLConnectionFactory {

	private final static Log LOGGER = LogFactory.getLog(HttpURLConnectionFactory.class);

	/**
	 * @introduce:根据网页参数获取页面，如果设置使用代理（useProxy为true），则用自带的代理IP（不建议）
	 * @param httpCrawlParam
	 * @return Document
	 */
	public static Document getDocument(CrawlParam crawlParam) {
		String documentStr = getDocumentStr(crawlParam);
		if (documentStr == null) {
			return null;
		}
		return Jsoup.parse(documentStr);
	}

	/**
	 * @introduce:如果useProxy设置为true，则会使用自带的代理进行爬取（不建议）
	 * @param httpCrawlParam
	 * @return String
	 */
	public static String getDocumentStr(CrawlParam crawlParam) {
		if (!crawlParam.isUseProxy()) {
			return getDocumentStr(crawlParam, null);
		}
		int tryCount = 0;
		Proxy proxy = ProxyUtil.INSTANCE.getProxy();
		String documentStr = getDocumentStr(crawlParam, proxy);
		if (documentStr == null && tryCount < 3) {
			documentStr = getDocumentStr(crawlParam);
			tryCount++;
		}
		if (documentStr == null) {
			ProxyUtil.INSTANCE.removeProxy(proxy);
		}
		return documentStr;
	}

	/**
	 * @introduce:根据网页参数和代理IP获取页面,其中useProxy需设置为true,不然代理不会生效
	 * @param httpCrawlParam
	 * @param ip
	 * @param port
	 * @return String
	 */
	public static String getDocumentStr(CrawlParam crawlParam, String ip, String port) {
		try {
			InetAddress host = InetAddress.getByName(ip);
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, Integer.parseInt(port)));
			return getDocumentStr(crawlParam, proxy);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			LOGGER.error(sw.toString());
		}
		return null;
	}

	/**
	 * @introduce:根据网页参数和代理IP获取页面,其中useProxy需设置为true,不然代理不会生效
	 * @param httpCrawlParam
	 * @param proxy
	 * @return String
	 */
	public static String getDocumentStr(CrawlParam crawlParam, Proxy proxy) {
		HttpURLConnection conn = null;
		BufferedReader br = null;
		try {
			Thread.sleep(crawlParam.getInterval() + crawlParam.getIntervalRange());
			URL url = new URL(crawlParam.getUrlStr());
			if (crawlParam.isUseProxy()) {
				conn = (HttpURLConnection) url.openConnection(proxy);
			} else {
				conn = (HttpURLConnection) url.openConnection();
			}
			conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0");
			if (crawlParam.getRequestHeadMap() != null) {
				for (Entry<String, String> entry : crawlParam.getRequestHeadMap().entrySet()) {
					conn.addRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			if (crawlParam.getCookie() != null) {
				conn.addRequestProperty("Cookie", crawlParam.getCookie());
			}
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			if (DocumentUtilConstants.POST_METHOD.equals(crawlParam.getRequestMethod())) {
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setUseCaches(false);
				OutputStream out = conn.getOutputStream();
				out.write(crawlParam.getPostParam().getBytes());
				out.flush();
				out.close();
			}

			if (HttpURLConnection.HTTP_OK != conn.getResponseCode()) {
				LOGGER.info("=====get document failure ,error code is " + conn.getResponseCode() + " , request url is " + crawlParam.getUrlStr());
				return null;
			}

			InputStream inputStream = null;
			if (crawlParam.isUseGZip()) {
				inputStream = new GZIPInputStream(conn.getInputStream());
			} else {
				inputStream = conn.getInputStream();
			}

			br = new BufferedReader(new InputStreamReader(inputStream, crawlParam.getCharset()));
			StringBuffer pageInfoStr = new StringBuffer();

			String readLine = null;
			while ((readLine = br.readLine()) != null) {
				pageInfoStr.append(readLine);
			}
			br.close();
			conn.disconnect();
			conn = null;
			return pageInfoStr.toString();
		} catch (Exception e) {
			LOGGER.error("===get document error,request url is  " + crawlParam.getUrlStr(), e);
			return null;
		}
	}

	/**
	 * @introduce:根据网页链接下载文件到本地
	 * @param urlStr
	 * @param cookie
	 * @param outputPath
	 * @return String (文件保存位置绝对路径)
	 */
	public static String downloadFile(CrawlParam crawlParam) {
		HttpURLConnection conn = null;
		try {
			if (crawlParam.getOutputPath() == null) {
				return "文件保存位置不嫩为空！！！";
			}
			File file = new File(crawlParam.getOutputPath());
			OutputStream os = new FileOutputStream(file);
			Thread.sleep(500);
			URL url = new URL(crawlParam.getUrlStr());
			conn = (HttpURLConnection) url.openConnection();
			conn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			if (crawlParam.getCookie() != null) {
				conn.addRequestProperty("Cookie", crawlParam.getCookie());
			}
			conn.setConnectTimeout(50000);
			conn.setReadTimeout(50000);

			if (HttpURLConnection.HTTP_OK != conn.getResponseCode()) {
				LOGGER.info("=====get document failure ,error code is " + conn.getResponseCode() + " , request url is " + crawlParam.getUrlStr());
				os.close();
				return "error code";
			}
			InputStream inputStream = null;
			if (crawlParam.isUseGZip()) {
				inputStream = new GZIPInputStream(conn.getInputStream());
			} else {
				inputStream = conn.getInputStream();
			}
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.flush();
			os.close();
			conn.disconnect();
			conn = null;
			return file.getAbsolutePath();
		} catch (Exception e) {
			LOGGER.error("===get document error,request url is  " + crawlParam.getUrlStr(), e);
			return "下载失败";
		}

	}

	/*	public static String getDocument(String urlStr, String charset) {
			HttpURLConnection conn = null;
			BufferedReader br = null;
			try {
				URL url = new URL(urlStr);
				conn = (HttpURLConnection) url.openConnection();
				conn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
				conn.setConnectTimeout(8000);
				conn.setReadTimeout(8000);

				if (HttpURLConnection.HTTP_OK != conn.getResponseCode()) {
					LOGGER.info("=====get document failure ,error code is " + conn.getResponseCode() + " , request url is " + urlStr);
					return "error code";
				}
				InputStream inputStream = null;
				br = new BufferedReader(new InputStreamReader(inputStream, charset));
				StringBuffer pageInfoStr = new StringBuffer();

				String readLine = null;
				while ((readLine = br.readLine()) != null) {
					pageInfoStr.append(readLine);
				}
				br.close();
				conn.disconnect();
				conn = null;
				return pageInfoStr.toString();
			} catch (Exception e) {
				e.printStackTrace();
				return "获取失败";
			}
		}*/

}
