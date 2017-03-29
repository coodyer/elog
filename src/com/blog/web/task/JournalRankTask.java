package com.blog.web.task;
import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.blog.web.base.thread.SysThreadPool;
import com.blog.web.cache.JournalCache;

@Service("journalRankTask")
public class JournalRankTask implements InitializingBean {
	@Resource
	JournalCache journalCache;
	public synchronized void excuteTask() {
		journalCache.writeRankCache();
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
