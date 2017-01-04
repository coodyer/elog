package com.blog.web.cache;

import java.util.List;

import org.springframework.stereotype.Service;

import com.blog.web.annotation.CacheHandle;
import com.blog.web.annotation.DelCacheHandle;
import com.blog.web.base.cache.CacheFinal;
import com.blog.web.base.page.Pager;
import com.blog.web.cache.base.BaseCache;
import com.blog.web.model.Tools;

@Service
public class ToolsCache extends BaseCache {

	@SuppressWarnings("unchecked")
	@CacheHandle(key=CacheFinal.TOOLS_LIST_CACHE ,validTime=3660)
	public  List<Tools> loadUtils() {
		return (List<Tools>) baseService.load(Tools.class);
	}
	@SuppressWarnings("unchecked")
	@CacheHandle(key=CacheFinal.TOOLS_PAGER_CACHE ,validTime=700)
	public  Pager<Tools> loadUtilPager(Tools utils,Pager<Tools> pager) {
		return (Pager<Tools>) baseService.findPagerByObject(utils, pager);
	}
	@CacheHandle(key=CacheFinal.TOOLS_INFO_CACHE ,validTime=600)
	public  Tools getTools(Integer id){
		return (Tools) baseService.get(Tools.class, id);
	}
	@DelCacheHandle(keys={CacheFinal.TOOLS_INFO_CACHE,CacheFinal.TOOLS_PAGER_CACHE,CacheFinal.TOOLS_LIST_CACHE})
	public void save(Tools tool){
		baseService.saveOrUpdate(tool);
	}
	@DelCacheHandle(keys={CacheFinal.TOOLS_INFO_CACHE,CacheFinal.TOOLS_PAGER_CACHE,CacheFinal.TOOLS_LIST_CACHE})
	public void del(Tools tool){
		baseService.delete(tool);
	}
	@DelCacheHandle(keys={CacheFinal.TOOLS_INFO_CACHE,CacheFinal.TOOLS_PAGER_CACHE,CacheFinal.TOOLS_LIST_CACHE})
	public void del(Integer id){
		baseService.delete(Tools.class, id);
	}
}
