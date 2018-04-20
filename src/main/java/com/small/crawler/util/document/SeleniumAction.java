package com.small.crawler.util.document;

import org.openqa.selenium.WebDriver;

/**
 * @author xinheng-cqb
 * @date 2018年4月20日
 * @introduce: 根据获取到的驱动进行对应的操作，暂时只有phantomjs,建议将driver强制转换为PhantomJSDriver
 */
public interface SeleniumAction {
	void execute(WebDriver driver);
}
