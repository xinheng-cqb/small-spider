package com.small.crawler;

import com.small.crawler.util.document.CrawlParam;
import com.small.crawler.util.document.HttpURLConnectionFactory;

/**
 * @author xinheng-cqb
 * @date 2018年3月21日
 * @introduce:
 */
public class Test {
	public static void main(String[] args) {

		CrawlParam param = new CrawlParam(
				"https://mp.weixin.qq.com/s?__biz=MzU0MjA0MzMyNQ==&mid=2247486721&idx=1&sn=876d463b5f3d91ec17102e34008bb87a&chksm=fb21f0d3cc5679c506910629c35e4d26b7a019d5d6a96f005a88ecc26699a64b97d8dee46ba4&mpshare=1&scene=1&srcid=0410r8nZb73ScBl2AOWASNQm&key=43f95b290df2f2f81f951ee7d97da5bacccee63de46f7d4fd1f56d111a859095db27ad261e2533eb8a3958a8abf375fc1043707f50443dbac5b2163aacc84ffb68ae398b75fb3affcd4b732b201c40a9&ascene=1&uin=MjAyNzYwNzQzMw%3D%3D&devicetype=Windows+7&version=6206021b&lang=zh_CN&pass_ticket=80nQXnMLbLmgDWgZ0%2FIYhd9deOq9K0v8sFRpHsfdBR2iSqyn0GZ0x4nFRgyVuoPQ&winzoom=1");
		param.setUrlStr("https://mp.weixin.qq.com/mp/profile_ext?action=getmsg&__biz=MzU1NDA4NjU2MA==&offset=0&count=10&uin=MjAyNzYwNzQzMw%3D%3D&key=d378b8c059054ae594f3fae71fc3b15c61622b9580ace597c4aee60e8efc6513cdca237e90af0ce13410cbe4f277a25091810ec63824968f0ebc8afd1e88b1eb0112194a13fb18d526fb64e7249c8f65&pass_ticket=3jD5lQ6KEkc1HATO6POTfjmYrYh2YBYlS8ZzgUZoMASwiAEAK38IkpxocP1Um5ig&wxtoken=&appmsg_token=951_uQwSNIhYpGOnsdWTnrnOPTHHlD2E6cXCKcAkgg~~&x5=0&f=json");
		param.setOutputPath("E:\\job\\无法复制.html");
		// param.setRequestHeadInfo(
		// "User-Agent",
		// "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36 MicroMessenger/6.5.2.501 NetType/WIFI WindowsWechat QBCore/3.43.691.400 QQBrowser/9.0.2524.400");
		param.setCookie("rewardsn=; wxtokenkey=777; wxuin=2027607433; devicetype=Windows7; version=6206021b; lang=zh_CN; pass_ticket=3jD5lQ6KEkc1HATO6POTfjmYrYh2YBYlS8ZzgUZoMASwiAEAK38IkpxocP1Um5ig; wap_sid2=CImr68YHElxiOG5IbG83RG9WMUFaa2pmQlhvcnRSLVNCeW9BaURDYVRKekpXQmJKQV9LZTIyNlFjYlNGT3hja0VLbTZjWW5OMDVBSzR4WDBUa1VDdGJYZ2w3OGZ3cmNEQUFBfjDI/LXWBTgNQJVO");
		// HttpURLConnectionFactory.downloadFile(param);
		String documentStr = HttpURLConnectionFactory.getDocumentStr(param);
		documentStr = documentStr.replaceAll("data-src", "src");
		// documentStr = documentStr.replaceAll("\\\\\"", "\"");
		documentStr = documentStr.replaceAll("\\\\\\\\\\\\", "");
		System.out.println(documentStr);
	}

}
