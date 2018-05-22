package com.small.crawler;

import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.collect.Lists;
import com.small.crawler.util.CrawlerUtil;
import com.small.crawler.util.ExcelUtil;
import com.small.crawler.util.document.CrawlParam;
import com.small.crawler.util.document.HttpURLConnectionFactory;

/**
 * @author xinheng-cqb
 * @date 2018年5月15日
 * @introduce:
 */
public class Test1 {
	public static void main(String[] args) throws UnknownHostException {
		CrawlParam crawlParam = new CrawlParam();
		List<String> contentList = Lists.newArrayList();
		contentList.add("医院名称;城市;医院等级;擅长病症;医院地址;医院电话;医院邮箱;医院网站");
		for (int i = 1; i < 1530; i++) { //
			crawlParam.setUrlStr("https://www.zgylbx.com/index.php?m=content&c=index&a=lists&catid=106&k1=0&k2=0&k3=0&k4=&page=" + i);
			Document document = HttpURLConnectionFactory.getDocument(crawlParam);
			List<String> subList = extractContents(document);
			if (subList.size() == 0) {
				System.out.println(crawlParam.getUrlStr());
				continue;
			}
			contentList.addAll(subList);
		}
		ExcelUtil.exportExcel("C:\\Users\\Administrator\\Desktop\\医院信息.xls", contentList);
	}

	private static List<String> extractContents(Document document) {
		List<String> contentList = Lists.newArrayList();
		if (document == null) {
			return contentList;
		}
		Elements baseInfoEles = document.select("tr[class$=tr-dt]");
		Elements extendInfoEles = document.select("tr[class^=tr-dd]");
		if (baseInfoEles.size() != 0 && baseInfoEles.size() == extendInfoEles.size()) {
			for (int i = 0; i < baseInfoEles.size(); i++) {
				StringBuilder sb = new StringBuilder();
				Elements baseEles = baseInfoEles.get(i).select("td");
				for (Element base : baseEles) {
					sb.append(base.text().replaceAll(";", "；") + ";");
				}

				String extendInfo = extendInfoEles.get(i).select("td").text().replaceAll(";", "；");
				String website = CrawlerUtil.matchBetweenSymbol(extendInfo, "网站:", " ");
				if (website == null) {
					website = "";
				}
				contentList.add(MessageFormat.format("{0}{1};{2};{3};{4}", sb.toString(), CrawlerUtil.matchBetweenSymbol(extendInfo, "地址:", "医"),
						CrawlerUtil.matchBetweenSymbol(extendInfo, "电话:", "医"), CrawlerUtil.matchBetweenSymbol(extendInfo, "邮箱:", "医"), website));
			}
		}
		return contentList;
	}
}
