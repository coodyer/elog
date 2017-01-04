package com.blog.web.model;

import java.util.List;

import com.blog.web.model.base.BaseModel;

/**
 * Types entity. @author MyEclipse Persistence Tools
 */

public class Types extends BaseModel {

	// Fields

	private Integer id;
	private String className;
	private Integer parentClass;
	private List<Types> childTypes;
	private String otherUrl;
	public Types(Integer id, String className, Integer parentClass,
			String otherUrl) {
		super();
		this.id = id;
		this.className = className;
		this.parentClass = parentClass;
		this.otherUrl = otherUrl;
	}

	public String getOtherUrl() {
		return otherUrl;
	}

	public void setOtherUrl(String otherUrl) {
		this.otherUrl = otherUrl;
	}

	private Integer count;
	// Constructors

	

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	/** default constructor */
	public Types() {
	}

	public List<Types> getChildTypes() {
		return childTypes;
	}

	public void setChildTypes(List<Types> childTypes) {
		this.childTypes = childTypes;
	}



	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getClassName() {
		return this.className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Integer getParentClass() {
		return this.parentClass;
	}

	public void setParentClass(Integer parentClass) {
		this.parentClass = parentClass;
	}

}