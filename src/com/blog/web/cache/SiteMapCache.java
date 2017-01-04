package com.blog.web.cache;

import org.springframework.stereotype.Service;

import com.blog.web.annotation.CacheHandle;
import com.blog.web.annotation.DelCacheHandle;
import com.blog.web.base.cache.CacheFinal;
import com.blog.web.cache.base.BaseCache;
import com.blog.web.model.SiteMap;

@Service
public class SiteMapCache extends BaseCache {

	@CacheHandle(key=CacheFinal.SITE_MAP ,validTime=60)
	public  SiteMap loadSiteMap(){
		return (SiteMap) baseService.loadFirst(SiteMap.class);
	}
	@DelCacheHandle(keys={CacheFinal.SITE_MAP})
	public void saveSiteMap(SiteMap siteMap){
		baseService.saveOrUpdate(siteMap);
	}
}
