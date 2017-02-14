package com.blog.web.cache.base;

import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.blog.web.annotation.CacheWrite;
import com.blog.web.base.cache.CacheFinal;
import com.blog.web.base.cache.CacheTimerHandler;
import com.blog.web.entity.IpAddressEntity;
import com.blog.web.service.base.BaseService;
import com.blog.web.util.IpAddressUtil;

@Service
public class BaseCache {

	@Resource
	protected BaseService baseService;

	/**
	 * 增加缓存对象
	 * 
	 * @param key
	 * @param ce
	 * @param validityTime
	 *            有效时间
	 */
	public static synchronized void addCache(String key, Object ce,
			int validityTime) {
		try {
			CacheTimerHandler.addCache(key, ce, validityTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static synchronized void addCache(String key, Object ce) {
		try {
			CacheTimerHandler.addCache(key, ce);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取缓存对象
	 * 
	 * @param key
	 * @return
	 */
	public static synchronized Object getCache(String key) {
		try {
			return CacheTimerHandler.getCache(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	/**
	 * 获取缓存key列表
	 * 
	 * @param key
	 * @return
	 */
	public static  Set<String> getCacheKeys() {
		return CacheTimerHandler.getCacheKeys();
	}
	/**
	 * 检查是否含有制定key的缓冲
	 * 
	 * @param key
	 * @return
	 */
	public static synchronized boolean contains(String key) {
		try {
			return CacheTimerHandler.contains(key);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 删除缓存
	 * 
	 * @param key
	 */
	public static synchronized void removeCache(String key) {
		try {
			CacheTimerHandler.removeCache(key);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 删除缓存
	 * 
	 * @param key
	 */
	public static synchronized void removeCacheFuzzy(String key) {
		try {
			CacheTimerHandler.removeCacheFuzzy(key);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 获取缓存大小
	 * 
	 * @param key
	 */
	public static int getCacheSize() {
		try {
			return CacheTimerHandler.getCacheSize();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

	}

	/**
	 * 清除全部缓存
	 */
	public static synchronized void clearCache() {
		try {
			CacheTimerHandler.clearCache();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	@CacheWrite(key=CacheFinal.IP_ADDRESS_INFO ,validTime=600,fields="ip")
	public static IpAddressEntity.AddressInfo getIpAddress(String ip) {
		IpAddressEntity.AddressInfo address=IpAddressUtil.getAddress(ip);
		return address;
	}

}
