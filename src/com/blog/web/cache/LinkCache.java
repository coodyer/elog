package com.blog.web.cache;

import java.util.List;

import org.springframework.stereotype.Service;

import com.blog.web.annotation.CacheHandle;
import com.blog.web.annotation.DelCacheHandle;
import com.blog.web.base.cache.CacheFinal;
import com.blog.web.cache.base.BaseCache;
import com.blog.web.model.Links;
@Service
public class LinkCache extends BaseCache {
	@CacheHandle(key=CacheFinal.LINK_LIST ,validTime=3600)
	public  List<Links> loadLinks(){
		return (List<Links>) baseService.load(Links.class, "sort", false);
	}
	@CacheHandle(key=CacheFinal.LINK_LIST ,validTime=600)
	public  Links getLink(Integer id){
		return (Links) baseService.get(Links.class, id);
	}
	@DelCacheHandle(keys={CacheFinal.LINK_LIST})
	public void saveLinks(Links link){
		baseService.saveOrUpdate(link);
	}
	
	@DelCacheHandle(keys={CacheFinal.LINK_LIST})
	public void delLinks(Integer id){
		baseService.delete(Links.class, id);
	}
}
