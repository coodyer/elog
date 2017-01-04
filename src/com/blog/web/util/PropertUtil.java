package com.blog.web.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.blog.web.base.cache.CacheTimerHandler;
import com.blog.web.base.dao.JDBCUtil;
import com.blog.web.entity.BeanFieldEntity;
import com.blog.web.entity.Record;
import com.blog.web.model.Journal;

/**
 * @remark 对象操作工具,多反射。
 * @author WebSOS
 * @email 644556636@qq.com
 * @blog bkkill.com
 */
public class PropertUtil {

	public static Object copyPropres(Object sourceObj, Object targetObj)
			throws Exception {
		Map<String, Object> map = objToMap(sourceObj);
		return mapToObject(targetObj.getClass(), map);
	}

	public static Object requestGetObject(HttpServletRequest request, Class cla) {
		try {
			return mapToObject(cla, requestGetMap(request));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Map requestGetMap(HttpServletRequest request) {
		Map requestParams = request.getParameterMap();
		return requestParams;
	}

	public static Object recordToObject(Class cla, Record rec) {
		return mapToObject(cla, rec.getMap());
	}

	public static Object mapToObject(Class cla, Map<String, Object> sourceMap) {
		try {
			if (StringUtils.isNullOrEmpty(sourceMap)) {
				return null;
			}
			BeanInfo sourceBean = Introspector.getBeanInfo(cla,
					java.lang.Object.class);
			PropertyDescriptor[] sourceProperty = sourceBean
					.getPropertyDescriptors();
			Map<String, PropertyDescriptor> propertyMap = new HashMap<String, PropertyDescriptor>();
			for (PropertyDescriptor propertyTmp : sourceProperty) {
				propertyMap.put(propertyTmp.getName().toLowerCase(),
						propertyTmp);
			}
			PropertyDescriptor propertyDescriptor = null;
			String currParaName = null;
			Object targetObj = cla.newInstance();
			for (String key : sourceMap.keySet()) {
				currParaName = parsParaName(key);
				if (propertyMap.containsKey(currParaName)) {
					propertyDescriptor = propertyMap.get(currParaName);
					try {
						propertyDescriptor.getWriteMethod().invoke(targetObj,
								sourceMap.get(key));
					} catch (Exception e) {
					}

				}
			}
			return targetObj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static Map<String, Object> objToMap(Object obj) {
		try {
			BeanInfo sourceBean = Introspector.getBeanInfo(obj.getClass(),
					java.lang.Object.class);
			PropertyDescriptor[] sourceProperty = sourceBean
					.getPropertyDescriptors();
			if (sourceProperty == null) {
				return null;
			}
			Map<String, Object> map = new HashMap<String, Object>();
			for (PropertyDescriptor tmp : sourceProperty) {
				map.put(tmp.getName(), tmp.getReadMethod().invoke(obj));
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static Map<String, Object> objToSqlParaMap(Object obj) {
		try {
			BeanInfo sourceBean = Introspector.getBeanInfo(obj.getClass(),
					java.lang.Object.class);
			PropertyDescriptor[] sourceProperty = sourceBean
					.getPropertyDescriptors();
			if (sourceProperty == null) {
				return null;
			}
			Map<String, Object> map = new HashMap<String, Object>();
			for (PropertyDescriptor tmp : sourceProperty) {
				map.put(parsParaName(tmp.getName()), tmp.getReadMethod()
						.invoke(obj));
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private static Class getObjClass(Object obj) {
		if (obj instanceof Class) {
			return (Class) obj;
		}
		return obj.getClass();
	}

	// userNameתΪuser_name
	public static String parsParaName(String paraName) {
		if (paraName == null) {
			return null;
		}
		if (paraName.indexOf("_") > -1) {
			String[] paraNames = paraName.split("_");
			if (paraNames.length > 1) {
				StringBuilder sb = new StringBuilder();
				sb.append(paraNames[0]);
				for (int i = 1; i < paraNames.length; i++) {
					sb.append(firstUpcase(paraNames[i]));
				}
				return sb.toString();
			}
		}
		return paraName;
	}

	public static String unParsParaName(String paraName) {
		char[] chrs = paraName.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < chrs.length; i++) {
			char chr = chrs[i];
			if (i != 0 && Character.isUpperCase(chr)) {
				sb.append("_");
			}
			sb.append(String.valueOf(chr).toLowerCase());
		}
		return sb.toString();
	}

	/**
	 * 设置字段值
	 * 
	 * @param obj
	 *            实例对象
	 * @param propertyName
	 *            属性名
	 * @param value
	 *            新的字段值
	 * @return
	 */
	public static void setProperties(Object object,
			String propertyName, Object value) throws Exception {
		Field field=object.getClass().getDeclaredField(propertyName);
		field.setAccessible(true);
		try {
			if (StringUtils.isNullOrEmpty(value)) {
				value = null;
				field.set(object, value);
				return;
			} 
			if (field.getType().getName().equals(Boolean.class.getName())) {
				value = ((String) value).equals("true") ? true : false;
				field.set(object, value);
				return;
			}
			if (field.getType().getName().equals(Integer.class.getName())) {
				value = Integer.valueOf(value.toString());
				field.set(object, value);
				return;
			} 
			if (field.getType().getName().equals(Float.class.getName())) {
				value = Float.valueOf(value.toString());
				field.set(object, value);
				return;
			}
			if (field.getType().getName().equals(Long.class.getName())) {
				value = Long.valueOf(value.toString());
				field.set(object, value);
				return;
			}
			if (field.getType().getName().equals(String.class.getName())) {
				value = value.toString();
				field.set(object, value);
				return;
			}
			if (field.getType().getName().equals(Date.class.getName())) {
				if (StringUtils.isMatcher(value.toString(),
						"[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}")) {
					value = new SimpleDateFormat("yyyy-MM-dd").parse(value
							.toString());
				}
				if (StringUtils
						.isMatcher(value.toString(),
								"^\\d{4}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D+\\d{1,2}\\D*")) {
					value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.parse(value.toString());
				}
				field.set(object, value);
				return;
			}
			field.set(object, value);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String firstUpcase(String s) {
		if (Character.isUpperCase(s.charAt(0))) {
			return s;
		} else {
			return (new StringBuilder())
					.append(Character.toUpperCase(s.charAt(0)))
					.append(s.substring(1)).toString();
		}
	}

	public static List<BeanFieldEntity> getBeanFields(Object obj) {
		Class cla = getObjClass(obj);
		Field[] fields = cla.getDeclaredFields();
		List<BeanFieldEntity> infos = getClassFields(cla);
		if(StringUtils.isNullOrEmpty(infos)){
			return null;
		}
		for (BeanFieldEntity info:infos) {
			try {
				Field f=info.getSourceField();
				f.setAccessible(true);
				info.setFieldValue(f.get(obj));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return infos;
	}
	public static List<BeanFieldEntity> getClassFields(Class cla){
		try {
			String key="OBJ_CLASS_FIELDS_CACHE"+EncryptionUtil.md5Code(cla.getName());
			List<BeanFieldEntity> infos=(List<BeanFieldEntity>) CacheTimerHandler.getCache(key);
			if(StringUtils.isNullOrEmpty(infos)){
				Field[] fields = cla.getDeclaredFields();
				infos = new ArrayList<BeanFieldEntity>();
				for (Field f : fields) {
					BeanFieldEntity tmp = new BeanFieldEntity();
					tmp.setSourceField(f);
					tmp.setFieldAnnotations(f.getAnnotations());
					tmp.setFieldName(f.getName());
					tmp.setFieldType(f.getType());
					infos.add(tmp);
				}
			}
			if(!StringUtils.isNullOrEmpty(infos)){
				CacheTimerHandler.addCache(key, infos,60);
			}
			return infos;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// List转为Map。fieldName作为Key，对象作为Value
		public static Map<?, ?> parsObjToMap(List<?> objs, String fieldName) {
			if (StringUtils.isNullOrEmpty(objs)) {
				return null;
			}
			Map<Object, Object> map = new TreeMap<Object, Object>();
			for (Object obj : objs) {
				try {
					Object fieldValue = getFieldValue(obj, fieldName);
					map.put(fieldValue, obj);
				} catch (Exception e) {
				}
			}
			if (StringUtils.isNullOrEmpty(map)) {
				return null;
			}
			return map;
		}

		// 一个List根据某个字段排序
		public static List parsListSeq(List objs, String fieldName) {
			return parsListSeq(objs, fieldName, null);
		}

		// 一个List根据某个字段排序
		public static List parsListSeq(List objs, String fieldName, Boolean isDesc) {
			if (StringUtils.isNullOrEmpty(objs)) {
				return null;
			}
			Map<Object, List> maps = parsObjToMaps(objs, fieldName);
			List list = new ArrayList();
			for (Object key : maps.keySet()) {
				try {
					list.addAll(maps.get(key));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (StringUtils.isNullOrEmpty(isDesc)) {
				isDesc = false;
			}
			if (isDesc) {
				Collections.reverse(list);
			}
			return list;
		}

		// 一个List转为Map，fieldName作为Key，所有字段值相同的组成List作为value
		public static Map<Object, List> parsObjToMaps(List objs, String fieldName) {
			if (StringUtils.isNullOrEmpty(objs)) {
				return null;
			}
			Map<Object, List> map = new TreeMap<Object, List>();
			List<Object> list;
			for (Object obj : objs) {
				try {
					Object fieldValue = getFieldValue(obj, fieldName);
					if (map.containsKey(fieldValue)) {
						map.get(fieldValue).add(obj);
						continue;
					}
					list = new ArrayList<Object>();
					list.add(obj);
					map.put(fieldValue, list);
				} catch (Exception e) {
				}
			}
			if (StringUtils.isNullOrEmpty(map)) {
				return null;
			}
			return map;
		}
	public static Object getFieldValue(Object obj, String fieldName) {
		if (StringUtils.isNullOrEmpty(obj)){
			return null;}
		Field f =getField(obj.getClass(), fieldName);
		if(StringUtils.isNullOrEmpty(f)){
			return null;
		}
		f.setAccessible(true);
		try {
			return f.get(obj);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}
	public static Class getMethodClass(Method method) {
		Class cla=(Class) PropertUtil.getFieldValue(method, "clazz");
		return cla;
	}
	private static Field getField(Class cla, String fieldName){
		String key="STRUTSUTILS_FIELD_CACHE"+StringUtils.getBeanKey(cla.getName(),fieldName);
		Field f=(Field) CacheTimerHandler.getCache(key);
		if(!StringUtils.isNullOrEmpty(f)){
			return f;
		}
		Field[] fields = cla.getDeclaredFields();
		for (int j = 0; j < fields.length; j++) {
			if (fields[j].getName().equals(fieldName)) {
				CacheTimerHandler.addCache(key, fields[j],60);
				return fields[j];
			}
		}
		return null;
	}
	// 获取List对象某个字段的值组成新List
	public static List<?> getFieldValues(List<?> objs, String fieldName) {
		if (StringUtils.isNullOrEmpty(objs)) {
			return null;
		}
		List<Object> list = new ArrayList<Object>();
		Object value;
		for (Object obj : objs) {
			value = getFieldValue(obj, fieldName);
			list.add(value);
		}
		if (StringUtils.isNullOrEmpty(objs)) {
			return null;
		}
		return list;

	}

	// 获取对象字段列表
	public static List<String> getFieldNames(Class<?> cla) {
		Field[] fields = cla.getDeclaredFields();
		List<String> fieldNames = new ArrayList<String>();
		for (Field field : fields) {
			fieldNames.add(field.getName());
		}
		return fieldNames;
	}
	
	public static void main(String[] args) {
		//创建JDBC对象
		JDBCUtil jdbc=new JDBCUtil();
		//从数据库获得ID为32866的一条记录
		Journal journal =(Journal) jdbc.get(Journal.class, 32866);
		for (int i = 0; i <10; i++) {
			//提取字段属性列表
			List<BeanFieldEntity> eneitys=getBeanFields(journal);
			//打印输出对象信息
			System.out.println(JSONWriter.format(JSONWriter.write(eneitys)));
		}
		
	}
}