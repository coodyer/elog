<%@page import="com.blog.web.base.cache.CacheTimerHandler"%>
<%@page import="com.blog.web.entity.IpAddressEntity"%>
<%@page import="com.blog.web.util.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String url=request.getParameter("url");
	if(!StringUtils.isNullOrEmpty(url)){
		String key="URL_SHORT_HANDLE"+EncryptionUtil.md5Code(url);
		String sortUrl=(String)CacheTimerHandler.getCache(key);
		if(StringUtils.isNullOrEmpty(sortUrl)){
			sortUrl=SortUrlUtil.getSortUrl(url);
			if(!StringUtils.isNullOrEmpty(sortUrl)){
				CacheTimerHandler.addCache(key, sortUrl, 600);
			}
		}
		request.setAttribute("sortUrl", sortUrl.replace("<", "").replace(">", ""));
	}
	RequestUtil.keepParas(request);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<jsp:include page="header.jsp" />
</head>
<body>
	<jsp:include page="js.jsp" />
	<div class="am-g">
		<div class="am-u-md-12">
			<div class="am-panel am-panel-default">
				<div class="am-panel-hd">${utils.title }</div>
				<div class="am-panel-bd">
					<form action="" class="am-form" id="dataform" method="post">
						<fieldset>
							<div class="am-form-group">
								<label for="doc-vld-name-2"><small>URL地址：</small></label> <input
									type="text" id="doc-vld-name-2" name="url"
									placeholder="请输入要缩短的URL" value="${url }"> <span class="Validform_checktip"></span>
							</div>
							<p>
								<button type="submit" class="am-btn am-btn-danger am-round">
									<i class="am-icon-spinner am-icon-spin"></i> 生成
								</button>
							</p>
							<div class="am-form-group">
								<hr />
							</div>
							<div class="am-form-group">
								<label for="doc-vld-name-2"><small>短网址：</small></label><label
									style="color: #7A7D7E" for="userModule0"><small>${sortUrl}</small>
								</label>
							</div>
						</fieldset>
					</form>
				</div>
			</div>
			<jsp:include page="../comm/foot.jsp" />
		</div>
	</div>
</body>
</html>
