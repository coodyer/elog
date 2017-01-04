<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<div class="links">
		<h2>
			<p>友情链接</p>
		</h2>
		<ul id="navigation">
			<c:forEach items="${links }" var="link">
				<li><a href="${link.url }">${link.title }</a></li>
			</c:forEach>
		</ul>
	</div>