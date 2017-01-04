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
					<strong class="am-text-primary am-text-lg">内容管理</strong> / <small>文章列表</small>
				</div>
			</div>
			<hr>
			<div class="am-panel am-panel-default">
				<div class="am-panel-hd am-cf"
					data-am-collapse="{target: '#collapse-panel-base'}">
					文章列表<span class="am-icon-chevron-down am-fr"></span> <a
						href="${basePath }admin/journalEdit.${defSuffix}"
						class="am-btn am-btn-default am-round am-fr am-btn-xs"
						style="float: right">发布文章</a>
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
							<th>类别：</th>
							<th><input name="types.className" type="text"
								class="am-form-input  am-radius"
								value="${types_className }" /></th>
							<th>权限：</th>
							<th><select name="openLevel">
									<option value="">全部级别</option>
									<option value="1" <c:if test="${openLevel==1 }">selected</c:if>>对所有人公开</option>
									<option value="2" <c:if test="${openLevel==2 }">selected</c:if>>对所有人保密</option>
									<option value="3" <c:if test="${openLevel==3 }">selected</c:if>>添加阅读密码</option>
							</select></th>
							<th><a href="javascript:indexPage()"
								class="am-btn am-btn-default am-fr ">查询</a></th>

						</thead>

					</table>
					<table class="am-table am-table-bordered">
						<thead>
							<tr>
								<th>ID</th>
								<th>文章名称</th>
								<th>所属分类</th>
								<th>浏览次数</th>
								<th>公开级别</th>
								<th>发布时间</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<c:if test="${empty pager.pageData}">
								<tr>
									<td colspan="7" style="text-align:center ">暂无数据</td>
								</tr>
							</c:if>
							<c:forEach items="${pager.pageData }" var="journal">
								<tr>
									<td>${journal.id }</td>
									<td>${journal.title }</td>
									<td>${journal.types.className}</td>
									<td>${journal.views==null?0:journal.views }</td>
									<td>
									 <c:if test="${journal.openLevel==1 }">对所有人公开</c:if>
									 <c:if test="${journal.openLevel==2 }">对所有人保密</c:if>
									 <c:if test="${journal.openLevel==3 }">阅读密码:${journal.openPwd }</c:if>
									</td>
									<td>${journal.time}</td>
									<td><a
										href="${basePath }admin/journalEdit.${defSuffix}?id=${journal.id }">编辑</a>
										<a href="#" onclick="delDate(${journal.id })">删除</a></td>
								</tr>
							</c:forEach>
						</tbody>
						<thead>
							<tr class="am-disabled">
								<th colspan="7"><jsp:include page="../base/page.jsp" /></th>
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
			url : 'journalDel.${defSuffix}',
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
