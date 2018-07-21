package com.small.crawler.util.proxy;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.small.crawler.util.document.CrawlParam;
import com.small.crawler.util.document.HttpURLConnectionFactory;

/**
 * @author caiqibin
 * @date 2017年6月16日
 * @introduce: (快代理)http://www.kuaidaili.com/free/inha/1/
 */
public class WebQuickProxy extends ProxyCrawlBase {

	public static void main(String[] args) {
		WebQuickProxy proxy = new WebQuickProxy();
		proxy.crawlLastesProxyInfo();
	}

	@Override
	public void crawlLastesProxyInfo() {
		for (int i = 1; i < 10; i++) {
			Document document = HttpURLConnectionFactory.getDocument(new CrawlParam("https://www.kuaidaili.com/free/inha/" + i + "/"));
			if (document == null) {
				continue;
			}
			Elements elements = document.select("div[id=list]>table>tbody>tr");
			for (Element element : elements) {
				String ip = element.select("td[data-title=IP]").first().text();
				String port = element.select("td[data-title=PORT]").first().text();
				super.ipInfoSet.add(ip + ":" + port);
				if (super.ipInfoSet.size() > 20) {
					return;
				}
			}
		}
	}

}
