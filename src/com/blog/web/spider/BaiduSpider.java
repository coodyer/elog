package com.blog.web.spider;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.blog.web.base.page.Pager;
import com.blog.web.cache.SiteMapCache;
import com.blog.web.entity.HttpEntity;
import com.blog.web.model.Journal;
import com.blog.web.model.SiteMap;
import com.blog.web.service.JournalService;
import com.blog.web.util.DateUtils;
import com.blog.web.util.HttpUtil;
import com.blog.web.util.StringUtils;

@Service
public class BaiduSpider {

	@Resource
	SiteMapCache siteMapCache;
	@Resource
	JournalService journalService;
	
	
	@SuppressWarnings("unchecked")
	public void parsSiteMap() {
		try {
			SiteMap siteMap = siteMapCache.loadSiteMap();
			
			siteMap.setLastJournalId(siteMap.getLastJournalId()==null?0:siteMap.getLastJournalId());
			siteMap.setLastPage(siteMap.getLastPage()==null?1:siteMap.getLastPage());
			Pager<Journal> pag = new Pager<Journal>(400, siteMap.getLastPage());
			pag = journalService.getSiteMapList(pag);
			siteMap.setLastPage(siteMap.getLastPage()+1);
			List<Journal> list = (List<Journal>) pag.getPageData();
			if(StringUtils.isNullOrEmpty(list)){
				siteMap.setLastJournalId(0);
				siteMap.setLastPage(1);
				siteMapCache.saveSiteMap(siteMap);
				return;
			}
			StringBuffer sbXml = new StringBuffer(
					"<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			StringBuffer sbHtml = new StringBuffer("");
			StringBuilder sbPost=new StringBuilder("");
			sbXml.append("<urlset>");
			for (Journal tmp : list) {
				if (tmp != null && tmp.getId() > 0) {
					String url=MessageFormat.format("{0}/article_{1}.html", siteMap.getDomain(),String.valueOf(tmp.getId()));
					sbXml.append("\r\n").append("<url>");
					sbXml.append("\r\n").append("<loc>")
							.append(url)
							.append("</loc>");
					sbXml.append("\r\n").append("<priority>1.0</priority>");
					sbXml.append("\r\n")
							.append("<lastmod>")
							.append(DateUtils.dateToString(new Date(),
									DateUtils.ISO_EXPANDED_DATE_FORMAT))
							.append("</lastmod>");
					sbXml.append("\r\n<changefreq>daily</changefreq>");
					sbXml.append("\r\n").append("</url>");
					sbHtml.append("<li>");
					sbHtml.append("<a href=\"");
					sbHtml.append(url);
					sbHtml.append("\" title=\"");
					sbHtml.append(tmp.getTitle());
					sbHtml.append("\" target=\"_blank\">");
					sbHtml.append(tmp.getTitle());
					sbHtml.append("</a></li>");
					sbPost.append(url).append("\r\n");
				}
				siteMap.setLastJournalId(tmp.getId());
			}
			sbXml.append("\r\n").append("</urlset>");
			siteMap.setUpdateTime(new Date());
			siteMap.setHtml(sbHtml.toString());
			siteMap.setXml(sbXml.toString());
			if(list.size()<400){
				siteMap.setLastJournalId(0);
				siteMap.setLastPage(1);
			}
			siteMapCache.saveSiteMap(siteMap);
			postBaidu(siteMap.getBaiduUrl(), sbPost.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void postBaidu(String url,String postData){
		try {
			HttpEntity entity=HttpUtil.Post(url, "http://www.54sb.org/sitemap.xml");
			System.out.println("百度任务推送完成:"+entity.getHtml());
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			HttpEntity entity=HttpUtil.Post(url, postData);
			System.out.println("百度任务推送完成:"+entity.getHtml());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public static void main(String[] args) {
		new BaiduSpider().postBaidu("http://data.zz.baidu.com/urls?site=blog.51duobei.com&token=oEQbKp1WVuJYlu00", "http://blog.51duobei.com/article_9722.html");
	}
}
