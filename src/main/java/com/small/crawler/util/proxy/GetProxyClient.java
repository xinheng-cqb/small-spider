package com.small.crawler.util.proxy;

import java.net.Proxy;
import java.util.List;

import com.small.crawler.util.document.CrawlParam;

/**
 * @author caiqibin
 * @date 2017年6月20日
 * @introduce: 根据目标网页配置信息获取可用的代理IP
 */
public class GetProxyClient {
	public static void main(String[] args) {
		CrawlParam httpCrawlParam = new CrawlParam("");
		httpCrawlParam
				.setUrlStr(
						"https://mp.weixin.qq.com/profile?src=3&timestamp=1528351901&ver=1&signature=0PnMdDtk8FNjeNDh25oqfenqqLjaooWg0Sxic7eEErdfQDmkpO92iiHGEgPHl2Uf0Da5fR1H9aTs7QlbsJMW1Q==")
				.setUseProxy(true);
		List<Proxy> proxyList = ProxyCrawlFactory.newInstance().getUsefulProxyList(httpCrawlParam, true);
		if (proxyList.size() == 0) {
			System.out.println("=======no useful proxy========");
		}
		for (Proxy proxy : proxyList) {
			System.out.println(proxy.toString());
		}
	}
}
