package com.small.crawler;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.small.crawler.util.CrawlerUtil;
import com.small.crawler.util.DateTimeUtil;
import com.small.crawler.util.document.CrawlParam;
import com.small.crawler.util.document.HttpURLConnectionFactory;

/**
 * @author xinheng-cqb
 * @date 2018年3月21日
 * @introduce:
 */
public class Test {
	public static void main(String[] args) {
		// testCrawl();
		String[] bizArray = { "MzI4NDY5Mjc1Mg==", "MjM5ODI5Njc2MA==", "MzI0MTg1MTQ2MQ==", "MjM5MTQzNzU2NA==", "MjM5MTM0NjQ2MQ==", "MzI0MjQxNjAyOQ==" };

		CrawlParam param = new CrawlParam(
				"https://mp.weixin.qq.com/s?__biz=MzU0MjA0MzMyNQ==&mid=2247486721&idx=1&sn=876d463b5f3d91ec17102e34008bb87a&chksm=fb21f0d3cc5679c506910629c35e4d26b7a019d5d6a96f005a88ecc26699a64b97d8dee46ba4&mpshare=1&scene=1&srcid=0410r8nZb73ScBl2AOWASNQm&key=43f95b290df2f2f81f951ee7d97da5bacccee63de46f7d4fd1f56d111a859095db27ad261e2533eb8a3958a8abf375fc1043707f50443dbac5b2163aacc84ffb68ae398b75fb3affcd4b732b201c40a9&ascene=1&uin=MjAyNzYwNzQzMw%3D%3D&devicetype=Windows+7&version=6206021b&lang=zh_CN&pass_ticket=80nQXnMLbLmgDWgZ0%2FIYhd9deOq9K0v8sFRpHsfdBR2iSqyn0GZ0x4nFRgyVuoPQ&winzoom=1");

		param.setCookie("rewardsn=; wxtokenkey=777; wxuin=2027607433; devicetype=Windows7; version=6206021b; lang=zh_CN; pass_ticket=StvOwx5Hk/HAOV2Xpd+PcglrT+RqAGscZm1bzveS45oyjY5XYMYyt2CiybndRWps; wap_sid2=CImr68YHEogBaTZJY1JoeWxfWHJNSFhzYlc1QTJ3aGdBNTNoOVdqMGtrUkt6TU9PVUkyaTFveUlNQm1NaThNd1BKVEZjY0h3QWtlbm92dEpQa2x2dUh4R0IzQm1wUzlXbnM4bDJTaElTYmppS2FTRlNKUFBlQWQxTEtobUFldDJJbHI0b0IzcFd1UU1BQUF+fjC+gOLWBTgNQJVO");
		// param.setUrlStr("https://mp.weixin.qq.com/mp/profile_ext?action=getmsg&__biz=MjM5MTQzNzU2NA==&offset=10&count=10&uin=MjAyNzYwNzQzMw%3D%3D&key=09dff48ab78aa96a5c23233e6d56fc5361098574d6d118aaaf3ee4a2ce49cac6ac7e17eb548724e32e6fc3923183c2cf69d28d2d96e971adae3c47fb429c6fbd04f8325f1436180859d7bfdb385dd75b&pass_ticket=i6KK0kOZEcma02RjPwlnAsU%2FVrgm9mblU1zMaNtodN88Dhsmy5iKscc%2FDClCKZke&wxtoken=&appmsg_token=953_ZbIKqV4eE79yJem2P79UqQmMehwHIBNNapjyBg~~&x5=0&f=json");
		// param.setCookie("tvfe_boss_uuid=d46683da1df2dff9; RK=mOmLx7Tf8T; mobileUV=1_15b8b3a6c5e_50bfa; pgv_pvi=2301257728; sd_userid=69631497836054835; sd_cookie_crttime=1497836054835; pac_uid=1_3182890960; pgv_pvid_new=3182890960_5904688b7; pgv_pvid=5906358160; ua_id=mbSwoF8vAubEFvdmAAAAABrdvNl-L4RpQbAVJFIL5QA=; mm_lang=zh_CN; ptcz=3ad5fcf882c2f318587247744679c4cefc28e3e2a63ed7e55701c79d4df92f13; xid=0e02530b0b88a3627cf5858fcaa845ea; ue_ts=1515841624; ue_uk=e7b64c2bf1c52b2baf4f71e40d723eda; ue_uid=adb26b372fe3122b5f5920039ea5615a; ue_skey=c079565033f8567d1dd68bfc850ae2e4; LW_pid=8bdd74a85270a2f5f0a366093ebcbb36; LW_sid=m1K5X1W8q177w9k2i3v7i8k6L3; ptui_loginuin=3368350076; o_cookie=3182890960; ptisp=ctc; pt2gguin=o2543112547; uin=o2543112547; pgv_info=ssid=s2052488082; rewardsn=; wxtokenkey=777; wxuin=2027607433; devicetype=Windows7; version=6206021b; lang=zh_CN; skey=@fY4vOLqdm; pgv_si=s5557598208; sig=h01ea7748f7f1486b36732af60803b56a461c13a5e229d59d0e714bbe40340f08dbe3644935d0763fbf; pass_ticket=i6KK0kOZEcma02RjPwlnAsU/Vrgm9mblU1zMaNtodN88Dhsmy5iKscc/DClCKZke; wap_sid2=CImr68YHElxkZmdiVEZRa2xhRkVqcnowTmktckRxSEcweGFIR1ZpWDIxRDk3eEpyWURIdGkxSklvYmR0a29MYm5NN2FMTzdjN1YyVVRqczRMMHduMk9hdkYzbFRhN2tEQUFBfjDX7tvWBTgNQJVO");
		param.setRequestHeadInfo(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36 MicroMessenger/6.5.2.501 NetType/WIFI WindowsWechat QBCore/3.43.691.400 QQBrowser/9.0.2524.400");
		// HttpURLConnectionFactory.downloadFile(param);
		for (int i = 1; i < 1000; i++) {
			if (i % 50 == 0) {
				try {
					TimeUnit.SECONDS.sleep(60);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			String biz = bizArray[i % bizArray.length];
			param.setUrlStr("https://mp.weixin.qq.com/mp/profile_ext?action=home&__biz=" + biz + "#wechat_redirect");
			String documentStr = HttpURLConnectionFactory.getDocumentStr(param);
			documentStr = documentStr.replaceAll("data-src", "src");
			// documentStr = documentStr.replaceAll("\\\\\"", "\"");
			// documentStr = documentStr.replaceAll("\\\\\\\\\\\\", "");
			System.out.println(MessageFormat.format("这是第> {0} <篇，公众号为{1}", i,
					CrawlerUtil.matchBetweenSymbol(documentStr, "id=\"nickname\">", "</strong")));
		}
	}

	/**
	 * @introduce: 获取微信公众号文章
	 * @return void
	 */
	public static void testCrawl() {
		String timeFormat = "yyyy-MM-dd";
		String todayStr = DateTimeUtil.getTime(timeFormat, 0);
		CrawlParam param = new CrawlParam("https://mp.weixin.qq.com/mp/profile_ext?action=home&__biz=MzI4NDY5Mjc1Mg==#wechat_redirect");
		// param.setOutputPath("E:\\job\\1.html");
		// HttpURLConnectionFactory.downloadFile(param);
		String documentStr = HttpURLConnectionFactory.getDocumentStr(param);
		String url = CrawlerUtil.matchBetweenSymbol(documentStr, "account_name_0\" href=\"", "\"");
		url = url.replaceAll("amp;", "");
		System.out.println("大数据文摘列表页对应的url是：" + url);
		param.setUrlStr(url);
		documentStr = HttpURLConnectionFactory.getDocumentStr(param);
		documentStr = documentStr.replaceAll("amp;", "");
		String jsonMsg = CrawlerUtil.matchBetweenSymbol(documentStr, "msgList = ", ";");
		JSONObject jsStr = JSONObject.fromObject(jsonMsg);
		JSONArray jsArray = jsStr.getJSONArray("list");
		for (int i = 0; i < jsArray.size(); i++) {
			JSONObject tempObj = (JSONObject) jsArray.get(i);
			Long datetimeStr = tempObj.getJSONObject("comm_msg_info").getLong("datetime") * 1000;
			String date = DateTimeUtil.parseMillis2Time(datetimeStr, timeFormat);
			// if (date.equals(todayStr)) {
			JSONObject articleInfoObj = tempObj.getJSONObject("app_msg_ext_info");
			String contentUrl = "https://mp.weixin.qq.com" + articleInfoObj.getString("content_url");
			System.out.println(MessageFormat.format("{0}: {1}", date, contentUrl));
			int isMulti = articleInfoObj.getInt("is_multi");
			if (isMulti == 1) {
				JSONArray multiArticleArray = articleInfoObj.getJSONArray("multi_app_msg_item_list");
				for (int j = 0; j < multiArticleArray.size(); j++) {
					JSONObject articleObj = (JSONObject) multiArticleArray.get(j);
					contentUrl = "https://mp.weixin.qq.com" + articleObj.getString("content_url");
					System.out.println(MessageFormat.format("{0}: {1}", date, contentUrl));
				}
			}
		}
		// }
	}

}
