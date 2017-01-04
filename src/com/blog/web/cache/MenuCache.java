package com.blog.web.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.blog.web.annotation.CacheHandle;
import com.blog.web.annotation.DelCacheHandle;
import com.blog.web.base.cache.CacheFinal;
import com.blog.web.cache.base.BaseCache;
import com.blog.web.model.Menus;
import com.blog.web.model.Role;
import com.blog.web.service.MenuService;
import com.blog.web.util.PropertUtil;
import com.blog.web.util.StringUtils;

@Service
public class MenuCache extends BaseCache {
	@Resource
	MenuService menuService;
	@CacheHandle(key=CacheFinal.ROLE_MENU_LIST_KEY ,validTime=600)
	public  List<Menus> loadMenus(Integer... ids) {
		List<Menus> menus = (List<Menus>) baseService.findInFields(Menus.class, "seq",
				false, "id", ids);
		menus=parsMenus(menus);
		return menus;
	}
	@CacheHandle(key=CacheFinal.MENU_INFO_KEY ,validTime=600)
	public  Menus getMenu(Integer id) {
		Menus menu = (Menus) baseService.get(Menus.class, id);
		return menu;
	}
	@DelCacheHandle(keys={CacheFinal.MENU_INFO_KEY,CacheFinal.MENU_LIST_KEY,CacheFinal.ROLE_MENU_LIST_KEY})
	public void save(Menus menu) {
		baseService.saveOrUpdate(menu);
	}
	@DelCacheHandle(keys={CacheFinal.MENU_INFO_KEY,CacheFinal.MENU_LIST_KEY,CacheFinal.ROLE_MENU_LIST_KEY})
	public void delete(Integer id) {
		menuService.delMenu(id);
	}
	@CacheHandle(key=CacheFinal.MENU_ROLE_LIST_KEY ,validTime=600)
	public  List<Menus> loadRoleMenus(Role role) {
		if (!StringUtils.isNullOrEmpty(role)
				&& !StringUtils.isNullOrEmpty(role.getMenus())) {
			Integer[] ids = StringUtils.splitByMosaicIntegers(role.getMenus(),
					",");
			List<Menus> roleMenus = loadMenus(ids);
			return roleMenus;
		}
		return null;
	}
	@CacheHandle(key=CacheFinal.MENU_LIST_KEY ,validTime=600)
	public  List<Menus> loadBaseMenus() {
		List<Menus> menus = (List<Menus>) baseService.findByField(Menus.class, "seq",
				false, "type", 0);
		return menus;
	}
	private  List<Menus> parsMenus(List<Menus> menus) {
		if (StringUtils.isNullOrEmpty(menus)) {
			return null;
		}
		Map<Object, List> menuMap = PropertUtil.parsObjToMaps(menus, "type");
		List<Menus> fatherMenus = menuMap.get(0);
		List<Menus> childMenus = menuMap.get(1);
		for (Menus father : fatherMenus) {
			try {
				Integer fatherId = father.getId();
				List<Menus> currChilds = new ArrayList<Menus>();
				for (Menus tmp : childMenus) {
					if (tmp.getMenus().getId().intValue() == fatherId
							.intValue()) {
						currChilds.add(tmp);
					}
				}
				currChilds = PropertUtil.parsListSeq(currChilds, "seq");
				father.setChildMenus(currChilds);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return fatherMenus;
	}
}
