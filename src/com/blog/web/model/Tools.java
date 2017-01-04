package com.blog.web.model;

import com.blog.web.model.base.BaseModel;

/**
 * Tools entity. @author MyEclipse Persistence Tools
 */

public class Tools extends BaseModel {

	// Fields

	private Integer id;
	private String url;
	private String path;
	private String title;
	private String remark;
	private String logo;

	// Constructors

	/** default constructor */
	public Tools() {
	}


	// Property accessors

	public Tools(Integer id, String url, String path, String title,
			String remark, String logo) {
		super();
		this.id = id;
		this.url = url;
		this.path = path;
		this.title = title;
		this.remark = remark;
		this.logo = logo;
	}


	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getLogo() {
		return this.logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

}