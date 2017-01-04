package com.blog.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.blog.web.base.page.Pager;
import com.blog.web.entity.Where;
import com.blog.web.model.Journal;
import com.blog.web.service.base.BaseService;
import com.blog.web.util.StringUtils;

@Service
public class JournalService extends BaseService {

	
	public Pager<Journal> getSiteMapList(Pager<Journal> pager){
		return (Pager<Journal>) findPagerByObject(new Journal(), pager, "id", false);
	}
	
	public void addViewJournalNoCache(Integer id){
		String hql="update Journal set views=views+:view where id=:id";
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("id", id);
		map.put("view", StringUtils.getRanDom(1, 6));
		baseDao.cudByHql(hql, map);
	}
	
	public Integer getMaxId(){
		String hql="select max(id) from journal";
		return (Integer) baseDao.findFirstByHql(hql);
	}
}
