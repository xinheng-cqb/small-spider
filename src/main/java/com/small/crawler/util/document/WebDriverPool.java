package com.small.crawler.util.document;

import org.openqa.selenium.WebDriver;

public interface WebDriverPool {
	WebDriver get() throws InterruptedException;

	void returnToPool(WebDriver webDriver);

	void close(WebDriver webDriver);

	void shutdown();
}
