package com.blog.web.model;

import com.blog.web.model.base.BaseModel;

/**
 * Author entity. @author MyEclipse Persistence Tools
 */

public class Author extends BaseModel {

	// Fields

	private Integer id;
	private String blogName;
	private String autograph;
	private String nickName;
	private Integer age;
	private String work;
	private String place;
	private String email;
	private String logo;

	// Constructors

	/** default constructor */
	public Author() {
	}

	/** full constructor */
	public Author(String blogName, String autograph, String nickName,
			Integer age, String work, String place, String email, String logo) {
		this.blogName = blogName;
		this.autograph = autograph;
		this.nickName = nickName;
		this.age = age;
		this.work = work;
		this.place = place;
		this.email = email;
		this.logo = logo;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBlogName() {
		return this.blogName;
	}

	public void setBlogName(String blogName) {
		this.blogName = blogName;
	}

	public String getAutograph() {
		return this.autograph;
	}

	public void setAutograph(String autograph) {
		this.autograph = autograph;
	}

	public String getNickName() {
		return this.nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Integer getAge() {
		return this.age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getWork() {
		return this.work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getPlace() {
		return this.place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogo() {
		return this.logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

}