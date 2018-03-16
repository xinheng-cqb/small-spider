package com.small.crawler.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;

import com.small.crawler.constants.DocumentUtilConstants;
import com.small.crawler.util.ExcelUtil;
import com.small.crawler.util.FileUtil;
import com.small.crawler.util.document.CrawlParam;
import com.small.crawler.util.document.HttpURLConnectionFactory;

/**
 * @author caiqibin
 * @date 2017年9月20日
 * @introduce:
 */
public class AreaCrawler {
	public static void main(String[] args) {
		Map<String, String> infoMap = new HashMap<>();
		List<String> contentList = new ArrayList<>();
		List<String> textList = FileUtil.readFile("C:\\Users\\Administrator\\Desktop\\ttt.txt");
		for (String text : textList) {
			String key = text.substring(0, 6);
			if (infoMap.get(key) == null) {
				String value = queryArea(text);
				infoMap.put(key, value);
			}
			contentList.add(text + ";" + infoMap.get(key));
		}
		ExcelUtil.exportExcel("C:\\Users\\Administrator\\Desktop\\area.xls", contentList);
	}

	private static String queryArea(String cardNum) {
		CrawlParam crawlParam = new CrawlParam("http://idcard.911cha.com/");
		crawlParam.setRequestMethod(DocumentUtilConstants.POST_METHOD);
		crawlParam.setPostParam("q=" + cardNum);
		Document document = HttpURLConnectionFactory.getDocument(crawlParam);
		String info = document.select("p[class=l200]").text().split("生　日：")[0];
		if (info.length() > 1) {
			info = info.replace("发证地：", "");
			info = info.replaceAll(" ", "_");
			return info.substring(0, info.length() - 1);
		}
		return "";
	}
}
