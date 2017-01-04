<%@page import="com.blog.web.base.cache.CacheTimerHandler"%>
<%@page import="com.blog.web.entity.IpAddressEntity"%>
<%@page import="com.blog.web.util.*"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String ip=request.getParameter("currIp");
	if(StringUtils.isNullOrEmpty(ip)){
		ip=RequestUtil.getIpAddr(request);
	}
	if(!StringUtils.isNullOrEmpty(ip)){
	String key="IP_SEARCH_HANDLE"+EncryptionUtil.md5Code(ip);
	IpAddressEntity.AddressInfo ipInfo=(IpAddressEntity.AddressInfo)CacheTimerHandler.getCache(key);
	if(StringUtils.isNullOrEmpty(ipInfo)){
		ipInfo=IpAddressUtil.getAddress(ip);
		if(!StringUtils.isNullOrEmpty(ipInfo)){
			CacheTimerHandler.addCache(key, ipInfo, 600);
		}
	}
	request.setAttribute("ipInfo", ipInfo);
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
								<label for="doc-vld-name-2"><small>IP地址：</small></label> <input
									type="text" id="doc-vld-name-2" name="currIp"
									placeholder="请输入IP地址" value="${currIp }"> <span class="Validform_checktip"></span>
							</div>
							<p>
								<button type="submit" class="am-btn am-btn-danger am-round">
									<i class="am-icon-spinner am-icon-spin"></i> 查询
								</button>
							</p>
							<div class="am-form-group">
								<hr />
							</div>
							<div class="am-form-group">
								<label for="doc-vld-name-2"><small>IP地址：</small></label><label
									style="color: #7A7D7E" for="userModule0"><small>${ipInfo.ip}</small>
								</label>
							</div>
							<div class="am-form-group">
								<label for="doc-vld-name-2"><small>地区：</small></label><label
									style="color: #7A7D7E" for="userModule0"><small>${ipInfo.country}
										${ipInfo.region} ${ipInfo.city} ${ipInfo.area} ${ipInfo.isp} </small>
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
