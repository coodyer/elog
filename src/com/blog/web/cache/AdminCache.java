package com.blog.web.cache;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.blog.web.annotation.CacheHandle;
import com.blog.web.annotation.DelCacheHandle;
import com.blog.web.base.cache.CacheFinal;
import com.blog.web.cache.base.BaseCache;
import com.blog.web.model.Admin;
import com.blog.web.service.AdminService;

@Service
public class AdminCache extends BaseCache {

	@Resource
	AdminService adminService;
	@CacheHandle(key=CacheFinal.ADMIN_INFO_KEY ,validTime=60)
	public Admin getAdmin(String username) {
		Admin admin = (Admin) baseService.findFirstByField(Admin.class, "userName",
				username);
		return admin;
	}
	@CacheHandle(key=CacheFinal.ADMIN_LIST_KEY ,validTime=60)
	public List<Admin> loadAdmins() {
		List<Admin> admins = (List<Admin>) baseService.load(Admin.class);
		return admins;
	}
	@CacheHandle(key=CacheFinal.ADMIN_INFO_KEY ,validTime=60)
	public Admin getAdmin(Integer id) {
		Admin admin = (Admin) baseService.get(Admin.class, id);
		return admin;
	}
	@DelCacheHandle(keys={CacheFinal.ADMIN_LIST_KEY,CacheFinal.ADMIN_INFO_KEY})
	public void save(Admin admin) {
		adminService.saveOrUpdate(admin);
	}
	@DelCacheHandle(keys={CacheFinal.ADMIN_INFO_KEY,CacheFinal.ADMIN_LIST_KEY})
	public void delete(Integer id) {
		adminService.delete(Admin.class, id);
	}
}
