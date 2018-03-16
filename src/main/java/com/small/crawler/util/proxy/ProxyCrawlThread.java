package com.small.crawler.util.proxy;

import java.net.Proxy;
import java.util.List;
import java.util.concurrent.Callable;

import com.small.crawler.util.document.CrawlParam;

/**
 * @author caiqibin
 * @date 2017年7月21日
 * @introduce:多线程实现代理IP的爬取
 */
public class ProxyCrawlThread implements Callable<List<Proxy>> {

	private final ProxyCrawlBase proxyCrawlBase;
	private final CrawlParam targetCrawlParam;
	private final boolean strictCheck;

	public ProxyCrawlThread(ProxyCrawlBase proxyCrawlBase, CrawlParam targetCrawlParam, boolean strictCheck) {
		this.proxyCrawlBase = proxyCrawlBase;
		this.targetCrawlParam = targetCrawlParam;
		this.strictCheck = strictCheck;
	}

	@Override
	public List<Proxy> call() throws Exception {
		return this.proxyCrawlBase.buildUseableList(this.targetCrawlParam, this.strictCheck);
	}
}
