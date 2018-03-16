package com.small.crawler.business;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.small.crawler.util.CrawlerUtil;
import com.small.crawler.util.DateTimeUtil;
import com.small.crawler.util.document.CrawlParam;
import com.small.crawler.util.document.HttpURLConnectionFactory;

/**
 * @author caiqibin
 * @date 2017年9月24日
 * @introduce: 基金爬虫加分析，获取最有价值的基金信息
 */
public class FundCrawler {
	String filePath = "";

	public static void main(String[] args) {
		FundCrawler crawler = new FundCrawler();
		crawler.mkdir();
		crawler.crawl();
	}

	private void mkdir() {
		String todayStr = DateTimeUtil.getTime("yyyy-MM-dd", 0);
		File file = new File("F:\\基金信息\\" + todayStr);
		if (!file.exists()) {
			file.mkdirs();
		}
		filePath = file.getAbsolutePath();
	}

	public void crawl() {
		CrawlParam crawlParam = new CrawlParam();
		crawlParam.setInterval(1000).setIntervalRange(1000);
		Map<String, List<FundInfo>> fundTypeRefAllInfoMap = new HashMap<>(5);
		List<FundInfo> allFundInfoList = new ArrayList<>();
		for (FundTypeEnums fundType : FundTypeEnums.values()) {
			Map<String, FundInfo> codeRefFundInfoMap = new HashMap<>(200);
			for (DateEnums date : DateEnums.values()) {
				List<String> baseInfoList = new ArrayList<>(200);
				int curPage = 1;
				crawlParam.setUrlStr(getUrl(fundType.getSubType(), date.getStarTime(), date.getEndTime(), curPage));
				String documentStr = HttpURLConnectionFactory.getDocumentStr(crawlParam);
				String sum = CrawlerUtil.matchNumber(documentStr, "\"sum\":");
				int allNeedCount = (int) (Integer.parseInt(sum) * date.getRate());
				while (allNeedCount > 100) {
					curPage++;
					allNeedCount -= 100;
					baseInfoList.addAll(getBaseInfoList(documentStr, 100));
					crawlParam.setUrlStr(getUrl(fundType.getSubType(), date.getStarTime(), date.getEndTime(), curPage));
					documentStr = HttpURLConnectionFactory.getDocumentStr(crawlParam);
				}
				baseInfoList.addAll(getBaseInfoList(documentStr, allNeedCount));
				updateFundInfoList(codeRefFundInfoMap, baseInfoList, date.getIndex());
			}
			List<FundInfo> fundInfoList = new ArrayList<>(codeRefFundInfoMap.values());
			fundInfoList.sort(null);
			allFundInfoList.addAll(fundInfoList);
			fundTypeRefAllInfoMap.put(fundType.getName(), fundInfoList);
		}
		allFundInfoList.sort(null);
		writeFile(allFundInfoList, filePath + "\\汇总的基金信息.txt");
		fundTypeRefAllInfoMap.forEach((key, value) -> {
			writeFile(value, filePath + "\\" + key + ".txt");
		});
	}

