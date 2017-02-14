package com.blog.web.cache;

import java.util.List;

import org.springframework.stereotype.Service;

import com.blog.web.annotation.CacheWipe;
import com.blog.web.annotation.CacheWrite;
import com.blog.web.base.cache.CacheFinal;
import com.blog.web.cache.base.BaseCache;
import com.blog.web.model.Links;
@Service
public class LinkCache extends BaseCache {
	@CacheWrite(key=CacheFinal.LINK_LIST ,validTime=3600)
	public  List<Links> loadLinks(){
		return (List<Links>) baseService.load(Links.class, "sort", false);
	}
	@CacheWrite(key=CacheFinal.LINK_LIST ,validTime=600)
	public  Links getLink(Integer id){
		return (Links) baseService.get(Links.class, id);
	}
	@CacheWipe(key=CacheFinal.LINK_LIST,isModel=true)
	public void saveLinks(Links link){
		baseService.saveOrUpdate(link);
	}
	
	@CacheWipe(key=CacheFinal.LINK_LIST,isModel=true)
	public void delLinks(Integer id){
		baseService.delete(Links.class, id);
	}
}
