package com.blog.web.util;

import java.util.UUID;

public class JUUIDUtil {
	static Object mutex = new Object();

	public static String createUuid() {
		synchronized (mutex) {
			String str = UUID.randomUUID().toString().replace("-", "");
			return str;
		}
	}

	public static void main(String[] args) {
		System.out.println(createUuid());
	}
}
