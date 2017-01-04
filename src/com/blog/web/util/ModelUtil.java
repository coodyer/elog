package com.blog.web.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class ModelUtil {

	public static void main(String[] args) {
		List<Class<?>> classes = ClassUtil.getClasses("com.xss.web.model");
		List<String> fieldNames = new ArrayList<String>();
		for (Class<?> cla : classes) {
			fieldNames = PropertUtil.getFieldNames(cla);
			if (StringUtils.isNullOrEmpty(fieldNames)
					|| fieldNames.contains("dataMap")) {
				continue;
			}
			System.out.println("---------" + cla.getName() + "----------");
			System.out.println(MessageFormat.format(
					"public static final String TABLE=\"{0}\";", cla.getName()
							.substring(cla.getName().lastIndexOf(".") + 1)));
			for (String fieldName : fieldNames) {
				System.out.println(MessageFormat.format(
						"public static final String {0}=\"{1}\";",
						fieldName.toUpperCase(), fieldName));
			}
			System.out.println("--------- ----------");
		}
	}
}
