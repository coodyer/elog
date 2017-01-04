package com.blog.web.model;

import com.blog.web.model.base.BaseModel;

/**
 * Links entity. @author MyEclipse Persistence Tools
 */

public class Links extends BaseModel {

	// Fields

	private Integer id;
	private String title;
	private String url;
	private Integer sort;

	// Constructors

	/** default constructor */
	public Links() {
	}

	/** full constructor */
	public Links(String title, String url, Integer sort) {
		this.title = title;
		this.url = url;
		this.sort = sort;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getSort() {
		return this.sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

}