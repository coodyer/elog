package com.blog.web.cache;

import java.util.List;

import org.springframework.stereotype.Service;

import com.blog.web.annotation.CacheHandle;
import com.blog.web.annotation.DelCacheHandle;
import com.blog.web.base.cache.CacheFinal;
import com.blog.web.cache.base.BaseCache;
import com.blog.web.model.Role;
import com.blog.web.util.StringUtils;

@Service
public class RoleCache extends BaseCache {
	@CacheHandle(key=CacheFinal.ROLE_INFO_KEY ,validTime=65)
	public Role loadRole(Integer roleId) {
		Role roles = (Role) baseService.get(Role.class, roleId);
		return roles;
	}
	@DelCacheHandle(keys={CacheFinal.ROLE_INFO_KEY,CacheFinal.ROLE_LIST_KEY, CacheFinal.ROLE_MENU_LIST_KEY})
	public void save(Role role) {
		baseService.saveOrUpdate(role);
	}
	@DelCacheHandle(keys={CacheFinal.ROLE_INFO_KEY,CacheFinal.ROLE_LIST_KEY, CacheFinal.ROLE_MENU_LIST_KEY})
	public void del(Integer roleId) {
		baseService.delete(Role.class, roleId);
	}
	@CacheHandle(key=CacheFinal.ROLE_LIST_KEY ,validTime=65)
	public List<Role> loadRoles() {
		List<Role> roles = (List<Role>) baseService.load(Role.class);
		return roles;
	}

}
