package com.blog.web.service;

import org.springframework.stereotype.Service;

import com.blog.web.model.Menus;
import com.blog.web.service.base.BaseService;

@Service
public class MenuService extends BaseService {

	public void delMenu(Integer id) {
		Menus menu = (Menus) get(Menus.class, id);
		if (menu.getType().intValue() == 0) {
			String hql = "delete from Menus where menus.id=" + menu.getId();
			baseDao.cudByHql(hql);
		}
		delete(menu);
	}
}
