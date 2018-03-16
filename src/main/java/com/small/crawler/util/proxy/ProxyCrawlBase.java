package com.small.crawler.util.proxy;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.small.crawler.util.document.CrawlParam;
import com.small.crawler.util.document.HttpURLConnectionFactory;

/**
 * @author caiqibin
 * @date 2017年6月21日
 * @introduce:代理IP信息爬取底层实现抽象类
 */
public abstract class ProxyCrawlBase {

	protected Set<String> ipInfoSet = new HashSet<>();

	protected abstract void crawlLastesProxyInfo();

	/**
	 * @introduce:获取可用的代理列表
	 * @param targetCrawlParam 目标网页配置信息
	 * @param strictCheck 对代理进行再次过滤
	 * @return List<Proxy> 可用的代理信息列表
	 */
	public List<Proxy> buildUseableList(CrawlParam targetCrawlParam, boolean strictCheck) {
		crawlLastesProxyInfo();
		List<Proxy> proxyList = new LinkedList<>();
		for (String ipInfo : this.ipInfoSet) {
			try {
				String[] tempArray = ipInfo.split(":");
				InetAddress host = InetAddress.getByName(tempArray[0]);
				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, Integer.parseInt(tempArray[1])));
				proxyList.add(proxy);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		proxyList = checkList(targetCrawlParam, proxyList);
		if (strictCheck) {
			proxyList = checkList(targetCrawlParam, proxyList);
		}
		return proxyList;
	}

	private List<Proxy> checkList(CrawlParam targetCrawlParam, List<Proxy> preProxyList) {
		List<Proxy> proxyList = new LinkedList<>();
		for (Proxy proxy : preProxyList) {
			if (checkProxy(targetCrawlParam, proxy)) {
				proxyList.add(proxy);
			}
		}
		return proxyList;
	}

	private boolean checkProxy(CrawlParam targetCrawlParam, Proxy proxy) {
		String documentStr = HttpURLConnectionFactory.getDocumentStr(targetCrawlParam, proxy);
		if (documentStr == null) {
			documentStr = HttpURLConnectionFactory.getDocumentStr(targetCrawlParam, proxy);
		}
		if (documentStr == null) {
			return false;
		}
		return true;
	}

}
