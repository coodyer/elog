<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<link href="assets/css/style.css" rel="stylesheet">
<h2 class="about_h" style="float:left;width: 100%;left:0">当前位置：<a href="${basePath }">首页</a>&gt;
<a href="${basePath }journal_1_${journal.types.id }.${defSuffix}">${journal.types.className }</a></h2>
<div class="index_about" id="journalInfo">
	<h2 class="c_titile"  style="max-width:100%;word-break:break-all">${journal.title }</h2>
	<p class="box_c">
		<span class="d_time">发布时间：<fmt:formatDate
				value="${journal.time}" pattern="yyyy-MM-dd HH:mm" /></span><span>编辑：${journal.author }</span>
		<span>浏览（${journal.views==null?0:journal.views})</span><span>类别:${journal.types.className }</span>
	</p>
	<c:if test="${journal.openLevel==1 || journalOpen==true }">
		<ul class="infos"  style="max-width:100%;word-break:break-all"><br/>${journal.context }</ul>
	</c:if>
	<c:if test="${journal.openLevel==3 && !journalOpen}">
		<jsp:include page="openpwd.jsp" />
	</c:if>
</div>
<jsp:include page="comment.jsp" />
<jsp:include page="renhome.jsp" />