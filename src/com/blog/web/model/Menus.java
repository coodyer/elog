package com.blog.web.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.blog.web.model.base.BaseModel;

/**
 * Menus entity. @author MyEclipse Persistence Tools
 */

public class Menus extends BaseModel {

	// Fields

	private Integer id;
	private Menus menus;
	private String title;
	private String url;
	private Integer type;
	private Integer seq;
	private Set menuses = new HashSet(0);
	private List<Menus> childMenus;

	// Constructors

	/** default constructor */
	public Menus() {
	}

	public List<Menus> getChildMenus() {
		return childMenus;
	}

	public void setChildMenus(List<Menus> childMenus) {
		this.childMenus = childMenus;
	}

	/** full constructor */
	public Menus(Menus menus, String title, String url, Integer type,
			Integer seq, Set menuses) {
		this.menus = menus;
		this.title = title;
		this.url = url;
		this.type = type;
		this.seq = seq;
		this.menuses = menuses;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Menus getMenus() {
		return this.menus;
	}

	public void setMenus(Menus menus) {
		this.menus = menus;
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

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getSeq() {
		return this.seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public Set getMenuses() {
		return this.menuses;
	}

	public void setMenuses(Set menuses) {
		this.menuses = menuses;
	}

}