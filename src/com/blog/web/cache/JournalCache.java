package com.blog.web.cache;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.blog.web.annotation.CacheHandle;
import com.blog.web.annotation.DelCacheHandle;
import com.blog.web.base.cache.CacheFinal;
import com.blog.web.base.page.Pager;
import com.blog.web.cache.base.BaseCache;
import com.blog.web.model.Journal;
import com.blog.web.service.JournalService;
import com.blog.web.util.HTMLSpirit;
import com.blog.web.util.StringUtils;

@Service
public class JournalCache extends BaseCache {
	@Resource
	TypeCache typeCache;
	@Resource
	JournalService journalService;
	
	@CacheHandle(key=CacheFinal.JOURNAL_LIST ,validTime=66)
	public  Pager<Journal> loadJournal(Journal journal,Pager<Journal> pager){
		Pager<Journal> data=(Pager<Journal>) baseService.findPagerByObject(journal, pager);
		if(StringUtils.isNullOrEmpty(data)){
			return null;
		}
		if(StringUtils.isNullOrEmpty(data.getPageData())){
			return data;
		}
		for (Object tmp:data.getPageData()) {
			if(StringUtils.isNullOrEmpty(((Journal)tmp).getContext())){
				continue;
			}
			((Journal)tmp).setContext(HTMLSpirit.delHTMLTag(((Journal)tmp).getContext()));
			((Journal)tmp).setContext(((Journal)tmp).getContext().replace("\r\n", " "));
			while(((Journal)tmp).getContext().indexOf("  ")>-1){
				((Journal)tmp).setContext(((Journal)tmp).getContext().replace("  ", " "));
			}
			if(((Journal)tmp).getContext().length()>288){
				((Journal)tmp).setContext(((Journal)tmp).getContext().substring(0,288));
			}
		}
		loadTypes((List<Journal>)data.getPageData());
		return data;
	}
	@CacheHandle(key=CacheFinal.JOURNAL_RANK_LIST ,validTime=60)
	public  List<Journal> loadJournalRank(String orderField){
		List<Journal> data=(List<Journal>) getCache(CacheFinal.JOURNAL_RANK_LIST+orderField);
		if(StringUtils.isNullOrEmpty(data)){
			writeRankCache();
			data=(List<Journal>) getCache(CacheFinal.JOURNAL_RANK_LIST+orderField);
		}
		loadTypes(data);
		return data;
	}
	public  void writeRankCache(){
		Pager<Journal> newPager = new Pager<Journal>(38, 1);
		newPager = loadRankNoCache(new Journal(), newPager, "id",
				true);
		List<Journal> newJournal = (List<Journal>) newPager.getPageData();
		addCache(CacheFinal.JOURNAL_RANK_LIST+"id", newJournal);
		Pager<Journal> hotPager = new Pager<Journal>(38, 1);
		hotPager =loadRankNoCache(new Journal(), hotPager,
				"views", true);
		List<Journal> hotJournal = (List<Journal>) hotPager.getPageData();
		addCache(CacheFinal.JOURNAL_RANK_LIST+"views", hotJournal);
	}
	public  Pager<Journal>  loadRankNoCache(Journal journal,Pager<Journal> pager,String orderField,boolean isDesc){
		Pager<Journal> data=(Pager<Journal>) baseService.findPagerByObject(journal, pager, orderField,isDesc);
		if(StringUtils.isNullOrEmpty(data)){
			return null;
		}
		if(StringUtils.isNullOrEmpty(data.getPageData())){
			return data;
		}
		for (Object tmp:data.getPageData()) {
			if(StringUtils.isNullOrEmpty(((Journal)tmp).getContext())){
				continue;
			}
			((Journal)tmp).setContext(HTMLSpirit.delHTMLTag(((Journal)tmp).getContext()));
			((Journal)tmp).setContext(((Journal)tmp).getContext().replace("\r\n", " "));
			while(((Journal)tmp).getContext().indexOf("  ")>-1){
				((Journal)tmp).setContext(((Journal)tmp).getContext().replace("  ", " "));
			}
			if(((Journal)tmp).getContext().length()>288){
				((Journal)tmp).setContext(((Journal)tmp).getContext().substring(0,288));
			}
		}
		loadTypes((List<Journal>)data.getPageData());
		return data;
	}
	@DelCacheHandle(keys={CacheFinal.JOURNAL_LIST,CacheFinal.JOURNAL_INFO})
	public void delJournal(Integer id) {
		baseService.delete(Journal.class, id);
	}
	@DelCacheHandle(keys={CacheFinal.JOURNAL_LIST,CacheFinal.JOURNAL_INFO})
	public void saveJournal(Journal journal) {
		baseService.saveOrUpdate(journal);
	}
	public  void addViewJournalNoCache(Integer id) {
		journalService.addViewJournalNoCache(id);
	}
	@CacheHandle(key=CacheFinal.JOURNAL_INFO ,validTime=600)
	public Journal getJournal(Integer id) {
		if(StringUtils.isNullOrEmpty(id)){
			return null;
		}
		Journal journal= (Journal) baseService.get(Journal.class, id);
		loadTypes(journal);
		return journal;
	}
	@CacheHandle(key=CacheFinal.JOURNAL_INFO ,validTime=60)
	public  Journal getJournal(String title) {
		if(StringUtils.isNullOrEmpty(title)){
			return null;
		}
		Journal journal= (Journal) baseService.findFirstByField(Journal.class, "title",title);
		loadTypes(journal);
		return journal;
	}
	private void loadTypes(Journal ... journals){
		if(StringUtils.isNullOrEmpty(journals)){
			return;
		}
		for (Journal journal:journals) {
			journal.setTypes(typeCache.getType(journal.getTypeId()));
		}
	}
	private void loadTypes(List<Journal> journals){
		if(StringUtils.isNullOrEmpty(journals)){
			return;
		}
		for (Journal journal:journals) {
			journal.setTypes(typeCache.getType(journal.getTypeId()));
		}
	}
}
