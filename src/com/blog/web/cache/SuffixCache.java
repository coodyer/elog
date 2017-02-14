package com.blog.web.cache;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.blog.web.annotation.CacheWipe;
import com.blog.web.annotation.CacheWrite;
import com.blog.web.base.cache.CacheFinal;
import com.blog.web.cache.base.BaseCache;
import com.blog.web.model.Suffix;
import com.blog.web.service.SuffixService;

@Service
public class SuffixCache extends BaseCache {

	@Resource
	private SuffixService suffixService;
	@CacheWrite(key=CacheFinal.SITE_SUFFIX_KEY ,validTime=65)
	public List<Suffix> loadSuffix() {
		List<Suffix> list = suffixService.loadSuffix();// 查询数据库
		return list;
	}
	@CacheWrite(key=CacheFinal.SITE_TMP_SUFFIX_KEY ,validTime=3600)
	public Suffix getSuffix(Integer id) {
		Suffix suffix = suffixService.getSuffix(id);
		return suffix;
	}
	@CacheWipe(key=CacheFinal.SITE_TMP_SUFFIX_KEY,isModel=true)
	@CacheWipe(key=CacheFinal.SITE_SUFFIX_KEY,isModel=true)
	@CacheWipe(key=CacheFinal.SITE_DEF_SUFFIX_KEY,isModel=true)
	@CacheWipe(key=CacheFinal.SITE_AVA_SUFFIX_KEY,isModel=true)
	public void updateSuffix(Suffix suffix) {
		suffixService.updateSuffix(suffix);
	}
	@CacheWrite(key=CacheFinal.SITE_AVA_SUFFIX_KEY ,validTime=3600)
	public  List<String> loadAvaSuffix() {
		List<String> suffix = suffixService.loadAvaSuffix();
		if(suffix==null){
			suffix=new ArrayList<String>();
		}
		suffix.add("do");
		return suffix;
	}
	@CacheWrite(key=CacheFinal.SITE_DEF_SUFFIX_KEY ,validTime=3600)
	public  String loadDefSuffix() {
		String defSuffix = suffixService.loadDefSuffix();
		return defSuffix;
	}
	@CacheWrite(key=CacheFinal.SITE_STA_SUFFIX_KEY ,validTime=3600)
	public  List<String> loadStaSuffix() {
		List<String> list = suffixService.loadStaSuffix();
		return list;
	}
	@CacheWipe(key=CacheFinal.SITE_TMP_SUFFIX_KEY,isModel=true)
	@CacheWipe(key=CacheFinal.SITE_SUFFIX_KEY,isModel=true)
	@CacheWipe(key=CacheFinal.SITE_DEF_SUFFIX_KEY,isModel=true)
	@CacheWipe(key=CacheFinal.SITE_AVA_SUFFIX_KEY,isModel=true)
	public void updateSuffix(Integer[] suffix) {
		suffixService.updateSuffix(suffix);
	}
}
