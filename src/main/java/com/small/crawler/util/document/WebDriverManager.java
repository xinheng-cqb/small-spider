package com.small.crawler.util.document;

import java.io.Closeable;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;

public class WebDriverManager implements Closeable {
	private Log log = LogFactory.getLog(WebDriverManager.class);

	private WebDriverPool webDriverPool = null;

	public WebDriverManager() {
		this.webDriverPool = new PhantomjsWebDriverPool(1, false);
	}

	public WebDriverManager(WebDriverPool webDriverPool) {
		this.webDriverPool = webDriverPool;
	}

	public void load(String url, int sleepTimeMillis, SeleniumAction... actions) {
		WebDriver driver = null;
		try {
			driver = webDriverPool.get();
			driver.get(url);
			sleep(sleepTimeMillis);
			WebDriver.Options manage = driver.manage();
			manage.window().maximize();
			for (SeleniumAction action : actions) {
				action.execute(driver);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			log.error("", e);
		} finally {
			if (driver != null) {
				webDriverPool.returnToPool(driver);
			}
		}
	}

	public void load(SeleniumAction... actions) {
		WebDriver driver = null;
		try {
			driver = webDriverPool.get();
			WebDriver.Options manage = driver.manage();
			manage.window().maximize();
			for (SeleniumAction action : actions) {
				action.execute(driver);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			log.error("", e);
		} finally {
			if (driver != null) {
				webDriverPool.returnToPool(driver);
			}
		}
	}

	public void shutDown() {
		if (webDriverPool != null) {
			webDriverPool.shutdown();
		}
	}

	@Override
	public void close() throws IOException {
		shutDown();
	}

	public void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
