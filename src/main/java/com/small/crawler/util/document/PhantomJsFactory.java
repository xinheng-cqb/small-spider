package com.small.crawler.util.document;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.small.crawler.util.CrawlerUtil;

/**
 * @author caiqibin
 * @date 2017年7月21日
 * @introduce:通过PhantomJs来获取页面，能同事加载该页面的js内容,暂不支持代理ip，设置cookie
 */
public class PhantomJsFactory {

	/*public void test() {
		// ps 各个pom版本，各种依赖都试了，还是运行不起来，希望后面能解决
		String programPath = "D:\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe";
		File path = new File(programPath);
		System.setProperty("phantomjs.binary.path", path.getAbsolutePath());
		WebDriver driver2 = new PhantomJSDriver();
		driver2.navigate().to("www.baidu.com");
		System.out.println(driver2.getPageSource());

		DesiredCapabilities caps = new DesiredCapabilities();
		((DesiredCapabilities) caps).setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, programPath);
		caps.setJavascriptEnabled(true);
		caps.setCapability("takesScreenshot", true);

		WebDriver driver = new PhantomJSDriver(caps);
		driver.navigate().to("www.baidu.com");
		System.out.println(driver.getPageSource());
	}*/

	public static WebDriver getPhantomJs() {
		String osname = System.getProperties().getProperty("os.name");

		if (osname.equals("Linux")) {// 判断系统的环境win or Linux
			System.setProperty("phantomjs.binary.path", "/usr/bin/phantomjs");
		} else {
			String programPath = CrawlerUtil.getResourcePath("phantomjs/phantomjs.exe");
			System.setProperty("phantomjs.binary.path", programPath);// 设置PhantomJs访问路径
		}
		DesiredCapabilities desiredCapabilities = DesiredCapabilities.phantomjs();
		// 设置参数
		desiredCapabilities.setCapability("phantomjs.page.settings.userAgent",
				"Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
		desiredCapabilities.setCapability("phantomjs.page.customHeaders.User-Agent",
				"Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:50.0) Gecko/20100101 　　Firefox/50.0");

		return new PhantomJSDriver(desiredCapabilities);
	}

	/*public static void main(String[] args) {
		Document documentStr = getDocument("https://s.wxb.com.cn/#query-success?channelNo=S000000");
		System.out.println(documentStr);
	}*/

	/**
	 * @introduce: 根据URL获取页面,使用默认的utf-8编码,不使用代理
	 * @param url
	 * @return Document
	 */
	public static Document getDocument(String url) {
		String result = getDocumentStr(url);
		if (result == null) {
			return null;
		}
		return Jsoup.parse(result);
	}

	/**
	 * @introduce: 根据URL获取页面,要指定编码,不使用代理
	 * @param url
	 * @param charset
	 * @return Document
	 */
	public static Document getDocument(String url, String charset) {
		String result = getDocumentStr(url, charset);
		if (result == null) {
			return null;
		}
		return Jsoup.parse(result);
	}

	/**
	 * @introduce: 根据URL获取页面,使用默认的utf-8编码,不使用代理
	 * @param url
	 * @return Document
	 */
	public static String getDocumentStr(String url) {
		return getDocumentStr(url, "utf-8");
	}

	/**
	 * @introduce: 根据URL获取页面,要指定编码,不使用代理
	 * @param url
	 * @param charset
	 * @return String
	 */
	public static String getDocumentStr(String url, String charset) {
		return getDocumentStr(url, charset, "x");
	}

	/**
	 * @introduce: 获取页面
	 * @param url 网页链接
	 * @param charset 编码
	 * @param proxy 代理形式 ip:port形式（示例：192.168.1.2:8080）
	 * @return String 页面内容
	 */
	public static String getDocumentStr(String url, String charset, String proxy) {
		Runtime runtime = Runtime.getRuntime();
		BufferedReader br = null;
		try {
			String programPath = CrawlerUtil.getResourcePath("phantomjs/phantomjs.exe");
			String jsPath = CrawlerUtil.getResourcePath("phantomjs/crawler.js");
			// 这个方法中的两个参数需要对应的文件存在，可根据实际情况更改 各个参数拼接的字符串用用一个空格隔开
			String path = programPath + " " + jsPath + " " + url + " " + proxy;
			Process p = runtime.exec(path);
			br = new BufferedReader(new InputStreamReader(p.getInputStream(), charset));
			StringBuffer pageInfoStr = new StringBuffer();

			String readLine = null;
			while ((readLine = br.readLine()) != null) {
				pageInfoStr.append(readLine);
			}
			br.close();
			return pageInfoStr.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		System.out.println(getDocument("https://www.tianyancha.com/search?key=%E5%BE%AE%E6%98%93&searchType=company"));
	}

}
