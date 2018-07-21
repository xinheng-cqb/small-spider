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

/**
 * @author caiqibin
 * @date 2017年6月16日
 * @introduce:通过HttpURlConnection方式获取网页，如果要使用代理，建议先根据目标网站链接获取可用的代理。参考ProxyTestClient
 */
public class HttpURLConnectionFactory {
	private static final String[] restrictedHeaders = { "Access-Control-Request-Headers", "Access-Control-Request-Method", "Connection",
			"Content-Length", "Content-Transfer-Encoding", "Expect", "Host", "Keep-Alive", "Origin", "Trailer", "Transfer-Encoding", "Upgrade", "Via" };

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
	 * @introduce:如果useProxy设置为true，则会使用自带的代理进行爬取（不建议）,已经改为不使用自带的代理，即使为true
	 * @param httpCrawlParam
	 * @return String
	 */
	public static String getDocumentStr(CrawlParam crawlParam) {
		return getDocumentStr(crawlParam, null);
		/*if (!crawlParam.isUseProxy()) {
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
		return documentStr;*/
	}

	public static Document getDocument(CrawlParam crawlParam, String ip, String port) {
		String documentStr = getDocumentStr(crawlParam, ip, port);
		if (documentStr == null) {
			return null;
		}
		return Jsoup.parse(documentStr);
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
			crawlParam.setUseProxy(true);
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
		// HttpURLConnection 直接设置Host 头部无效
		// 参考链接：https://blog.csdn.net/zlfprogram/article/details/79030217
		// 有没有生效可以通过查看conn 对象的requests属性来验证，如果没生效可以通过在程序入口地点直接放上
		// System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
		boolean allowRestrictedHeaders = false;
		if (crawlParam.getRequestHeadMap() != null) {
			for (Entry<String, String> entry : crawlParam.getRequestHeadMap().entrySet()) {
				if (!allowRestrictedHeaders) {
					for (String restrictedHeader : restrictedHeaders) {
						if (restrictedHeader.equals(entry.getKey())) {
							allowRestrictedHeaders = true;
							System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
							break;
						}
					}
				}
			}
		}
		// HTTPS直接使用HOST为ip地址的时候，是无法正确使用SSL校验安全证书的，因为证书和域名绑定。参考链接：https://blog.csdn.net/herotangabc/article/details/41824065
		/*if (crawlParam.getUrlStr().matches("https://([\\d]+\\.){3}[\\d]+.*")) {
			HttpsURLConnection.setDefaultHostnameVerifier((String hostname, SSLSession session) -> {
				return true;
			});
		}*/
		HttpURLConnection conn = null;
		BufferedReader br = null;
		int tryNum = 0;
		while (tryNum < crawlParam.getTryCount()) {
			tryNum++;
			try {
				Thread.sleep(crawlParam.getInterval() + crawlParam.getActualRangeValue());
				URL url = new URL(crawlParam.getUrlStr());
				if (crawlParam.isUseProxy()) {
					conn = (HttpURLConnection) url.openConnection(proxy);
				} else {
					conn = (HttpURLConnection) url.openConnection();
				}

				conn.addRequestProperty("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
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
				if (DocumentUtilConstants.POST_METHOD.equals(crawlParam.getRequestMethod())
						|| DocumentUtilConstants.PUT_METHOD.equals(crawlParam.getRequestMethod())) {
					conn.setDoInput(true);
					conn.setDoOutput(true);
					conn.setRequestMethod(crawlParam.getRequestMethod());
					conn.setUseCaches(false);
					OutputStream out = conn.getOutputStream();
					out.write(crawlParam.getPostParam().getBytes());
					out.flush();
					out.close();
				}

				if (HttpURLConnection.HTTP_OK != conn.getResponseCode()) {
					LOGGER.info("=====get document failure ,error code is " + conn.getResponseCode() + " , request url is " + crawlParam.getUrlStr());
					continue;
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
				if (pageInfoStr.length() < 10) {
					continue;
				}
				return pageInfoStr.toString();
			} catch (Exception e) {
				LOGGER.error("===get document error,request url is  " + crawlParam.getUrlStr(), e);
				continue;
			}
		}
		return null;
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
}
