package com.small.crawler.util.proxy;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.small.crawler.util.document.CrawlParam;

/**
 * @author caiqibin
 * @date 2017年7月21日
 * @introduce:代理IP信息爬取工厂
 */
public class ProxyCrawlFactory {

	private static final Log LOG = LogFactory.getLog(ProxyCrawlFactory.class);

	public static ProxyCrawlFactory newInstance() {
		return ProxyCrawlFactorySingleton.SINGLETON;
	}

	private ProxyCrawlFactory() {

	}

	/**
	 * @introduce:针对每个代理网站使用单独的线程去爬取可用的代理IP
	 * @param httpCrawlParam 标网页配置信息
	 * @param strictCheck 对代理进行再次过滤
	 * @param timeLimit 每个线程超时配置 （分钟为单位）
	 * @return List<Proxy> 结果列表
	 */
	public List<Proxy> getUsefulProxyList(CrawlParam httpCrawlParam, boolean strictCheck, long timeLimit) {
		httpCrawlParam = httpCrawlParam.setUseProxy(true);
		ExecutorService service = Executors.newFixedThreadPool(UseableProxyWebsiteEnum.values().length);
		List<ProxyCrawlThread> threadList = new ArrayList<>();
		for (UseableProxyWebsiteEnum useableProxyWebsite : UseableProxyWebsiteEnum.values()) {
			threadList.add(new ProxyCrawlThread(useableProxyWebsite.getProxyCrawlBase(), httpCrawlParam, strictCheck));
		}
		List<Proxy> usefulProxyList = new ArrayList<Proxy>();
		try {
			List<Future<List<Proxy>>> futureList = null;
			if (timeLimit != 0) {
				futureList = service.invokeAll(threadList, timeLimit, TimeUnit.MINUTES);
			} else {
				futureList = service.invokeAll(threadList);
			}
			for (Future<List<Proxy>> future : futureList) {
				usefulProxyList.addAll(future.get());
			}
		} catch (Exception e) {
			LOG.error("get proxy error", e);
		}
		service.shutdown();
		return usefulProxyList;
	}

	/**
	 * @introduce: 针对每个代理网站使用单独的线程去爬取可用的代理IP
	 * @param httpCrawlParam 目标网页配置信息
	 * @param strictCheck 对代理进行再次过滤
	 * @return List<Proxy> 结果列表
	 */
	public List<Proxy> getUsefulProxyList(CrawlParam httpCrawlParam, boolean strictCheck) {
		return getUsefulProxyList(httpCrawlParam, strictCheck, 0);
	}

	private static class ProxyCrawlFactorySingleton {
		private static final ProxyCrawlFactory SINGLETON = new ProxyCrawlFactory();
	}
}
