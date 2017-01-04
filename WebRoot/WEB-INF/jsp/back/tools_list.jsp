<%@page import="com.blog.web.util.DateUtils"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<!doctype html>
<html class="no-js">
<head>
<jsp:include page="base/head.jsp" />
</head>
<body>
	<form class="am-form am-form-horizontal" method="post" id="dataform"
		name="searchForm">
		<div class="admin-content">
			<div class="am-cf am-padding">
				<div class="am-fl am-cf">
					<strong class="am-text-primary am-text-lg">内容管理</strong> / <small>站长工具管理</small>
				</div>
			</div>
			<hr>
			<div class="am-panel am-panel-default">
				<div class="am-panel-hd am-cf"
					data-am-collapse="{target: '#collapse-panel-base'}">
					站长工具列表<span class="am-icon-chevron-down am-fr"></span> <a
						href="${basePath }admin/toolsEdit.${defSuffix}"
						class="am-btn am-btn-default am-round am-fr am-btn-xs"
						style="float: right">添加工具</a>
				</div>
				<div id="collapse-panel-base" class="am-panel-bd am-collapse am-in">
					<table class="am-table am-table-bordered">
						<thead>
						<th style="width: 40px">ID:</th>
							<th ><input name="id" class="am-form-input  am-radius"
								type="email" 
								value="${id }" /></th>
							<th>标题:</th>
							<th><input name="title" class="am-form-input  am-radius"
								type="email" 
								value="${title }" /></th>
							<th>访问路劲：</th>
							<th><input name="url" type="text"
								class="am-form-input  am-radius"
								value="${url }" /></th>
							<th><a href="javascript:indexPage()"
								class="am-btn am-btn-default am-fr ">查询</a></th>
						</thead>

					</table>
					<table class="am-table am-table-bordered">
						<thead>
							<tr>
								<th>ID</th>
								<th>标题</th>
								<th>访问URL</th>
								<th>解析文件路劲</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<c:if test="${empty pager.pageData}">
								<tr>
									<td colspan="5" style="text-align:center ">暂无数据</td>
								</tr>
							</c:if>
							<c:forEach items="${pager.pageData }" var="tools">
								<tr>
									<td>${tools.id }</td>
									<td>${tools.title }</td>
									<td>${tools.url}</td>
									<td>${tools.path }</td>
									<td><a
										href="${basePath }admin/toolsEdit.${defSuffix}?id=${tools.id }">编辑</a>
										<a href="#" onclick="delDate(${tools.id })">删除</a></td>
								</tr>
							</c:forEach>
						</tbody>
						<thead>
							<tr class="am-disabled">
								<th colspan="5"><jsp:include page="../base/page.jsp" /></th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</form>
</body>
<jsp:include page="base/js.jsp" />
<script>
	function delDate(id) {
		if (!confirm("数据删除后将无法恢复,是否继续操作?")) {
			return;
		}
		$.ajax({
			type : "POST",
			dataType : 'json',
			data : 'id=' + id,
			url : 'toolsDel.${defSuffix}',
			timeout : 60000,
			success : function(json) {
				alert(json.msg);
				if (json.code == 0) {
					location.reload(true);
				}

			},
			error : function() {
				alert("系统繁忙");
			}

		});
	}
</script>
<style>
input {
	padding: 0em;
}
</style>
</html>
