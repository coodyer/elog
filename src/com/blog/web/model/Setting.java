package com.blog.web.model;

import com.blog.web.model.base.BaseModel;

/**
 * 设置 Setting entity. @author MyEclipse Persistence Tools
 */

public class Setting extends BaseModel {

	// Fields

	private Integer id;
	private String siteName;
	private String keywords;
	private String description;
	private String copyright;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCopyright() {
		return copyright;
	}
	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}
	public Setting(Integer id, String siteName, String keywords,
			String description, String copyright) {
		super();
		this.id = id;
		this.siteName = siteName;
		this.keywords = keywords;
		this.description = description;
		this.copyright = copyright;
	}
	public Setting(){
	}

}