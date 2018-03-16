package com.small.crawler.util.proxy;

/**
 * @author caiqibin
 * @date 2017年7月21日
 * @introduce: 实现了爬取的代理网站信息配置枚举
 */
public enum UseableProxyWebsiteEnum {

	SSIP("66IP网", "http://www.66ip.cn/areaindex_11/", new Web66Ip()),
	QUICKPROXY("快代理", "http://www.kuaidaili.com/free/inha/", new WebQuickProxy()),
	XICIPROXY("西刺代理", "http://www.xicidaili.com/nn/", new WebXiCiProxy());
	private String name;
	private String indexUrl;
	private ProxyCrawlBase proxyCrawlBase;

	private UseableProxyWebsiteEnum(String name, String indexUrl, ProxyCrawlBase proxyCrawlBase) {
		this.name = name;
		this.indexUrl = indexUrl;
		this.proxyCrawlBase = proxyCrawlBase;
	}

	public String getName() {
		return name;
	}

	public String getIndexUrl() {
		return indexUrl;
	}

	public ProxyCrawlBase getProxyCrawlBase() {
		return proxyCrawlBase;
	}

}
