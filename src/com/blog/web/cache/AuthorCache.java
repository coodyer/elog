package com.blog.web.cache;

import java.lang.reflect.Method;

import org.springframework.stereotype.Service;

import com.blog.web.annotation.CacheHandle;
import com.blog.web.annotation.DelCacheHandle;
import com.blog.web.base.cache.CacheFinal;
import com.blog.web.cache.base.BaseCache;
import com.blog.web.model.Author;
import com.blog.web.util.PropertUtil;

@Service
public class AuthorCache extends BaseCache {
	@CacheHandle(key=CacheFinal.AUTHOR_INFO ,validTime=3600)
	public  Author loadAuthor(){
		return (Author) baseService.loadFirst(Author.class);
	}
	@DelCacheHandle(keys={CacheFinal.AUTHOR_INFO })
	public  void saveAuthor(Author author){
		baseService.saveOrUpdate(author);
	}
	
	public static void main(String[] args) {
		Method [] methods=AuthorCache.class.getDeclaredMethods();
		Method method=methods[0];
		Class cla=(Class) PropertUtil.getFieldValue(method, "clazz");
		System.out.println(cla.getName());
	}
}
