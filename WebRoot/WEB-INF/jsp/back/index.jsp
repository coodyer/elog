<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<html class="no-js">
<head>
<jsp:include page="base/head.jsp" />
</head>
<body>
	<header class="am-topbar admin-header">
		<jsp:include page="base/nav.jsp" />
	</header>
	<!-- sidebar end -->
	<jsp:include page="base/menu.jsp" />
	<!-- content start -->
	<div class="admin-content" style="height: 95%">
		<iframe src="base.${defSuffix }" name="index" width="100%" id="index" 
			height="100%" style="background-color=transparent;"> </iframe>
	</div>
	<%-- <footer>
		<jsp:include page="base/foot.jsp" />
	</footer> --%>
	<jsp:include page="base/js.jsp" />
</body>

</html>
