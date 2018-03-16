package com.small.crawler.util.proxy;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.small.crawler.util.document.CrawlParam;
import com.small.crawler.util.document.HttpURLConnectionFactory;

/**
 * @author caiqibin
 * @date 2017年6月16日
 * @introduce:(66Ip) http://www.66ip.cn/ 这里用的是浙江省的代理
 */
public class WebXiCiProxy extends ProxyCrawlBase {

	public static void main(String[] args) {
		WebXiCiProxy test = new WebXiCiProxy();
		test.crawlLastesProxyInfo();
	}

	@Override
	protected void crawlLastesProxyInfo() {
		for (int page = 1; page < 50; page++) {
			Document document = HttpURLConnectionFactory.getDocument(new CrawlParam("http://www.xicidaili.com/wn/" + page));
			if (document == null) {
				continue;
			}
			Elements elements = document.select("table[id=ip_list]>tbody>tr");
			for (int i = 1, size = elements.size(); i < size; i++) {
				String ip = elements.get(i).select("td").get(1).text();
				String port = elements.get(i).select("td").get(2).text();
				super.ipInfoSet.add(ip + ":" + port);
				if (super.ipInfoSet.size() > 20) {
					return;
				}
			}
		}
	}

}
