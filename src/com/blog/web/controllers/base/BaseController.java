package com.blog.web.controllers.base;

import java.util.Date;
import java.util.Enumeration;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.blog.web.cache.SuffixCache;
import com.blog.web.cache.base.BaseCache;
import com.blog.web.entity.IpAddressEntity;
import com.blog.web.model.Admin;
import com.blog.web.util.DateUtils;
import com.blog.web.util.JSONWriter;
import com.blog.web.util.RequestUtil;
import com.blog.web.util.SpringContextHelper;
import com.blog.web.util.StringUtils;

public abstract class BaseController {
	@Resource
	protected BaseCache baseCache;
	@Autowired
	protected HttpServletRequest request;

	protected void keepParas() {
		RequestUtil.keepParas(request);
	}
	protected String loadBasePath(HttpServletRequest request) {
		String path = request.getContextPath();
		String basePath = request.getScheme()
				+ "://"
				+ request.getServerName()
				+ (request.getServerPort() == 80 ? "" : ":"
						+ request.getServerPort()) + path + "/";
		request.getSession().setAttribute("basePath", basePath);
		request.setAttribute("basePath", basePath);
		return basePath;
	}
	protected String getDefSuffix(){
		SuffixCache suffixCache = (SuffixCache) SpringContextHelper
				.getBean("suffixCache");
		String defSuffix = suffixCache.loadDefSuffix();
		if (StringUtils.isNullOrEmpty(defSuffix)) {
			defSuffix = "jspx";
		}
		return defSuffix;
	}
	protected String getPara(String paraName) {
		return request.getParameter(paraName);
	}

	protected Integer getParaInteger(String paraName) {
		return StringUtils.toInteger(request.getParameter(paraName));
	}

	protected Integer[] getParaIntegers(String paraName) {
		String[] paras = request.getParameterValues(paraName);
		if (StringUtils.isNullOrEmpty(paras)) {
			return null;
		}
		Integer[] values = new Integer[paras.length];
		for (int i = 0; i < paras.length; i++) {
			try {
				values[i] = Integer.valueOf(paras[i]);
			} catch (Exception e) {
			}
		}
		return values;
	}

	protected Double getParaDouble(String paraName) {
		return StringUtils.toDouble(request.getParameter(paraName));
	}

	protected Float getParaFloat(String paraName) {
		return StringUtils.toFloat(request.getParameter(paraName));
	}

	protected Long getParaLong(String paraName) {
		return StringUtils.toLong(request.getParameter(paraName));
	}

	protected Date getParaDate(String paraName) {
		try {
			return DateUtils.stringToDate(request.getParameter(paraName));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	protected Date getParaDateTime(String paraName) {
		try {
			return DateUtils.stringToDate(request.getParameter(paraName),
					DateUtils.DATETIME_PATTERN);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	protected Object getBeanAll(Object obj) {
		return RequestUtil.getBeanAll(request, obj);
	}

	protected Object getBeanAccept(Object obj, String... paras) {
		return RequestUtil.getBeanAccept(request, obj, paras);
	}

	protected Object getBeanRemove(Object obj, String... paras) {
		return RequestUtil.getBeanRemove(request, obj, paras);
	}

	protected Object getBeanAll(String fatherName, Object obj) {
		return RequestUtil.getBeanAll(request, fatherName, obj);
	}

	protected Object getBeanAccept(String fatherName, Object obj,
			String... paras) {
		return RequestUtil.getBeanAccept(request, fatherName, obj, paras);
	}

	protected Object getBeanRemove(String fatherName, Object obj,
			String... paras) {
		return RequestUtil.getBeanRemove(request, fatherName, obj, paras);
	}

	protected Object getSessionPara(String paraName) {
		return request.getSession().getAttribute(paraName);
	}

	protected void setSessionPara(String paraName, Object obj) {
		request.getSession().setAttribute(paraName, obj);
	}

	protected Object getAttribute(String paraName) {
		return request.getAttribute(paraName);
	}

	protected void setAttribute(String paraName, Object obj) {
		request.setAttribute(paraName, obj);
	}

	protected String getHeader(String paraName) {
		return request.getHeader(paraName);
	}

	// 打印返回消息
	protected void printMsg(HttpServletResponse res, Object obj) {
		try {
			String msg = JSONWriter.write(obj);
			System.out.println(msg);
			res.getWriter().print(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	} // 打印返回消息

	protected void print(HttpServletResponse res, String str) {
		try {
			System.out.println(str);
			res.getWriter().print(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	protected String[] getParas(String paraName) {
		return request.getParameterValues(paraName);
	}
	protected void loadIpAddress(String ip) {
		if(StringUtils.isNullOrEmpty(ip)){
			return;
		}
		IpAddressEntity.AddressInfo address=baseCache.getIpAddress(ip);
		setAttribute("ip_address", address);
	}
	
	protected void delSessionAdmin(){
		RequestUtil.setAdmin(request, null);
	}
	protected void setSessionAdmin(Admin admin){
		RequestUtil.setAdmin(request, admin);
	}
	protected Admin getSessionAdmin(){
		return (Admin) RequestUtil.getAdmin(request);
	}
}
