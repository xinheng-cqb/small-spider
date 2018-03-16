package com.small.crawler.entity;

import java.util.Date;

public class StockCommentInfo {
	private Integer id;
	private String code;
	private Integer count;
	private Integer replyMax;
	private Integer replyAllCount;
	private Integer favMax;
	private Integer favAllCount;
	private Double currentDayBenefit;
	private Double nextDayBenefit;
	private String date;
	private String source;
	private Date create_date;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getReplyMax() {
		return replyMax;
	}

	public void setReplyMax(Integer replyMax) {
		this.replyMax = replyMax;
	}

	public Integer getReplyAllCount() {
		return replyAllCount;
	}

	public void setReplyAllCount(Integer replyAllCount) {
		this.replyAllCount = replyAllCount;
	}

	public Integer getFavMax() {
		return favMax;
	}

	public void setFavMax(Integer favMax) {
		this.favMax = favMax;
	}

	public Integer getFavAllCount() {
		return favAllCount;
	}

	public void setFavAllCount(Integer favAllCount) {
		this.favAllCount = favAllCount;
	}

	public Double getCurrentDayBenefit() {
		return currentDayBenefit;
	}

	public void setCurrentDayBenefit(Double currentDayBenefit) {
		this.currentDayBenefit = currentDayBenefit;
	}

	public Double getNextDayBenefit() {
		return nextDayBenefit;
	}

	public void setNextDayBenefit(Double nextDayBenefit) {
		this.nextDayBenefit = nextDayBenefit;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

}
