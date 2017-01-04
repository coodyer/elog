package com.blog.web.model;

import com.blog.web.model.base.BaseModel;

/**
 * Shell entity. @author MyEclipse Persistence Tools
 */

public class Shell extends BaseModel {

	// Fields

	private Integer id;
	private String url;
	private Integer errNum;

	public Shell(Integer id, String url, Integer errNum) {
		super();
		this.id = id;
		this.url = url;
		this.errNum = errNum;
	}

	// Constructors

	public Integer getErrNum() {
		return errNum;
	}

	public void setErrNum(Integer errNum) {
		this.errNum = errNum;
	}

	/** default constructor */
	public Shell() {
	}


	// Property accessors

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

}