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
					<strong class="am-text-primary am-text-lg">内容管理</strong> / <small>编辑类别</small>
				</div>
			</div>
			<hr>
			<div class="am-panel am-panel-default">
				<div class="am-panel-hd am-cf"
					data-am-collapse="{target: '#collapse-panel-4'}">
					编辑类别<span class="am-icon-chevron-down am-fr"></span>
				</div>
				<div id="collapse-panel-4" class="am-panel-bd am-collapse am-in">
					<div class="am-g">
						<div class="am-u-sm-12 am-u-md-4 am-u-md-push-8"></div>
						<div class="am-u-sm-12 am-u-md-8 am-u-md-pull-4">

							<div class="am-form-group">
								<label for="user-name" class="am-u-sm-3 am-form-label"><small>上级类别</small></label>
								<div class="am-u-sm-9">
									<input name="id" type="hidden" value="${type.id }" />
									<select name="parentClass" >
										<option value="">根类别</option>
										<c:forEach items="${types }" var="curr">
										<option value="${curr.id }" <c:if test="${type.parentClass==curr.id ||curr.id==parentId }">selected</c:if>>${curr.className }</option>
										</c:forEach>
									</select>
								</div>
							</div>

							<div class="am-form-group">
								<label  class="am-u-sm-3 am-form-label"><small>类别名称</small></label>
								<div class="am-u-sm-9">
									<input type="text" id="user-email"
										value="${type.className }" name="className"
										datatype="*2-100" placeholder="请输入网站关键字"
										errormsg="请输入正确的网站关键字(2-100位)" sucmsg="输入正确" nullmsg="请输入网站关键字">
								</div>
							</div>
						</div>
					</div>
					<div class="am-form-group">
						<div class="am-u-sm-9 am-u-sm-push-3">
							<button  class="am-btn am-btn-primary"
								 type="submit">保存</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</form>
</body>
<jsp:include page="base/js.jsp" />
<script>
	$("#dataform").Validform({
		label : ".lable",
		showAllError : false,
		postonce : true,
		tiptype : 3,
		beforeSubmit : function(curform) {
			saveSetting();
			return false;
		}
	});
	function saveSetting() {
		$.ajax({
			type : "POST",
			dataType : 'json',
			data : $("#dataform").serialize(),
			url : 'typeSave.${defSuffix}',
			timeout : 60000,
			success : function(json) {
				alert(json.msg);
				if (json.code == 0) {
					location.href='typeList.${defSuffix}?parentId=${parentId}';
				}

			},
			error : function() {
				alert("系统繁忙");
			}

		});
	}
</script>
</html>
