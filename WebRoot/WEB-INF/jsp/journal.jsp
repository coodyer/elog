<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!doctype html>
<html>
<jsp:include page="comm/header.jsp" />
<body>
	<div class="ibody">
		<jsp:include page="comm/top.jsp" />
		<article>
			<jsp:include page="comm/banner.jsp" />
			<jsp:include page="comm/journal_info.jsp" />
		</article>
		<aside>
			<jsp:include page="comm/author.jsp" />
			<jsp:include page="comm/class.jsp" />
			<jsp:include page="comm/links.jsp" />
			<jsp:include page="comm/foot.jsp" />
		</aside>
		<div class="clear"></div>
		<!-- 清除浮动 -->
	</div>
	<style>
img {
	max-width: 100%;
}
</style>
</body>
</html>
