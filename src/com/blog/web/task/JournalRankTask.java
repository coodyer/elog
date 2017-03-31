package com.blog.web.task;
import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.blog.web.base.thread.SysThreadPool;
import com.blog.web.cache.JournalCache;
import com.blog.web.cache.TypeCache;

@Service("journalRankTask")
public class JournalRankTask implements InitializingBean {
	@Resource
	JournalCache journalCache;
	@Resource
	TypeCache typeCache;
	
	public synchronized void excuteTask() {
		try {
			journalCache.writeRankCache();
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			typeCache.writeTypes();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		SysThreadPool.threadPool.execute(new Runnable() {
			@Override
			public void run() {
				excuteTask();
			}
		});
		
	}
	
}
