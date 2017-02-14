package com.blog.web.cache;

import org.springframework.stereotype.Service;

import com.blog.web.annotation.CacheWipe;
import com.blog.web.annotation.CacheWrite;
import com.blog.web.base.cache.CacheFinal;
import com.blog.web.cache.base.BaseCache;
import com.blog.web.model.SiteMap;

@Service
public class SiteMapCache extends BaseCache {

	@CacheWrite(key=CacheFinal.SITE_MAP ,validTime=60)
	public  SiteMap loadSiteMap(){
		return (SiteMap) baseService.loadFirst(SiteMap.class);
	}
	@CacheWipe(key=CacheFinal.SITE_MAP,fields="siteMap.id")
	public void saveSiteMap(SiteMap siteMap){
		baseService.saveOrUpdate(siteMap);
	}
}
