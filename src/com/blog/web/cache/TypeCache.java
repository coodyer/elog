package com.blog.web.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.blog.web.annotation.CacheWipe;
import com.blog.web.annotation.CacheWrite;
import com.blog.web.base.cache.CacheFinal;
import com.blog.web.cache.base.BaseCache;
import com.blog.web.entity.Where;
import com.blog.web.model.Types;
import com.blog.web.service.TypeService;
import com.blog.web.util.PropertUtil;
import com.blog.web.util.StringUtils;

@Service
@SuppressWarnings("unchecked")
public class TypeCache extends BaseCache {

	@Resource
	TypeService typeService;

	@CacheWrite(key = CacheFinal.JOURNAL_TYPE_LIST, validTime = 600)
	public  List<Types> loadTypes() {
		List<Types> types = (List<Types>) baseService.load(Types.class);
		types = formatTypes(types);
		types = loadTypeJournalCount(types);
		return types;
	}

	@CacheWrite(key = CacheFinal.JOURNAL_TYPE_LIST, validTime = 600)
	public  List<Types> loadInTypes(Integer... ids) {
		List<Types> types = (List<Types>) baseService.findInFields(Types.class,
				"id", ids);
		return types;
	}

	// 加载第三方采集类别
	@CacheWrite(key = CacheFinal.JOURNAL_TYPE_LIST, validTime = 600)
	public  List<Types> loadOtherTypes() {
		Where where = new Where().set("otherUrl", "<>", "");
		List<Types> types = (List<Types>) baseService.findByObject(Types.class,
				where);
		return types;
	}

	@CacheWrite(key = CacheFinal.JOURNAL_TYPE_LIST, validTime = 600)
	public  List<Types> loadTypes(Integer parentId) {
		List<Types> types = loadTypes();
		if (StringUtils.isNullOrEmpty(types)) {
			return null;
		}
		if (StringUtils.isNullOrEmpty(parentId)) {
			return types;
		}
		for (Types type : types) {
			if (type.getId().intValue() == parentId.intValue()) {
				return type.getChildTypes();
			}
		}
		return types;
	}

	@CacheWipe(key=CacheFinal.JOURNAL_TYPE_LIST,isModel=true)
	@CacheWipe(key=CacheFinal.JOURNAL_TYPE_INFO,isModel=true)
	@CacheWipe(key=CacheFinal.JOURNAL_TYPE_COUNT,isModel=true)
	public void saveType(Types type) {
		baseService.saveOrUpdate(type);
	}

	@CacheWipe(key=CacheFinal.JOURNAL_TYPE_LIST,isModel=true)
	@CacheWipe(key=CacheFinal.JOURNAL_TYPE_INFO,isModel=true)
	@CacheWipe(key=CacheFinal.JOURNAL_TYPE_COUNT,isModel=true)
	public void delType(Integer id) {
		baseService.delete(Types.class, id);
	}

	public Types getType(Integer id) {
		if (StringUtils.isNullOrEmpty(id)) {
			return null;
		}
		String key = CacheFinal.JOURNAL_TYPE_INFO + id;
		Types type = (Types) getCache(key);
		if (!StringUtils.isNullOrEmpty(type)) {
			return type;
		}
		type = (Types) baseService.get(Types.class, id);
		if (!StringUtils.isNullOrEmpty(type)) {
			addCache(key, type);
		}
		return type;
	}

	public Types getType(String name) {
		if (StringUtils.isNullOrEmpty(name)) {
			return null;
		}
		String key = CacheFinal.JOURNAL_TYPE_INFO + name;
		Types type = (Types) getCache(key);
		if (!StringUtils.isNullOrEmpty(type)) {
			return type;
		}
		List<Types> types = (List<Types>) baseService.findByField(Types.class,
				"className", name);
		if (StringUtils.isNullOrEmpty(types)) {
			return null;
		}
		type = types.get(0);
		addCache(key, type);
		return type;
	}

	@CacheWrite(key = CacheFinal.JOURNAL_TYPE_INFO, validTime = 3600)
	public Types getType(Integer parentClass, String name) {
		if (StringUtils.isNullOrEmpty(name)) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("parentClass", parentClass);
		map.put("className", name);
		List<Types> types = (List<Types>) baseService.findByField(Types.class,
				map);
		if (StringUtils.isNullOrEmpty(types)) {
			return null;
		}
		return types.get(0);
	}

	@CacheWrite(key = CacheFinal.JOURNAL_TYPE_COUNT, validTime = 3600)
	private  List<Types> loadTypeJournalCount(List<Types> types) {
		if (StringUtils.isNullOrEmpty(types)) {
			return null;
		}
		Map<Integer, Integer> map = typeService.loadTypeJournalCount();
		for (Types type : types) {
			if (StringUtils.isNullOrEmpty(type.getParentClass())
					&& !StringUtils.isNullOrEmpty(type.getChildTypes())) {
				int currCount = 0;
				type.setCount(currCount);
				if (!StringUtils.isNullOrEmpty(map)) {
					currCount = (map.get(type.getId()) == null ? 0 : map
							.get(type.getId()));
				}
				for (Types child : type.getChildTypes()) {
					child.setCount(0);
					if (StringUtils.isNullOrEmpty(map)) {
						continue;
					}
					if (!StringUtils.isNullOrEmpty(map)) {
						child.setCount(map.get(child.getId()) == null ? 0 : map
								.get(child.getId()));
						// continue;
					}
					if (!StringUtils.isNullOrEmpty(map.get(child.getId()))) {
						currCount += (map.get(child.getId()));
					}
				}
				type.setCount(currCount);
				continue;
			}
			if (StringUtils.isNullOrEmpty(map)) {
				type.setCount(0);
				continue;
			}
			type.setCount(map.get(type.getId()));
		}
		return types;
	}

	private  List<Types> formatTypes(List<Types> types) {
		if (StringUtils.isNullOrEmpty(types)) {
			return null;
		}
		Map<Object, List> map = PropertUtil.parsObjToMaps(types, "parentClass");
		List<Types> tmps = new ArrayList<Types>();
		for (Types type : types) {
			if (StringUtils.isNullOrEmpty(type.getParentClass())) {
				tmps.add(type);
			}
		}
		if (StringUtils.isNullOrEmpty(tmps)) {
			return null;
		}
		List<Types> tmpChilds = null;
		for (Types type : tmps) {
			try {
				tmpChilds = map.get(type.getId());
				type.setChildTypes(tmpChilds);
			} catch (Exception e) {
			}
		}
		return tmps;
	}
}
