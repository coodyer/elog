<%@page import="com.blog.web.util.DateUtils"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<!doctype html>
<html class="no-js">
<head>
<jsp:include page="base/head.jsp" />
</head>
<body>
	<form class="am-form am-form-horizontal" method="post" id="dataform">
		<div class="admin-content">
			<div class="am-cf am-padding">
				<div class="am-fl am-cf">
					<strong class="am-text-primary am-text-lg">内容管理</strong> / <small>文章类别管理</small>
				</div>
			</div>
			<hr>
			<div class="am-panel am-panel-default">
				<div class="am-panel-hd am-cf"
					data-am-collapse="{target: '#collapse-panel-base'}">
					文章类别管理<span class="am-icon-chevron-down am-fr"
						style="display: none"></span><a href="${basePath }admin/typeEdit.${defSuffix}?parentId=${parentId}"
						class="am-btn am-btn-default am-round am-fr am-btn-xs"
						style="float: right">添加类别</a>
				</div>
				<div id="collapse-panel-base" class="am-panel-bd am-collapse am-in">
					<table class="am-table am-table-bordered">
						<thead>
							<tr>
								<th>ID</th>
								<th>类别名称</th>
								<th>子类数</th>
								<th>现有文章数</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
						<c:if test="${empty types}"><tr><td  colspan="4" style="text-align:center ">暂无数据</td></tr></c:if>
							<c:forEach items="${types }" var="type">
								<tr>
									<td>${type.id }</td>
									<td>${type.className }</td>
									<td>${(empty type.childTypes)?0:type.childTypes.size() }</td>
									<td>${type.count }</td>
									<td>
									<c:if test="${empty type.parentClass }">
									<a
										href="${basePath }admin/typeList.${defSuffix}?parentId=${type.id }">子类管理</a>
									</c:if>
									<a
										href="${basePath }admin/typeEdit.${defSuffix}?id=${type.id }">编辑</a>
										<a href="#" onclick="delDate(${type.id })">删除</a></td>
								</tr>
							</c:forEach>
						</tbody>
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
			data : 'id='+id,
			url : 'typeDel.${defSuffix}',
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
</html>
