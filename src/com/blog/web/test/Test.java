package com.blog.web.test;

import com.blog.web.model.Journal;
import com.blog.web.util.ReqJsonUtil;


public class Test{
	public static void main(String[] args) {
		for (int i = 0; i <100; i++) {
			Journal journal=new Journal();
			journal.setAuthor("admin");
			journal.setContext("context");
			journal.setId(100);
			journal.setLogo("logo");
			journal.setOpenPwd("123456");
			System.out.println(ReqJsonUtil.objectToJson(journal));
		}
	}
}
