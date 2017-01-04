package com.blog.web.task;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.blog.web.coll.JournalLoading;

@Service
public class CollJournalTask {
	@Resource
	JournalLoading journalLoading;
	public static Boolean isRun = false;

	public void excuteTask() {
		System.out.println("采集任务执行中=============");
		if (isRun) {
			System.out.println("正在执行,暂停本次采集任务=============");
			return;
		}
		isRun = true;
		try {
			journalLoading.loadJournal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			isRun = false;
			System.out.println("采集任务执行完毕=============");
		}

	}
}
