package com.blog.web.cache;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.blog.web.annotation.CacheWipe;
import com.blog.web.annotation.CacheWrite;
import com.blog.web.base.cache.CacheFinal;
import com.blog.web.cache.base.BaseCache;
import com.blog.web.model.Setting;
import com.blog.web.service.SettingService;

@Service
public class SettingCache extends BaseCache {
	@Resource
	SettingService settingService;
	@CacheWrite(key=CacheFinal.SETTING_KEY ,validTime=3600)
	public  Setting loadSetting() {
		Setting setting = (Setting) settingService.loadFirst(Setting.class);
		return setting;
	}
	@CacheWipe(key=CacheFinal.SETTING_KEY,isModel=true)
	public void saveSetting(Setting setting) {
		settingService.saveOrUpdate(setting);
	}
}
