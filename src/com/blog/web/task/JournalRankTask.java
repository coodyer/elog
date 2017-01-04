package com.blog.web.task;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.blog.web.cache.JournalCache;

@Service("journalRankTask")
public class JournalRankTask {
	@Resource
	JournalCache journalCache;
	public synchronized void excuteTask() {
		journalCache.writeRankCache();
	}
	
}
