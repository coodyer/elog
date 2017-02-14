package com.blog.web.cache;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.blog.web.annotation.CacheWipe;
import com.blog.web.annotation.CacheWrite;
import com.blog.web.base.cache.CacheFinal;
import com.blog.web.cache.base.BaseCache;
import com.blog.web.model.Admin;
import com.blog.web.service.AdminService;

@Service
public class AdminCache extends BaseCache {

	@Resource
	AdminService adminService;
	@CacheWrite(key=CacheFinal.ADMIN_INFO_KEY ,validTime=60)
	public Admin getAdmin(String username) {
		Admin admin = (Admin) baseService.findFirstByField(Admin.class, "userName",
				username);
		return admin;
	}
	@CacheWrite(key=CacheFinal.ADMIN_LIST_KEY ,validTime=60)
	public List<Admin> loadAdmins() {
		List<Admin> admins = (List<Admin>) baseService.load(Admin.class);
		return admins;
	}
	@CacheWrite(key=CacheFinal.ADMIN_INFO_KEY ,validTime=60)
	public Admin getAdmin(Integer id) {
		Admin admin = (Admin) baseService.get(Admin.class, id);
		return admin;
	}
	@CacheWipe(key=CacheFinal.ADMIN_LIST_KEY)
	@CacheWipe(key=CacheFinal.ADMIN_INFO_KEY)
	public void save(Admin admin) {
		adminService.saveOrUpdate(admin);
	}
	@CacheWipe(key=CacheFinal.ADMIN_LIST_KEY)
	@CacheWipe(key=CacheFinal.ADMIN_INFO_KEY)
	public void delete(Integer id) {
		adminService.delete(Admin.class, id);
	}
}
