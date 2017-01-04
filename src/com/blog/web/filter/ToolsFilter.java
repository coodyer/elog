package com.blog.web.filter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.blog.web.cache.ToolsCache;
import com.blog.web.model.Tools;
import com.blog.web.util.PropertUtil;
import com.blog.web.util.RequestUtil;
import com.blog.web.util.SpringContextHelper;
import com.blog.web.util.StringUtils;

public class ToolsFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		ToolsCache toolsCache=(ToolsCache) SpringContextHelper.getBean("toolsCache");
		List<Tools> tools=toolsCache.loadUtils();
		if(StringUtils.isNullOrEmpty(tools)){
			arg2.doFilter(arg0, arg1);
			return;
		}
		
		Map<String, Tools> toolsMap=(Map<String, Tools>) PropertUtil.parsObjToMap(tools, "url");
		HttpServletRequest request = (HttpServletRequest) arg0;
		String url=getRequestURI(request);
		if(!toolsMap.containsKey(url)){
			arg2.doFilter(arg0, arg1);
			return;
		}
		Tools util=toolsMap.get(url);
		request.setAttribute("utils", util);
		request.getRequestDispatcher(util.getPath()).forward(arg0, arg1);
	}

	private String getRequestURI(HttpServletRequest request){
		String url=RequestUtil.getRequestUri(request);
		String suffix=RequestUtil.getReqSuffix(request);
		if(!StringUtils.isNullOrEmpty(suffix)){
			url=url.replace("."+suffix, "");
		}
		if(url.startsWith("/")){
			url=url.replace("/", "");
		}
		return url;
	}
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
