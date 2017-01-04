package com.blog.web.filter;

import java.io.IOException;
import java.util.List;

import javax.persistence.metamodel.SetAttribute;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.blog.web.cache.SuffixCache;
import com.blog.web.util.RequestUtil;
import com.blog.web.util.SpringContextHelper;
import com.blog.web.util.StringUtils;

public class SuffixFilter implements Filter {

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;
		String suffix = RequestUtil.getReqSuffix(request);
		request.setAttribute("suffix", suffix);
		SuffixCache suffixCache = (SuffixCache) SpringContextHelper
				.getBean("suffixCache");
		List<String> staList = suffixCache.loadStaSuffix();
		List<String> avaList = suffixCache.loadAvaSuffix();
		avaList.add("do");
		if (!suffix.equals("") && !staList.contains(suffix)
				&& !avaList.contains(suffix)) {
			request.getRequestDispatcher("/WEB-INF/jsp/404.jsp").forward(arg0,
					arg1);
			return;
		}
		String defSuffix = suffixCache.loadDefSuffix();
		if (StringUtils.isNullOrEmpty(defSuffix)) {
			defSuffix = "do";
		}
		if (staList.contains(suffix) && !suffix.equals(defSuffix)) {
			response.setHeader("Cache-Control", "max-age=600");
		}
		request.setAttribute("defSuffix", defSuffix);
		arg2.doFilter(arg0, arg1);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
}
