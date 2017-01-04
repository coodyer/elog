package com.blog.web.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.blog.web.cache.MenuCache;
import com.blog.web.cache.RoleCache;
import com.blog.web.model.Admin;
import com.blog.web.model.Menus;
import com.blog.web.model.Role;
import com.blog.web.util.RequestUtil;
import com.blog.web.util.SpringContextHelper;
import com.blog.web.util.StringUtils;

public class AdminFilter implements Filter {

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;
		if(RequestUtil.isFilterExcluded(request, exculdeUrls)){
			arg2.doFilter(arg0, arg1);
			return;
		}
		String uri = getRequestUri(request);
		if (uri.equals("login") || uri.equals("doLogin")) {
			arg2.doFilter(arg0, arg1);
			return;
		}
		Admin admin = (Admin) RequestUtil.getAdmin(request);
		if (RequestUtil.isAdminLogin(request)) {
			getRoleMenus(request, admin.getRole().getId());
			arg2.doFilter(arg0, arg1);
			return;
		}
		response.sendRedirect(request.getAttribute("basePath") + "admin/login."
				+ request.getAttribute("defSuffix"));
	}

	private String[] exculdeUrls;
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String excludes = filterConfig.getInitParameter("excludes");
		if (!StringUtils.isNullOrEmpty(excludes))
			exculdeUrls = excludes.split(",");
	}

	private void getRoleMenus(HttpServletRequest request, Integer roleId) {
		RoleCache roleCache = (RoleCache) SpringContextHelper
				.getBean("roleCache");
		Role role = roleCache.loadRole(roleId);
		MenuCache menuCache = (MenuCache) SpringContextHelper
				.getBean("menuCache");
		List<Menus> roleMenus = menuCache.loadRoleMenus(role);
		request.setAttribute("adminMenus", roleMenus);

	}

	private String getRequestUri(HttpServletRequest request) {
		String uri = request.getRequestURI();
		String[] uris = uri.split("/admin/");
		if (uris.length == 1) {
			return "";
		}
		uris = uris[uris.length - 1].split("\\.");
		return uris[0];
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}
