<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!doctype html>
<html>
<jsp:include page="comm/header.jsp" />
<body>
	<div class="ibody">
<jsp:include page="comm/top.jsp" />
		<article>
<jsp:include page="comm/banner.jsp" />
<jsp:include page="comm/about.jsp" />
<div class="clear"></div>
<jsp:include page="comm/renhome.jsp" />
<p style="height: 10px;"></p>
		</article>
		<aside>
<script src="<%=basePath%>assets/js/silder.js"></script>>
	<jsp:include page="comm/author.jsp" />
	<jsp:include page="comm/class.jsp" />
	<jsp:include page="comm/links.jsp" />
<jsp:include page="comm/foot.jsp" />
</aside>
<div class="clear"></div>
</div>
</body>
</html>

