package com.blog.web.model;

import com.blog.web.model.base.BaseModel;

/**
 * Nav entity. @author MyEclipse Persistence Tools
 */

public class Nav extends BaseModel {

	// Fields

	private Integer id;
	private String title;
	private String url;
	private Integer sort;

	// Constructors

	/** default constructor */
	public Nav() {
	}

	/** full constructor */
	public Nav(String title, String url, Integer sort) {
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