	private void writeFile(List<FundInfo> fundInfoList, String absolutePath) {
		File file = new File(absolutePath);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for (int i = 0; i < fundInfoList.size(); i++) {
				bw.write(i + " : " + fundInfoList.get(i).toString());
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void updateFundInfoList(Map<String, FundInfo> codeRefFundInfoMap, List<String> baseInfoList, int index) {
		for (String baseInfo : baseInfoList) {
			String[] tempArray = baseInfo.split("_");
			FundInfo fundInfo = codeRefFundInfoMap.get(tempArray[0]);
			if (fundInfo == null) {
				fundInfo = new FundInfo();
				fundInfo.setCode(tempArray[0]);
				fundInfo.setName(tempArray[1]);
			}
			fundInfo.setValue(index, Double.parseDouble(tempArray[2]));
			codeRefFundInfoMap.put(tempArray[0], fundInfo);
		}
	}

	private List<String> getBaseInfoList(String documentStr, int needCount) {
		List<String> baseInfoList = new ArrayList<>(100);
		String text = CrawlerUtil.matchBetweenSymbol(documentStr, "\"list\":", ")");
		String[] textArray = text.split("\\},\\{");
		for (int i = 0; i < needCount; i++) {
			baseInfoList.add(CrawlerUtil.matchNumber(textArray[i], "\"fundCode\":\"") + "_"
					+ CrawlerUtil.matchBetweenSymbol(textArray[i], "\"fundName\":\"", "\"") + "_"
					+ CrawlerUtil.matchNumber(textArray[i], "\"rangeYield\":\""));
		}
		return baseInfoList;
	}

	private String getUrl(String subType, String startTime, String endTime, int curPage) {
		return "http://jingzhi.funds.hexun.com/jz/JsonData/KaifangQuJianPM.aspx?callback=callback&subtype=" + subType
				+ "&pagesize=100&fundcompany=--%E5%85%A8%E9%83%A8%E5%9F%BA%E9%87%91%E7%AE%A1%E7%90%86%E5%85%AC%E5%8F%B8--&begindate=" + startTime
				+ "&enddate=" + endTime + "&curpage=" + curPage + "&fundisbuy=1&_=1506222519514";
	}
}

class FundInfo implements Comparable<FundInfo> {
	private String name;
	private String code;
	private int[] valueArray = new int[DateEnums.values().length];
	private int countOrder;
	private double weight;

	public void setName(String name) {
		this.name = name;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setValue(int index, double rangeYield) {
		this.valueArray[index] = 1;
		this.countOrder++;
		this.weight += Math.pow(2, (DateEnums.values().length - 1 - index)) * rangeYield;
	}

	@Override
	public int compareTo(FundInfo other) {
		if (this.countOrder > other.countOrder) {
			return -1;
		} else if (this.countOrder < other.countOrder) {
			return 1;
		} else {
			if (this.weight >= other.weight) {
				return -1;
			} else {
				return 1;
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("基金名称：" + this.name).append(" , 基金代码：" + this.code).append("\n基金价值数组：[ ");
		for (int i : this.valueArray) {
			sb.append(i + " ");
		}
		sb.append("]\n基金价值统计：" + this.countOrder).append(" , 基金最终权重：" + this.weight).append("\n------------- 分割线 ----------\n");
		return sb.toString();
	}
}

enum DateEnums {
	ONE_MONTH(DateTimeUtil.getBeforeTime("yyyy-MM-dd", 0, 1, 0), 0.20, 0),
	THREE_MONTH(DateTimeUtil.getBeforeTime("yyyy-MM-dd", 0, 3, 0), 0.20, 1),
	SIX_MONTH(DateTimeUtil.getBeforeTime("yyyy-MM-dd", 0, 6, 0), 0.20, 2),
	THIS_YEAR(DateTimeUtil.getBeforeTime("yyyy", 0) + "-01-01", 0.20, 3),
	ONE_YEAR(DateTimeUtil.getBeforeTime("yyyy-MM-dd", 1), 0.20, 4),
	TWO_YEAR(DateTimeUtil.getBeforeTime("yyyy-MM-dd", 2), 0.10, 5),
	THREE_YEAR(DateTimeUtil.getBeforeTime("yyyy-MM-dd", 3), 0.10, 6),
	FIVE_YEAR(DateTimeUtil.getBeforeTime("yyyy-MM-dd", 5), 0.10, 7);

	private String starTime;
	private double rate;
	private int index;
	private String endTime = initEndTime();

	private DateEnums(String startTime, double rate, int index) {
		this.starTime = startTime;
		this.rate = rate;
		this.index = index;
	}

	public String getEndTime() {
		return endTime;
	}

	public String getStarTime() {
		return starTime;
	}

	public double getRate() {
		return rate;
	}

	public int getIndex() {
		return index;
	}

	private String initEndTime() {
		String nowDate = DateTimeUtil.getBeforeTime("yyyy-MM-dd", 0);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(format.parse(nowDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int w = cal.get(Calendar.DAY_OF_WEEK);
		if (w == 1) {
			nowDate = DateTimeUtil.getBeforeTime("yyyy-MM-dd", 0, 0, 2);
		} else if (w == 7) {
			nowDate = DateTimeUtil.getBeforeTime("yyyy-MM-dd", 0, 0, 1);
		}
		return nowDate;
	}
}

enum FundTypeEnums {
	STOCK_TYPE("股票型", "2"),
	MIXTURE_TYPE("混合型", "3"),
	BOND_TYPE("债券", "4"),
	EXPONENT_TYPE("指数型", "5");

	private String name;
	private String subType;

	private FundTypeEnums(String name, String subType) {
		this.name = name;
		this.subType = subType;
	}

	public String getName() {
		return name;
	}

	public String getSubType() {
		return subType;
	}

}
