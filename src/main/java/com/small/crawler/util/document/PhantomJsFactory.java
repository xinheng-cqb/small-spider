package com.small.crawler.util.document;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.small.crawler.util.CrawlerUtil;

/**
 * @author xinheng-cqb
 * @date 2018年4月20日
 * @introduce: 通过PhantomJs来获取页面，能同事加载该页面的js内容
 */
public class PhantomJsFactory {

	public static void main(String[] args) {
		// 测试截屏功能
		PhantomJSDriver driver = getHeadlessDriver();
		driver.get("http://www.baidu.com/");
		File srcFile = driver.getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(srcFile, new File("screenshot.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		driver.quit();
	}

	/**
	 * @introduce: 获取无界面的驱动器，返回的驱动通过driver.get(url)形式来调用，driver使用结束后需要调用.quit()来关闭
	 * @return WebDriver
	 */
	public static PhantomJSDriver getHeadlessDriver() {
		String programPath = CrawlerUtil.getResourcePath("phantomjs/phantomjs.exe");
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setJavascriptEnabled(true);
		caps.setCapability("takesScreenshot", true);
		caps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "Accept-Language", "en-US");
		caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, programPath);
		ArrayList<String> cliArgsCap = new ArrayList<String>();
		cliArgsCap.add("--web-security=false");
		cliArgsCap.add("--ssl-protocol=any");
		cliArgsCap.add("--ignore-ssl-errors=true");
		((DesiredCapabilities) caps).setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
		return new PhantomJSDriver(caps);

	}

	/**
	 * @introduce: 用实际的谷歌浏览器来调试使用，返回的驱动通过driver.navigate().to(url)形式来调用，driver使用结束后需要调用.close()来关闭
	 * @return WebDriver
	 */
	public static WebDriver debugWebPage() {

		String programPath = CrawlerUtil.getResourcePath("phantomjs/chromedriver.exe");
		DesiredCapabilities caps = new DesiredCapabilities();
		System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, programPath);
		caps.setJavascriptEnabled(true);
		caps.setCapability("takesScreenshot", true);
		return new ChromeDriver(caps);
	}

}
