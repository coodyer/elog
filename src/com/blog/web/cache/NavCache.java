package com.blog.web.cache;

import java.util.List;

import org.springframework.stereotype.Service;

import com.blog.web.annotation.CacheWipe;
import com.blog.web.annotation.CacheWrite;
import com.blog.web.base.cache.CacheFinal;
import com.blog.web.cache.base.BaseCache;
import com.blog.web.model.Nav;
@Service
public class NavCache extends BaseCache{
	@CacheWrite(key=CacheFinal.NAV_LIST ,validTime=3660)
	public  List<Nav> loadNavs() {
		List<Nav> list= (List<Nav>) baseService.findByObject(Nav.class, null, "sort", false);
		return list;
	}
	@CacheWrite(key=CacheFinal.NAV_INFO ,validTime=60)
	public  Nav getNav(Integer id) {
		return  (Nav) baseService.get(Nav.class, id);
	}
	@CacheWipe(key=CacheFinal.NAV_INFO,isModel=true)
	@CacheWipe(key=CacheFinal.NAV_LIST,isModel=true)
	public void delNav(Integer id) {
		baseService.delete(Nav.class, id);
	}
	@CacheWipe(key=CacheFinal.NAV_INFO,isModel=true)
	@CacheWipe(key=CacheFinal.NAV_LIST,isModel=true)
	public void saveNav(Nav nav) {
		baseService.saveOrUpdate(nav);
	}
}
