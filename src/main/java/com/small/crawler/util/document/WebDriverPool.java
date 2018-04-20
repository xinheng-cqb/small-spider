package com.small.crawler.util.document;

import org.openqa.selenium.WebDriver;

/**
 * @author xinheng-cqb
 * @date 2018年4月20日
 * @introduce: webDriver池接口
 */
public interface WebDriverPool {
	WebDriver get() throws InterruptedException;

	void returnToPool(WebDriver webDriver);

	void close(WebDriver webDriver);

	void shutdown();
}
