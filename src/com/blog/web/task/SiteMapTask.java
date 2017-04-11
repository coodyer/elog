package com.blog.web.task;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.blog.web.spider.BaiduSpider;

@Service
public class SiteMapTask {
	public static boolean siteMapTaskIsRun = false;

	@Resource
	BaiduSpider baiduSpider;

	public void excuteTask() {
		if (siteMapTaskIsRun) {
			return;
		}
		siteMapTaskIsRun = true;
		try {
			baiduSpider.parsSiteMap();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
		siteMapTaskIsRun = false;
		}
		
	}

	
}
