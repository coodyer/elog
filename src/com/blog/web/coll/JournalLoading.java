package com.blog.web.coll;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.blog.web.base.dao.JDBCUtil;
import com.blog.web.base.page.Pager;
import com.blog.web.cache.JournalCache;
import com.blog.web.cache.TypeCache;
import com.blog.web.entity.HttpEntity;
import com.blog.web.model.Types;
import com.blog.web.util.HttpUtil;
import com.blog.web.util.StringUtils;
import com.blog.web.util.UploadUtil;

@Service
public class JournalLoading {

	private final static String articlePattern = "<a href=\"{url}\\d{1,15}.html\" target=\"_blank\">.*?</a>";
	private HttpEntity hEntity = null;
	private final String contextPattern = "<div class=\"c_txt\">.*?</div>(?=(<div class=))";
	private final String imgPattern = "<img.*?src=\"([^\"]*)\".*?";
	private final String baseUrl = "http://www.myexception.cn";

	@Resource
	JDBCUtil jDBCUtil;

	@Resource
	TypeCache typeCache;

	@Resource
	JournalCache journalCache;

	@Resource
	TypesLoading typesLoading;

	// 加载页码文章列表
	private Pager<JournalVO> loadPager(Types type, Pager<JournalVO> pager) {
		String url = getPagerUrl(type.getOtherUrl(), pager.getCurrentPage());
		System.out.println(type.getClassName() + ",[页码:"
				+ pager.getCurrentPage() + "]抓取页面文章列表============");
		JournalVO vo = null;
		List<JournalVO> vos = new ArrayList<JournalVO>();
		try {
			hEntity = HttpUtil.Get(url);
			if (hEntity == null || hEntity.getCode() != 200) {
				System.out.println(type.getClassName() + ",页面内容为空============");
				return null;
			}
			String html = hEntity.getHtml();
			String pattern = articlePattern
					.replace("{url}", type.getOtherUrl());
			List<String> artices = StringUtils.doMatcher(html, pattern);
			if (StringUtils.isNullOrEmpty(artices)) {
				System.out.println("无任何文章:" + url);
				return null;
			}
			for (String artice : artices) {
				if (StringUtils.isNullOrEmpty(artice)) {
					continue;
				}
				vo = new JournalVO();
				vo.setTitle(StringUtils.textCutCenter(artice, ">", "<"));
				vo.setUrl(StringUtils.textCutCenter(artice, "href=\"", "\""));
				vos.add(vo);
			}
			// 加载总页数
			if (StringUtils.isNullOrEmpty(pager.getTotalPages())
					|| pager.getTotalPages() == 1) {
				html = StringUtils.textCutCenter(html, "<div class=\"c_p_s\">",
						"</div>");
				String[] pageA = html.split("</li>");
				for (int i = 0; i < pageA.length; i++) {
					String page = StringUtils.textCutCenter(pageA[i],
							"href=\"index_", ".html");
					if (StringUtils.isNumber(page)) {
						pager.setTotalPages(StringUtils.toInteger(page));
					}
				}
			}
			System.out.println(type.getClassName() + ",文章列表获取到:"
					+ pager.getTotalPages() + "页============");
			pager.setPageData(vos);
			return pager;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 加载单篇文章内容
	private void loadJournalContext(JournalVO vo, Types types) {
		if (StringUtils.isNullOrEmpty(vo.getTitle())) {
			System.out.println(types.getClassName() + "," + vo.getTitle()
					+ ",文章标题为空:");
			return;
		}
		System.out.println(types.getClassName() + "," + vo.getTitle()
				+ ",获得文章页面信息=============");
		Map<String, String> imgMap=new HashMap<String, String>();
		String context = getJournalContext(vo.getUrl(),imgMap);
		if (StringUtils.isNullOrEmpty(context)) {
			System.out.println(types.getClassName() + "," + vo.getTitle()
					+ ",文章内容为空=============");
			return;
		}
		String sql = "insert into journal(type_id,title,context,views,author,time,open_level,logo) values(?,?,?,?,?,?,?,?)";
		Map<Integer, Object> paraMap = new HashMap<Integer, Object>();
		paraMap.put(1, types.getId());
		paraMap.put(2, vo.getTitle());
		paraMap.put(3, context);
		paraMap.put(4, StringUtils.getRanDom(400, 1200));
		paraMap.put(5, "websos");
		paraMap.put(6, new Date());
		paraMap.put(7, 1);
		paraMap.put(
				8,
				MessageFormat.format("assets/journal/logo ({0}).jpg",
						String.valueOf(StringUtils.getRanDom(1, 89))));
		try {
			Integer code=jDBCUtil.querySqlUpdate(sql, paraMap);
			if(code<1){
				delDownImg(imgMap);
			}
		} catch (Exception e) {
			System.out.println(types.getClassName() + "," + vo.getTitle()
					+ ",文章已存在=============");
			delDownImg(imgMap);
			return;
		}
	}
/*	private boolean hasJournal(String title) {
		List<Record> journals = jDBCUtil.findByField(Journal.class, "title",
				title);
		return !StringUtils.isNullOrEmpty(journals);
	}*/
	private String getJournalContext(String url,Map<String, String> map) {
		hEntity = HttpUtil.Get(url);
		if (hEntity == null || StringUtils.isNullOrEmpty(hEntity.getHtml())) {
			return null;
		}
		String html = hEntity.getHtml();
		try {
			Thread.sleep(50);
			String context = StringUtils.doMatcherFirst(html, contextPattern);
			if (StringUtils.isNullOrEmpty(context)) {
				System.out.println("文章内容为空:" + url);
				return null;
			}
			if (context.length() < 2500) {
				System.out.println("文章内容过短:" + url);
				return null;
			}
			// 加载图片列表
			List<String> images =getImages(context, imgPattern);
			if (StringUtils.isNullOrEmpty(images)) {
				return context;
			}
			// 下载图片
			context=parsImage(context, images,map);
			return context;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	private List<String> getImages(String context, String pat){
			try {
				List<String> images = new ArrayList<String>();
				Integer index = 0;
				Pattern pattern = Pattern.compile(pat, Pattern.DOTALL);
				Matcher matcher = pattern.matcher(context);
				String tmp = null;
				while (matcher.find(index)) {
					tmp = matcher.group(1);
					index = matcher.end();
					if (StringUtils.isNullOrEmpty(tmp)) {
						continue;
					}
					images.add(tmp);
				}
				return images;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
	}
	private String parsImage(String context, List<String> imageUrls,Map<String, String> map) throws Exception {
		for (String imageUrl : imageUrls) {
			try {
				String imgPath = map.get(imageUrl);
				if (!StringUtils.isNullOrEmpty(imgPath)) {
					context = context.replace(imageUrl, imgPath);
					continue;
				}
				// 下载图片
				imgPath = downImg(baseUrl, imageUrl);
				Thread.sleep(20);
				System.out.println("下载图片:" + imageUrl);
				// 替换超链接图片
				if (!StringUtils.isNullOrEmpty(imgPath)) {
					map.put(imageUrl, imgPath);
					context = context.replace(imageUrl, imgPath);
					continue;
				}
				// 删除已下载图片
				delDownImg(map);
				throw  new Exception("图片下载失败");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(baseUrl + imageUrl + "图片下载失败!");
				// 删除已下载图片
				delDownImg(map);
				throw  new Exception("图片下载失败");
			}
		}
		return context;
	}
	private void delDownImg(Map<String, String> fileMap) {
		if (StringUtils.isNullOrEmpty(fileMap)) {
			return;
		}
		String basePath = System.getProperty("tansungWeb.root");
		for (String key : fileMap.keySet()) {
			String imgPath = basePath + fileMap.get(key);
			try {
				System.out.println("删除图片:" + imgPath);
				new File(imgPath).delete();
			} catch (Exception e) {
			}
		}
	}
	private String downImg(String baseUrl, String imgUri) {
		try {
			return UploadUtil.doDown(baseUrl + imgUri);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 加载文章列表内容
	private void loadJournalInfo(Pager<JournalVO> pager, Types types) {
		System.out.println(types.getClassName() + ",获取文章内容=============");
		if (StringUtils.isNullOrEmpty(pager)) {
			pager=new Pager<JournalVO>();
			System.out.println(types.getClassName() + ",页面为空=============");
			pager.setCurrentPage(1);
			pager.setTotalPages(1);
			return;
		}
		if (StringUtils.isNullOrEmpty(pager.getPageData())) {
			System.out.println(types.getClassName() + ",页面无数据=============");
			pager=new Pager<JournalVO>();
			pager.setCurrentPage(1);
			pager.setTotalPages(1);
			return;
		}
		List<JournalVO> vos = (List<JournalVO>) pager.getPageData();
		for (JournalVO vo : vos) {
			try {
				loadJournalContext(vo, types);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	// 格式化分页URL
	private String getPagerUrl(String url, Integer page) {
		if (page == 1) {
			return url + "/index.html";
		}
		return url + "/index_" + String.valueOf(page) + ".html";
	}
	// 更新本地类别列表，并随机获取一个类别
	private Types getAType() {
		try {
			List<Types> types = typesLoading.loadTypes();
			if (StringUtils.isNullOrEmpty(types)) {
				return null;
			}
			Collections.shuffle(types);
			return types.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public void loadJournal() {
		System.out.println("加载文章类别列表============");
		Types type = getAType();
		if (StringUtils.isNullOrEmpty(type)) {
			System.out.println("文章类别列表为空,放弃本次采集============");
			return;
		}
		Pager<JournalVO> pager = new Pager<JournalVO>(1);
		pager = loadPager(type, pager);
		loadJournalInfo(pager, type);
		while (pager.getCurrentPage() < pager.getTotalPages()
				&& pager.getCurrentPage() < 5000) {
			try {
				pager.setCurrentPage(pager.getCurrentPage() + 1);
				pager = loadPager(type, pager);
				Thread.sleep(100);
				loadJournalInfo(pager, type);
				if (pager.getCurrentPage() % 10 == 0) {
					Thread.sleep(5000);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		Integer max = new Integer(100);
		Integer min = new Integer(10);
		Integer a = min;
		Integer b = max / min;
		System.out.println(a == b);
	}
}
