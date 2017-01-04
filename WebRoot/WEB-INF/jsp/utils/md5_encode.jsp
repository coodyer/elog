<%@page import="com.blog.web.base.cache.CacheTimerHandler"%>
<%@page import="com.blog.web.entity.HttpEntity"%>
<%@page import="com.blog.web.util.*"%>
<%@page import="java.text.MessageFormat"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String curr=request.getParameter("curr");
	RequestUtil.keepParas(request);
	if(!StringUtils.isNullOrEmpty(curr)){
		String key="MD5_QUERY_HANDLE"+curr;
		String result=(String)CacheTimerHandler.getCache(key);
		if(StringUtils.isNullOrEmpty(result)){
			result=md5Query(curr);
			if(!StringUtils.isNullOrEmpty(result)){
				CacheTimerHandler.addCache(key, result, 600);
			}
		}
		request.setAttribute("result", result.replace("<", "").replace(">", ""));
		request.setAttribute("curr", curr.replace("<", "").replace(">", "").replace("\"", "").replace("\"", ""));
	}
	
%>

<%!
public String md5Query(String md5){
	try {
		String url="http://www.somd5.com/somd5-index-md5.html";
		String key="VsgU7LYtSIX6UKriPNn6aYg";
		String postData="isajax={0}&md5={1}";
		postData=MessageFormat.format(postData, key,md5);
		HttpEntity entity=HttpUtil.Post(url, postData);
		String result=StringUtils.textCutCenter(entity.getHtml(), "<h1 style=\"display:inline;\">", "</h1>");
		return result;
	} catch (Exception e) {
		e.printStackTrace();
		return null;
	}
}
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
								<label for="doc-vld-name-2"><small>IP地址：</small></label> <input
									type="text" id="curr" name="curr"
									placeholder="请输入MD5密文或待加密的明文" value="${curr }">
							</div>
							<p>
								<button type="button" onclick="encode()"
									class="am-btn am-btn-danger am-round">
									<i class="am-icon-spinner am-icon-spin"></i> 加密
								</button>
								<button type="submit"  
									class="am-btn am-btn-success am-round">
									<i class="am-icon-spinner am-icon-spin"></i> 解密
								</button>
							</p>
							<div class="am-form-group">
								<hr />
							</div>
								<div class="am-form-group">
								<label for="doc-vld-name-2"><small>结果：</small></label><label
									style="color: #7A7D7E" for="userModule0"><small id="result">${result}</small>
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
<script src="${basePath }assets/js/md5.js"></script>
<script type='text/javascript'>
	function encode() {
		var htm= hex_md5($('#curr').val());
		$('#result').html(htm);
	}
</script>
</html>
