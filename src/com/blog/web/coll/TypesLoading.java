package com.blog.web.coll;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.blog.web.cache.TypeCache;
import com.blog.web.entity.HttpEntity;
import com.blog.web.model.Types;
import com.blog.web.util.HttpUtil;
import com.blog.web.util.StringUtils;

@Service
public class TypesLoading {

	public static String url = "http://www.myexception.cn/";
	public static String framePattern = "<div class=\"m_t_c_\\w+\"><span>.*?</div>(?=(</ul><div class=\"m_t_c_\\w+\">)||(</ul></div></div><div class=\"m_a_3\">))";
	public static String columnPattern = "<a href=\"http://www.myexception.cn/\\w(.*?)\" target=\"_blank\">.*?</a>";
	@Resource
	TypeCache typeCache;
	// 获得首页框架大类
	public List<String> LoadFrame() {
		HttpEntity hEntity = HttpUtil.Get(url);
		String html = hEntity.getHtml();
		hEntity = null;
		List<String> articeHtml =StringUtils.doMatcher(html, framePattern);
		if (articeHtml == null || articeHtml.equals("")) {
			return null;
		}
		return articeHtml;
	}
	// 获得每大框架子类
	public List<Types> LoadColumn(List<String> htmlList) {
		List<Types> parentTypes = new ArrayList<Types>();
		for (String html : htmlList) {
			Types parent = new Types();
			String outlineTitle = StringUtils.textCutCenter(html, "<span>","</span>");
			List<Types> childTypes=getChildTypes(html);
			parent.setClassName(outlineTitle);
			parent.setChildTypes(childTypes);
			parentTypes.add(parent);
		}
		return parentTypes;
	}
	public List<Types> getChildTypes(String html){
		List<String> outlines = StringUtils.doMatcher(html, columnPattern);
		if (StringUtils.isNullOrEmpty(outlines)) {
			return null;
		}
		List<Types> childTypes=new ArrayList<Types>();
		for (String outline : outlines) {
			String urltmp = StringUtils.textCutCenter(outline, "href=\"","\"");
			String titleTmp = StringUtils.textCutCenter(outline, ">", "<");
			if (urltmp == null || urltmp.equals("")) {
				continue;
			}
			Types child = new Types();
			child.setClassName("_" + titleTmp);
			child.setOtherUrl(urltmp);
			childTypes.add(child);
		}
		if(StringUtils.isNullOrEmpty(childTypes)){
			return null;
		}
		return childTypes;
	}
	public List<Types> saveTypes(List<Types>  list){
		Types parent=null,child;
		for (Types type:list) {
			parent=typeCache.getType(type.getClassName());
			if(StringUtils.isNullOrEmpty(parent)){
				parent=new Types();
				parent.setClassName(type.getClassName());
			}
			typeCache.saveType(parent);
			if(StringUtils.isNullOrEmpty(parent.getId())){
				parent=typeCache.getType(type.getClassName());
			}
			if(StringUtils.isNullOrEmpty(type.getChildTypes())){
				continue;
			}
			for (Types childType:type.getChildTypes()) {
				child=typeCache.getType(parent.getId(),childType.getClassName());
				if(StringUtils.isNullOrEmpty(child)){
					child=new Types();
					child.setClassName(childType.getClassName());
				}
				child.setOtherUrl(childType.getOtherUrl());
				child.setParentClass(parent.getId());
				typeCache.saveType(child);
			}
		}
		return list;
	}
	public List<Types> loadTypes() {
		try {
			List<Types> list=typeCache.loadOtherTypes();
			if(StringUtils.isNullOrEmpty(list)){
				return loadOtherTypes();
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public List<Types> loadOtherTypes() {
		try {
			List<String> articeHtml =LoadFrame();
			List<Types>  list=LoadColumn(articeHtml);
			saveTypes(list);
			return typeCache.loadOtherTypes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
