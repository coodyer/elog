package com.blog.web.controllers;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.blog.web.base.page.Pager;
import com.blog.web.base.thread.SysThreadPool;
import com.blog.web.cache.AuthorCache;
import com.blog.web.cache.JournalCache;
import com.blog.web.cache.LinkCache;
import com.blog.web.cache.NavCache;
import com.blog.web.cache.SettingCache;
import com.blog.web.cache.SiteMapCache;
import com.blog.web.cache.ToolsCache;
import com.blog.web.cache.TypeCache;
import com.blog.web.controllers.base.BaseController;
import com.blog.web.model.Author;
import com.blog.web.model.Journal;
import com.blog.web.model.Links;
import com.blog.web.model.Nav;
import com.blog.web.model.Setting;
import com.blog.web.model.SiteMap;
import com.blog.web.model.Tools;
import com.blog.web.model.Types;
import com.blog.web.util.StringUtils;

@org.springframework.stereotype.Controller
public class Controller extends BaseController {

	@Resource
	NavCache navCache;
	@Resource
	SettingCache settingCache;
	@Resource
	AuthorCache authorCache;
	@Resource
	TypeCache typeCache;
	@Resource
	JournalCache journalCache;
	@Resource
	LinkCache linkCache;
	@Resource
	SiteMapCache siteMapCache;
	@Resource
	ToolsCache toolsCache;

	@RequestMapping
	public String index(HttpServletRequest req, HttpServletResponse res) {
		loadJournals();
		baseLoding();
		keepParas();
		return "index";
	}

	@RequestMapping(value = { "journal_{page:\\d+}_{type:\\d+}" })
	public String journal(@PathVariable Integer page,
			@PathVariable Integer type, HttpServletRequest req) {
		Pager<Journal> pager = new Pager<Journal>();
		pager.setCurrentPage(page);
		Journal journal = new Journal();
		Types types = new Types();
		types.setId(type);
		journal.setTypes(types);
		loadJournals(journal, pager);
		baseLoding();
		setAttribute("currType", type);
		keepParas();
		return "index";
	}
	@RequestMapping(value = { "tools_{page:\\d+}" })
	public String toolsPager(@PathVariable Integer page, HttpServletRequest req) {
		Pager<Tools> pager = new Pager<Tools>();
		pager.setCurrentPage(page);
		pager.setPageSize(20);
		pager=toolsCache.loadUtilPager(new Tools(), pager);
		setAttribute("pager", pager);
		baseLoding();
		keepParas();
		return "tools";
	}
	@RequestMapping
	public String tools(HttpServletRequest req) {
		return toolsPager(1, req);
	}
	
	@RequestMapping(value = { "journal_{page:\\d+}" })
	public String journal(@PathVariable Integer page, HttpServletRequest req) {
		return journal(page, null, req);
	}

	@RequestMapping(value = { "article_{id:\\d+}" }, method = {
			RequestMethod.GET, RequestMethod.POST })
	public String article(@PathVariable Integer id, HttpServletRequest req) {
		req.setAttribute("article", id);
		if (id == null || id < 1) {
			return "404";
		}
		baseLoding();
		final Journal journal = journalCache.getJournal(id);
		if(StringUtils.isNullOrEmpty(journal)){
			return "404";
		}
		setAttribute("journal", journal);
		if (journal.getOpenLevel() == 3) {
			String openPwd = req.getParameter("password");
			if (!StringUtils.hasEmpty(openPwd, journal.getOpenPwd())
					&& journal.getOpenPwd().equals(openPwd)) {
				setSessionPara("journal" + journal.getId(), true);
			}
		}
		setSessionPara("journalOpen",
				getSessionPara("journal" + journal.getId()));
		if (!StringUtils.isNullOrEmpty(journal)) {
			setAttribute("keywords", journal.getTitle() + ",");
			SysThreadPool.threadPool.execute(new Runnable() {
				@Override
				public void run() {
					journalCache.addViewJournalNoCache(journal.getId());
				}
			});
			
		}
		return "journal";
	}
	private void baseLoding() {
		loadSiteInfo();
		loadTypes();
		loadAuthorInfo();
		loadNavs();
		loadJournalRank();
		loadLinks();
		keepParas();
	}

	private void loadLinks() {
		List<Links> links = linkCache.loadLinks();
		setAttribute("links", links);
	}

	private void loadNavs() {
		List<Nav> navs = navCache.loadNavs();
		setAttribute("navs", navs);
	}

	private void loadSiteInfo() {
		Setting setting = settingCache.loadSetting();
		setAttribute("setting", setting);
	}

	private void loadAuthorInfo() {
		Author author = authorCache.loadAuthor();
		setAttribute("author", author);
	}

	private void loadTypes() {
		List<Types> types = typeCache.loadTypes();
		setAttribute("types", types);
	}

	private void loadJournals() {
		Pager<Journal> pager = (Pager<Journal>) getBeanAll(Pager.class);
		Journal journal = (Journal) getBeanAll(Journal.class);
		loadJournals(journal, pager);
	}

	private synchronized void loadJournalRank() {
		List<Journal> newJournal = journalCache.loadJournalRank( "id");
		setAttribute("newJournal", newJournal);
		List<Journal> hotJournal = journalCache.loadJournalRank("views");
		setAttribute("hotJournal", hotJournal);
	}

	private void loadJournals(Journal journal, Pager<Journal> pager) {
		pager.setPageSize(12);
		pager = journalCache.loadJournal(journal, pager);
		setAttribute("pager", pager);
	}
	
	@RequestMapping
	private String robots() {
		return "robots";
	}

	@RequestMapping
	private String sitemap() {
		SiteMap siteMap = siteMapCache.loadSiteMap();
		setAttribute("siteMap", siteMap);
		loadSiteInfo();
		loadTypes();
		return "sitemap";
	}
}
