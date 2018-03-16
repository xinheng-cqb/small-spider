package com.small.crawler.util.proxy;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.small.crawler.util.CrawlerUtil;
import com.small.crawler.util.FileUtil;

/**
 * @author caiqibin
 * @date 2017年6月12日
 * @introduce:加载系统自带的代理IP，不适用于多线程 （不推荐使用）
 */
public enum ProxyUtil {

	INSTANCE;

	private final Log logger = LogFactory.getLog(ProxyUtil.class);
	public Integer size = 0;
	private int index = 0;
	private List<Proxy> proxyList;

	public synchronized Proxy getProxy() {
		init();
		if (this.index == this.proxyList.size()) {
			this.index = 0;
		}
		return proxyList.get(index++);
	}

	public Proxy getProxy(int i) {
		init();
		i = i < this.proxyList.size() ? i : 0;
		return this.proxyList.get(i);
	}

	public Proxy getProxy(String ip, int port) {
		InetAddress host;
		try {
			host = InetAddress.getByName(ip);
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
			return proxy;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

	public synchronized void removeProxy(Proxy proxy) {
		this.proxyList.remove(proxy);
		this.index = 0;
	}

	private void init() {
		if (this.proxyList == null || this.proxyList.size() == 0) {
			this.proxyList = getList();
			size = this.proxyList.size();
			logger.info("------init proxyList size ：" + size + "------------");
		}
	}

	private List<Proxy> getList() {
		List<Proxy> proxyList = new ArrayList<>();
		List<String> infoList = FileUtil.readFile(CrawlerUtil.getResourcePath("proxy.txt"));
		for (String info : infoList) {
			if (info.startsWith("#")) {
				continue;
			}
			try {
				String[] tempArray = info.split(":");
				InetAddress host = InetAddress.getByName(tempArray[0]);
				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, Integer.parseInt(tempArray[1])));
				proxyList.add(proxy);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return proxyList;
	}

}
