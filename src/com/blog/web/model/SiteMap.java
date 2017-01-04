package com.blog.web.model;


import java.util.Date;

import com.blog.web.model.base.BaseModel;

/**
 * SiteMap entity. @author MyEclipse Persistence Tools
 */

public class SiteMap extends BaseModel {

	// Fields

	private Integer id;
	private String html;
	private String xml;
	private Integer lastJournalId;
	private String domain;
	private Date updateTime;
	private String baiduUrl;
	private Integer lastPage;
	// Constructors

	public SiteMap(Integer id, String html, String xml, Integer lastJournalId,
			String domain, Date updateTime, String baiduUrl, Integer lastPage) {
		super();
		this.id = id;
		this.html = html;
		this.xml = xml;
		this.lastJournalId = lastJournalId;
		this.domain = domain;
		this.updateTime = updateTime;
		this.baiduUrl = baiduUrl;
		this.lastPage = lastPage;
	}

	public Integer getLastPage() {
		return lastPage;
	}

	public void setLastPage(Integer lastPage) {
		this.lastPage = lastPage;
	}

	/** default constructor */
	public SiteMap() {
	}

	/** full constructor */
	public SiteMap(String html, String xml, Integer lastJournalId,
			String domain, Date updateTime, String baiduUrl) {
		this.html = html;
		this.xml = xml;
		this.lastJournalId = lastJournalId;
		this.domain = domain;
		this.updateTime = updateTime;
		this.baiduUrl = baiduUrl;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getHtml() {
		return this.html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getXml() {
		return this.xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public Integer getLastJournalId() {
		return this.lastJournalId;
	}

	public void setLastJournalId(Integer lastJournalId) {
		this.lastJournalId = lastJournalId;
	}

	public String getDomain() {
		return this.domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getBaiduUrl() {
		return this.baiduUrl;
	}

	public void setBaiduUrl(String baiduUrl) {
		this.baiduUrl = baiduUrl;
	}

}