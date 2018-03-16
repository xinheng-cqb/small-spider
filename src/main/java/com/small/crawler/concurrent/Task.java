package com.small.crawler.concurrent;

import com.small.crawler.constants.ConcurrentConstants;

/**
 * @author caiqibin
 * @date 2017年6月18日
 * @introduce:提交的任务
 */
public class Task {

	// 任务状态
	protected String status = ConcurrentConstants.PREPARE_STATUS;
	protected String name;

	public String getStatus() {
		return status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
