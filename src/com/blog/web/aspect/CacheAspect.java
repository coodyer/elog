package com.blog.web.aspect;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import com.blog.web.annotation.CacheWipe;
import com.blog.web.annotation.CacheWrite;
import com.blog.web.base.cache.CacheTimerHandler;
import com.blog.web.util.AspectUtil;
import com.blog.web.util.StringUtils;

@Aspect
@Component
public class CacheAspect {


	@Around("execution(* com.blog.web..*.*(..)) && @annotation(com.blog.web.annotation.CacheWrite)")
	public Object cCacheWrite(ProceedingJoinPoint pjp) throws Throwable {
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
			CacheWrite handle = method.getAnnotation(CacheWrite.class);
			if (handle == null) {
				return pjp.proceed();
			}
			// 封装缓存KEY
			Object[] args = pjp.getArgs();
			String key = handle.key();
			try {
				if (StringUtils.isNullOrEmpty(key)) {
					key = AspectUtil.getMethodCacheKey(method);
				}
				if (StringUtils.isNullOrEmpty(handle.fields())) {
					key += "_";
					key += AspectUtil.getBeanKey(args);
				}
				if (!StringUtils.isNullOrEmpty(handle.fields())) {
					key = AspectUtil.getFieldKey(method, args, key,
							handle.fields());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Integer cacheTimer = ((handle.validTime() == 0) ? 24 * 3600
					: handle.validTime());
			// 获取缓存
			try {
				Object result = CacheTimerHandler.getCache(key);
				if (!StringUtils.isNullOrEmpty(result)) {
					return result;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Object result = pjp.proceed();
			if (result != null) {
				try {
					CacheTimerHandler.addCache(key, result, cacheTimer);
				} catch (Exception e) {
				}
			}
			return result;
		} finally {
			sw.stop();
		}
	}

	/**
	 * 缓存清理
	 * 
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Around("execution(* com.blog.web..*.*(..)) && (@annotation(com.blog.web.annotation.CacheWipe)||@annotation(com.blog.web.annotation.CacheWipes))")
	public Object zCacheWipe(ProceedingJoinPoint pjp) throws Throwable {
		StopWatch sw = new StopWatch(getClass().getSimpleName());
		try {
			// 启动监听
			sw.start(pjp.getSignature().toShortString());
			Signature signature = pjp.getSignature();
			MethodSignature methodSignature = (MethodSignature) signature;
			Method method = methodSignature.getMethod();
			if (method == null) {
				return pjp.proceed();
			}
			Object[] paras = pjp.getArgs();
			Object result = pjp.proceed();
			CacheWipe[] handles = method.getAnnotationsByType(CacheWipe.class);
			if (StringUtils.isNullOrEmpty(handles)) {
				return result;
			}
			for (CacheWipe wipe : handles) {
				try {
					String key = wipe.key();
					if (StringUtils.isNullOrEmpty(wipe.key())) {
						key = (AspectUtil.getMethodCacheKey(method));
					}
					if (wipe.isModel() == true) {
						CacheTimerHandler.removeCacheFuzzy(key);
						continue;
					}
					if(!StringUtils.isNullOrEmpty(wipe.fields())){
						key=AspectUtil.getFieldKey(method, paras,
								key, wipe.fields());
					}
					CacheTimerHandler.removeCache(key);
				} catch (Exception e) {
				}
			}
			return result;
		} finally {
			sw.stop();
		}
	}
}
