package com.blog.web.cache;

import java.lang.reflect.Method;

import org.springframework.stereotype.Service;

import com.blog.web.annotation.CacheWipe;
import com.blog.web.annotation.CacheWrite;
import com.blog.web.base.cache.CacheFinal;
import com.blog.web.cache.base.BaseCache;
import com.blog.web.model.Author;
import com.blog.web.util.PropertUtil;

@Service
public class AuthorCache extends BaseCache {
	@CacheWrite(key=CacheFinal.AUTHOR_INFO ,validTime=3600)
	public  Author loadAuthor(){
		return (Author) baseService.loadFirst(Author.class);
	}
	@CacheWipe(key=CacheFinal.AUTHOR_INFO)
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
