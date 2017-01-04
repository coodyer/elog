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
					<strong class="am-text-primary am-text-lg">内容管理</strong> / <small>编辑栏目</small>
				</div>
			</div>
			<hr>
			<div class="am-panel am-panel-default">
				<div class="am-panel-hd am-cf"
					data-am-collapse="{target: '#collapse-panel-4'}">
					编辑栏目<span class="am-icon-chevron-down am-fr"></span>
				</div>
				<div id="collapse-panel-4" class="am-panel-bd am-collapse am-in">
					<div class="am-g">
						<div class="am-u-sm-12 am-u-md-4 am-u-md-push-8"></div>
						<div class="am-u-sm-12 am-u-md-8 am-u-md-pull-4">
							<div class="am-form-group">
								<label  class="am-u-sm-3 am-form-label"><small>栏目名称</small></label>
								<div class="am-u-sm-9">
								<input type="hidden" name="id" value="${nav.id }">
									<input type="text" id="user-email"
										value="${nav.title }" name="title"
										datatype="*2-10" placeholder="请输入栏目名称"
										errormsg="请输入正确的栏目名称(2-10位)" sucmsg="输入正确" nullmsg="请输入栏目名称">
								</div>
							</div>
							<div class="am-form-group">
								<label  class="am-u-sm-3 am-form-label"><small>栏目链接</small></label>
								<div class="am-u-sm-9">
									<input type="text" id="user-email"
										value="${nav.url }" name="url"
										datatype="*0-255" placeholder="请输入栏目链接"
										errormsg="请输入正确的栏目链接(0-255位)" sucmsg="输入正确" nullmsg="请输入栏目链接">
								</div>
							</div>
							<div class="am-form-group">
								<label  class="am-u-sm-3 am-form-label"><small>栏目排序</small></label>
								<div class="am-u-sm-9">
									<input type="text" id="user-email"
										value="${nav.sort }" name="sort"
										datatype="n1-2" placeholder="请输入栏目排序"
										errormsg="请输入正确的栏目排序(1-2位)" sucmsg="输入正确" nullmsg="请输入栏目排序">
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
			url : 'navSave.${defSuffix}',
			timeout : 60000,
			success : function(json) {
				alert(json.msg);
				if (json.code == 0) {
					location.href='navList.${defSuffix}';
				}

			},
			error : function() {
				alert("系统繁忙");
			}

		});
	}
</script>
</html>
