package com.blog.web.model;

import java.util.Date;

import com.blog.web.model.base.BaseModel;

/**
 * Journal entity. @author MyEclipse Persistence Tools
 */

public class Journal extends BaseModel {

	// Fields

	private Integer id;
	private Types types;
	private String title;
	private String context;
	private Integer views;
	private String author;
	private Date time;
	private Integer openLevel;
	private String openPwd;
	private String logo;
	private Integer typeId;
	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	// Constructors

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	/** default constructor */
	public Journal() {
	}


	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Types getTypes() {
		return this.types;
	}

	public void setTypes(Types types) {
		this.types = types;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContext() {
		return this.context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public Integer getViews() {
		return this.views;
	}

	public void setViews(Integer views) {
		this.views = views;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getTime() {
		return this.time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Integer getOpenLevel() {
		return this.openLevel;
	}

	public void setOpenLevel(Integer openLevel) {
		this.openLevel = openLevel;
	}

	public String getOpenPwd() {
		return this.openPwd;
	}

	public void setOpenPwd(String openPwd) {
		this.openPwd = openPwd;
	}

	public Journal(Integer id, Types types, String title, String context,
			Integer views, String author, Date time, Integer openLevel,
			String openPwd, String logo, Integer typeId) {
		super();
		this.id = id;
		this.types = types;
		this.title = title;
		this.context = context;
		this.views = views;
		this.author = author;
		this.time = time;
		this.openLevel = openLevel;
		this.openPwd = openPwd;
		this.logo = logo;
		this.typeId = typeId;
	}

}