package com.small.crawler.util.document;

import java.util.ArrayList;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.small.crawler.util.CrawlerUtil;

public class PhantomjsWebDriverPool implements WebDriverPool {
	private Log logger = LogFactory.getLog(PhantomjsWebDriverPool.class);

	private int CAPACITY = 5;
	private AtomicInteger refCount = new AtomicInteger(0);
	private static final String DRIVER_PHANTOMJS = "phantomjs/phantomjs.exe";

	/**
	 * store webDrivers available
	 */
	private BlockingDeque<WebDriver> innerQueue = new LinkedBlockingDeque<WebDriver>(CAPACITY);

	private String PHANTOMJS_PATH;
	private DesiredCapabilities caps = DesiredCapabilities.phantomjs();

	public PhantomjsWebDriverPool() {
		this(5, false);
	}

	/**
	 * 
	 * @param poolsize
	 * @param loadImg 是否加载图片，默认不加载
	 */
	public PhantomjsWebDriverPool(int poolsize, boolean loadImg) {
		this.CAPACITY = poolsize;
		innerQueue = new LinkedBlockingDeque<WebDriver>(poolsize);
		PHANTOMJS_PATH = CrawlerUtil.getResourcePath(DRIVER_PHANTOMJS);
		caps.setJavascriptEnabled(true);
		caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, PHANTOMJS_PATH);
		// caps.setCapability("takesScreenshot", false);
		caps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");
		ArrayList<String> cliArgsCap = new ArrayList<String>();
		// http://phantomjs.org/api/command-line.html
		cliArgsCap.add("--web-security=false");
		cliArgsCap.add("--ssl-protocol=any");
		cliArgsCap.add("--ignore-ssl-errors=true");
		if (loadImg) {
			cliArgsCap.add("--load-images=true");
		} else {
			cliArgsCap.add("--load-images=false");
		}
		caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
		caps.setCapability(PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_CLI_ARGS, new String[] { "--logLevel=INFO" });

	}

	public WebDriver get() throws InterruptedException {
		WebDriver poll = innerQueue.poll();
		if (poll != null) {
			return poll;
		}
		if (refCount.get() < CAPACITY) {
			synchronized (innerQueue) {
				if (refCount.get() < CAPACITY) {

					WebDriver mDriver = new PhantomJSDriver(caps);
					// 尝试性解决：https://github.com/ariya/phantomjs/issues/11526问题
					mDriver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
					// mDriver.manage().window().setSize(new Dimension(1366,
					// 768));
					innerQueue.add(mDriver);
					refCount.incrementAndGet();
				}
			}
		}
		return innerQueue.take();
	}

	public void returnToPool(WebDriver webDriver) {
		// webDriver.quit();
		// webDriver=null;
		innerQueue.add(webDriver);
	}

	public void close(WebDriver webDriver) {
		refCount.decrementAndGet();
		webDriver.quit();
		webDriver = null;
	}

	public void shutdown() {
		try {
			for (WebDriver driver : innerQueue) {
				close(driver);
			}
			innerQueue.clear();
		} catch (Exception e) {
			// e.printStackTrace();
			logger.warn("webdriverpool关闭失败", e);
		}
	}

}
