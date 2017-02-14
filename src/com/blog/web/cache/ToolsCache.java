package com.blog.web.cache;

import java.util.List;

import org.springframework.stereotype.Service;

import com.blog.web.annotation.CacheWipe;
import com.blog.web.annotation.CacheWrite;
import com.blog.web.base.cache.CacheFinal;
import com.blog.web.base.page.Pager;
import com.blog.web.cache.base.BaseCache;
import com.blog.web.model.Tools;

@Service
public class ToolsCache extends BaseCache {

	@SuppressWarnings("unchecked")
	@CacheWrite(key=CacheFinal.TOOLS_LIST_CACHE ,validTime=3660)
	public  List<Tools> loadUtils() {
		return (List<Tools>) baseService.load(Tools.class);
	}
	@SuppressWarnings("unchecked")
	@CacheWrite(key=CacheFinal.TOOLS_PAGER_CACHE ,validTime=700)
	public  Pager<Tools> loadUtilPager(Tools utils,Pager<Tools> pager) {
		return (Pager<Tools>) baseService.findPagerByObject(utils, pager);
	}
	@CacheWrite(key=CacheFinal.TOOLS_INFO_CACHE ,validTime=600)
	public  Tools getTools(Integer id){
		return (Tools) baseService.get(Tools.class, id);
	}
	@CacheWipe(key=CacheFinal.TOOLS_INFO_CACHE,isModel=true)
	@CacheWipe(key=CacheFinal.TOOLS_PAGER_CACHE,isModel=true)
	@CacheWipe(key=CacheFinal.TOOLS_PAGER_CACHE,isModel=true)
	public void save(Tools tool){
		baseService.saveOrUpdate(tool);
	}
	@CacheWipe(key=CacheFinal.TOOLS_INFO_CACHE,isModel=true)
	@CacheWipe(key=CacheFinal.TOOLS_PAGER_CACHE,isModel=true)
	@CacheWipe(key=CacheFinal.TOOLS_PAGER_CACHE,isModel=true)
	public void del(Tools tool){
		baseService.delete(tool);
	}
	@CacheWipe(key=CacheFinal.TOOLS_INFO_CACHE,isModel=true)
	@CacheWipe(key=CacheFinal.TOOLS_PAGER_CACHE,isModel=true)
	@CacheWipe(key=CacheFinal.TOOLS_PAGER_CACHE,isModel=true)
	public void del(Integer id){
		baseService.delete(Tools.class, id);
	}
}
