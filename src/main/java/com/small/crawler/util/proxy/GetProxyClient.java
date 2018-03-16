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
				.setUrlStr("https://xueqiu.com/statuses/search.json?page=3&q=SZ200725")
				.setCookie(
						"s=ev18ee1utc; Hm_lvt_1db88642e346389874251b5a1eded6e3=1500964702; u=261500964701749; device_id=974a1ba1698dfb454d7f281546f1bdb7; __utmt=1; __utma=1.808588405.1500969492.1500969492.1500969492.1; __utmb=1.1.10.1500969492; __utmz=1.1500969492.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); aliyungf_tc=AQAAAPP9Oj4vggcAoqXsc+ai8ryFKABk; xq_a_token=82d9cefaa0793743cb186e53294ec0e61ac2abec; xq_r_token=11b86433a20d1d1eef63ecc12252297196a20e10; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1500969492; __utmc=1")
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
