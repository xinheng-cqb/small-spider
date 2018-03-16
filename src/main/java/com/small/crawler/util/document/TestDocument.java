package com.small.crawler.util.document;

import com.small.crawler.constants.DocumentUtilConstants;
import com.small.crawler.util.proxy.ProxyUtil;

public class TestDocument {
	public static void main(String[] args) {
		// JSESSIONID=lM8Rpjk1FQA5Xw1DLzJSDDA85k8wk-C-PhpbXtlHnPGeHmYI-wvp!804392984;
		/*HttpCrawlParam param = new HttpCrawlParam("http://epolicy.taikanglife.com/proEp/risks/queryRateOfRisk.action")
				.setRequestMethod(DocumentUtilConstants.POST_METHOD)
				.setRequestHeadInfo("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
				.setCookie(
						"JSESSIONID=FrQRXJiz1BTcnla3pwd9LfvuaEOXs_4tEkLskNTKvTkG28PVSdoT!804392984; BIGipServerepol_web_pool=222791434.23835.0000; BIGipServerepol_web_pool=222791434.23835.0000")
				.setPostParam("calSql=SELECT+RATE+FROM+EPOLICY.RT_CYRS+WHERE+AGE%3D30+AND+PAYENDYEAR%3D5");
		String documentStr = HttpURLConnectionFactory.getDocumentStr(param);
		System.out.println(documentStr);*/
		CrawlParam param1 = new CrawlParam(
				"http://epolicy.taikanglife.com/proEp/dividendQuery.action?dividendParam.getmodel=&dividendParam.getyear=&dividendParam.insuredage=31&dividendParam.insuyear=&dividendParam.livegetmode=&dividendParam.payendyear=5&dividendParam.riskcode=CYRS&dividendParam.sex=M")
				.setRequestMethod(DocumentUtilConstants.GET_METHOD)
				.setRequestHeadInfo("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
				.setCookie("JSESSIONID=lM8Rpjk1FQA5Xw1DLzJSDDA85k8wk-C-PhpbXtlHnPGeHmYI-wvp!804392984; BIGipServerepol_web_pool=222791434.23835.0000")
				.setUseProxy(true);
		String documentStr1 = HttpURLConnectionFactory.getDocumentStr(param1, ProxyUtil.INSTANCE.getProxy("183.131.19.233", 3128));
		System.out.println(documentStr1);
	}

}
