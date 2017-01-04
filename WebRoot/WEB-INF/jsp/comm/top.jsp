<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<header>
	<h1 style="font-family:楷体, 楷体_GB2312;">
		<strong>${author.blogName }</strong>
	</h1>
	<h2>
		<span
			style="font-size:14px;font-weight:normal;font-family:楷体, 楷体_GB2312;display:inline-block;color:white;text-shadow:rgb(127, 127, 127) 1px 0px 4px, rgb(127, 127, 127) 0px 1px 4px, rgb(127, 127, 127) 0px -1px 4px, rgb(127, 127, 127) -1px 0px 4px">${author.autograph }</span>
	</h2>
	<div class="logo">
		<a href="/"></a>
	</div>

	<nav id="topnav">
		<c:forEach items="${navs }" var="nav" varStatus="index">
			<a href="${nav.url }?navId=${nav.id}"
				<c:if test="${nav.id==navId}">id="topnav_current"</c:if>
				<c:if test="${navId==null&&index.index==0 }">id="topnav_current"</c:if>>${nav.title }</a>
		</c:forEach>
	</nav>
</header>