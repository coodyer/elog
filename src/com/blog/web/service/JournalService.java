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

	
	public List<Journal> getSiteMapList(Integer lastId){
		if(lastId==null){
			lastId=0;
		}
		Where where=new Where();
		where.set("id", ">",lastId);
		Pager<Journal> pager=new Pager<Journal>();
		pager.setPageSize(400);
		return	(List<Journal>) baseDao.findByObject(Journal.class, pager, where, null, null);
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
