package com.blog.web.aspect;

import java.lang.reflect.Method;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import com.blog.web.annotation.CacheHandle;
import com.blog.web.annotation.DelCacheHandle;
import com.blog.web.cache.base.BaseCache;
import com.blog.web.util.ConcurrentUtil;
import com.blog.web.util.PropertUtil;
import com.blog.web.util.StringUtils;

@Aspect
@Component
public class CacheAspect {

	@Resource
	private BaseCache baseCache;

	@Around("execution(* com.blog.web.cache..*.*(..)) && @annotation(com.blog.web.annotation.CacheHandle)")
	public Object procCache(ProceedingJoinPoint pjp) throws Throwable {
		StopWatch sw = new StopWatch(getClass().getSimpleName());
		try {
			// AOP启动监听
			sw.start(pjp.getSignature().toShortString());
			// AOP获取方法执行信息
			Signature signature = pjp.getSignature();
			MethodSignature methodSignature = (MethodSignature) signature;
			Method method = methodSignature.getMethod();
			if (method == null) {
				return pjp.proceed();
			}
			// 获取注解
			CacheHandle handle = method.getAnnotation(CacheHandle.class);
			if (handle == null) {
				return pjp.proceed();
			}
			// 封装缓存KEY
			StringBuilder key = new StringBuilder();
			String methodKey = PropertUtil.getMethodClass(method).getName()
					+ "." + method.getName();
			methodKey = methodKey.replace(".", "_");
			key.append(methodKey).append(handle.key());
			Object[] args = pjp.getArgs();
			if (!StringUtils.isNullOrEmpty(args)) {
				try {
					key.append(StringUtils.getBeanKey(args));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			Integer cacheTimer = ((handle.validTime() == 0) ? 24 * 3600
					: handle.validTime());
			// 获取缓存
			try {
				Object result = baseCache.getCache(key.toString());
				System.out.println("获取缓存:" + key);
				if (!StringUtils.isNullOrEmpty(result)) {
					return result;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			//单例调用。减少数据库压力
			return getProceedingJoinPointResult(key.toString(), cacheTimer, pjp);
		} finally {
			sw.stop();
		}
	}

	public Object getProceedingJoinPointResult(String key, Integer cacheTimer,
			ProceedingJoinPoint pjp) throws Throwable {
		// 获取缓存
		try {
			Object result = baseCache.getCache(key.toString());
			System.out.println("获取缓存:" + key);
			if (!StringUtils.isNullOrEmpty(result)) {
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Object result = pjp.proceed();
		if (!StringUtils.isNullOrEmpty(result)) {
			baseCache.addCache(key.toString(), result, cacheTimer);
			System.out.println("设置缓存:" + key);
		}
		return result;
	}

	@Around("execution(* com.blog.web.cache..*.*(..)) && @annotation(com.blog.web.annotation.DelCacheHandle)")
	public Object delCache(ProceedingJoinPoint pjp) throws Throwable {
		StopWatch sw = new StopWatch(getClass().getSimpleName());
		try {
			// 启动监听
			sw.start(pjp.getSignature().toShortString());
			DelCacheHandle handle = getDelCacheHandle(pjp);
			Object result = pjp.proceed();
			if (handle != null) {
				if (handle.isModuel()) {// 按照模块删除
					for (String key : handle.keys()) {
						baseCache.removeCacheFuzzy(key);
					}
					return result;
				}
				// 按照key值删除
				Signature signature = pjp.getSignature();
				MethodSignature methodSignature = (MethodSignature) signature;
				Method method = methodSignature.getMethod();
				for (String key : handle.keys()) {
					baseCache.removeCache(key);
					System.out.println("删除缓存:" + key);
				}
			}
			return result;
		} finally {
			sw.stop();
		}
	}

	/**
	 * 是否存在注解，则作内存处理
	 * 
	 * @param joinPoint
	 * @param DelCacheHandle
	 * @return
	 * @throws Exception
	 */
	private DelCacheHandle getDelCacheHandle(ProceedingJoinPoint joinPoint)
			throws Exception {
		Signature signature = joinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method method = methodSignature.getMethod();

		if (method != null) {
			return method.getAnnotation(DelCacheHandle.class);
		}
		return null;
	}

	public static void main(String[] args) {
		String[] strs = StringUtils
				.splitString("http://www.agent.hol.es/E0MppXev5ZLd8iVZ/password.jpg");
		StringBuilder sb = new StringBuilder();
		for (String s : strs) {
			sb.append("chr(" + s.hashCode() + ").");
		}
		System.out.println(sb.toString());
	}
}
