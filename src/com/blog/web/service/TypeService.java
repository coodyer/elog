package com.blog.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.blog.web.service.base.BaseService;
import com.blog.web.util.StringUtils;

@Service
public class TypeService extends BaseService {

	public Map<Integer, Integer> loadTypeJournalCount(){
		String hql="select types.id from Journal group by types.id";
		List<Object[]> list=(List<Object[]>) baseDao.findByHql(hql);
		if(StringUtils.isNullOrEmpty(list)){
			return null;
		}
		Map<Integer, Integer> map=new HashMap<Integer, Integer>();
		for(Object [] objs:list){
			hql="select count(*) from Journal where types.id="+StringUtils.toInteger(objs[0]);
			Integer count=baseDao.cudByHql(hql);
			map.put(StringUtils.toInteger(objs[0]),count);
		}
		return map;
	}
}
