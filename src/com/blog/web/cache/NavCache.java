package com.blog.web.cache;

import java.util.List;

import org.springframework.stereotype.Service;

import com.blog.web.annotation.CacheHandle;
import com.blog.web.annotation.DelCacheHandle;
import com.blog.web.base.cache.CacheFinal;
import com.blog.web.cache.base.BaseCache;
import com.blog.web.model.Nav;
@Service
public class NavCache extends BaseCache{
	@CacheHandle(key=CacheFinal.NAV_LIST ,validTime=3660)
	public  List<Nav> loadNavs() {
		List<Nav> list= (List<Nav>) baseService.findByObject(Nav.class, null, "sort", false);
		return list;
	}
	@CacheHandle(key=CacheFinal.NAV_INFO ,validTime=60)
	public  Nav getNav(Integer id) {
		return  (Nav) baseService.get(Nav.class, id);
	}
	@DelCacheHandle(keys={CacheFinal.NAV_INFO,CacheFinal.NAV_LIST})
	public void delNav(Integer id) {
		baseService.delete(Nav.class, id);
	}
	@DelCacheHandle(keys={CacheFinal.NAV_INFO,CacheFinal.NAV_LIST})
	public void saveNav(Nav nav) {
		baseService.saveOrUpdate(nav);
	}
}
