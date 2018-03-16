package com.small.crawler.entity;

/**
 * @author caiqibin
 * @date 2017年9月15日
 * @introduce: 身份证对应地区实体
 */
public class AreaInfo {
	private int id;
	private String areaValue;
	private int level;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getArea_value() {
		return areaValue;
	}

	public void setArea_value(String areaValue) {
		this.areaValue = areaValue;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
