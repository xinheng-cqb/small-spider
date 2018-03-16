package com.small.crawler.entity;

import java.util.Date;

public class StockInfo {
	private Integer id; // 自增ID
	private String code; // 股票编码
	private String turnrate; // 换手率
	private Double open; // 开盘价
	private Double high; // 当天最高价
	private Double low; // 当天最低价
	private Double close; // 收盘价
	private Double chg; // 涨跌额
	private String percent;// 涨跌幅
	private Long volume; // 成交量

	private String date; // 当天时间
	private Date createDate; // 入库时间

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

	public String getTurnrate() {
		return turnrate;
	}

	public void setTurnrate(String turnrate) {
		this.turnrate = turnrate;
	}

	public Double getOpen() {
		return open;
	}

	public void setOpen(Double open) {
		this.open = open;
	}

	public Double getHigh() {
		return high;
	}

	public void setHigh(Double high) {
		this.high = high;
	}

	public Double getLow() {
		return low;
	}

	public void setLow(Double low) {
		this.low = low;
	}

	public Double getClose() {
		return close;
	}

	public void setClose(Double close) {
		this.close = close;
	}

	public Double getChg() {
		return chg;
	}

	public void setChg(Double chg) {
		this.chg = chg;
	}

	public String getPercent() {
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}

	public Long getVolume() {
		return volume;
	}

	public void setVolume(Long volume) {
		this.volume = volume;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
