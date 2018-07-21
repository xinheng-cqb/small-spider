package com.small.crawler.util.document;

import com.small.crawler.util.CrawlerUtil;

/**
 * @author xinheng-cqb
 * @date 2018年5月8日
 * @introduce: 通过代理精灵获取可用的代理，每个代理可用时长为1-5分钟，可重复获取，每天有获取限制，没有结果时返回的是null,线程不安全，不可用于多线程
 */
public class ProxySpirit {

	private static final String ORIGIN_URL = "http://ip.11jsq.com/index.php/api/entry?method=proxyServer.generate_api_url&packid=1&fa=0&qty=1&time=1&port=1&format=txt&ss=1&dt=1";

	// 多个ip 请用英文逗号隔开
	private static final String ADD_WHITE_LIST_URL = "http://http.zhiliandaili.com/Users-whiteIpAddNew.html?appid=416&appkey=598464ef402f438bf0068a8fee668aee&whiteip=";

	/**
	 * @introduce: 获取可用的代理，有结果用ip:port形式返回，没有结果返回null
	 * @return String
	 */
	public static String getUsefulProxy() {
		CrawlParam param = new CrawlParam(ORIGIN_URL);
		param.setInterval(1000);

		String documentStr = HttpURLConnectionFactory.getDocumentStr(param);
		if (documentStr == null) {
			return null;
		}
		if (documentStr.contains("已用完") || documentStr.contains("有效IP数量不够")) {
			return null;
		} else if (documentStr.contains("不是白名单IP")) {
			String ip = CrawlerUtil.matchNumber(documentStr, "\"msg\":\"");
			param.setUrlStr(ADD_WHITE_LIST_URL + ip);
			HttpURLConnectionFactory.getDocumentStr(param);
			return getUsefulProxy();
		} else if (documentStr.contains("当前用户不允许获取")) {
			String cookie = HttpClientFactory.simulateLoginAcquireCookie("http://http.zhiliandaili.com/Users-login.html", "username", "data_xinheng",
					"password", "data410");
			if (cookie == null) {
				return null;
			}
			CrawlParam c = new CrawlParam("http://http.zhiliandaili.com/Index-getFree.html");
			c.setRequestMethod("POST");
			c.setPostParam("");
			c.setCookie(cookie);
			HttpURLConnectionFactory.getDocumentStr(c);
			return getUsefulProxy();
		}
		return documentStr;
	}
}
