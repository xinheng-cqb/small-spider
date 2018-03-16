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
public class Web66Ip extends ProxyCrawlBase {

	public static void main(String[] args) {
		Web66Ip test = new Web66Ip();
		test.crawlLastesProxyInfo();
	}

	@Override
	protected void crawlLastesProxyInfo() {
		for (int page = 3; page < 6; page++) {
			Document document = HttpURLConnectionFactory.getDocument(new CrawlParam("http://www.66ip.cn/areaindex_11/" + page + ".html")
					.setCharset("gbk"));
			if (document == null) {
				continue;
			}
			Elements elements = document.select("div[id=footer]>div>table>tbody>tr");
			for (int i = 1, size = elements.size(); i < size; i++) {
				String ip = elements.get(i).select("td").first().text();
				String port = elements.get(i).select("td").get(1).text();
				super.ipInfoSet.add(ip + ":" + port);
				if (super.ipInfoSet.size() > 20) {
					return;
				}
			}
		}
	}

}
