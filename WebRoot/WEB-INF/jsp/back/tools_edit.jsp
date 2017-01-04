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
					<strong class="am-text-primary am-text-lg">内容管理</strong> / <small>工具编辑</small>
				</div>
			</div>
			<hr>
			<div class="am-panel am-panel-default">
				<div class="am-panel-hd am-cf"
					data-am-collapse="{target: '#collapse-panel-4'}">
					工具编辑<span class="am-icon-chevron-down am-fr"></span>
				</div>
				<div id="collapse-panel-4" class="am-panel-bd am-collapse am-in">
					<div class="am-g">
						<div class="am-u-sm-12 am-u-md-4 am-u-md-push-8"></div>
						<div class="am-u-sm-12 am-u-md-8 am-u-md-pull-4">
							<div class="am-form-group">
								<label for="user-name" class="am-u-sm-3 am-form-label"><small>链接标题</small></label>
								<div class="am-u-sm-9">
									<input name="id" type="hidden" value="${tools.id }" />
									<input type="text" id="user-email"
										value="${tools.title }" name="title"
										datatype="*2-100" placeholder="请输入标题"
										errormsg="请输入正确的标题(2-100位)" sucmsg="输入正确" nullmsg="请输入标题">
								</div>
							</div>
							<div class="am-form-group">
								<label for="user-phone" class="am-u-sm-3 am-form-label"><small>logo图片</small></label>
								<div class="am-u-sm-9">
									<img alt="${tools.logo}" src="${tools.logo}"
										onerror="this.src='${basePath}/assets/images/logo.jpg'"
										style="max-height:60px;float: left;" id="imgShow">
									<p style="float: left">&nbsp;&nbsp;</p>
									<input type="hidden" name="logo" value="${tools.logo==null?(basePath+'/assets/images/logo.jpg'):tools.logo}"
										id="journallogo" /> <input type="file" id="ajaxFile"
										placeholder="封面图片" value="${tools.logo}" name="logoTmp"
										style="float: left;padding-top: 1rem;" onchange="fileUpload()">
								</div>
							</div>
							<div class="am-form-group">
								<label  class="am-u-sm-3 am-form-label"><small>链接URL</small></label>
								<div class="am-u-sm-9">
									<input type="text" id="user-email"
										value="${tools.url }" name="url"
										datatype="*2-200" placeholder="请输入访问URL"
										errormsg="请输入正确的访问URL(2-200位)" sucmsg="输入正确" nullmsg="请输入访问URL">
								</div>
							</div>
							<div class="am-form-group">
								<label  class="am-u-sm-3 am-form-label"><small>解析路劲</small></label>
								<div class="am-u-sm-9">
									<input type="text" id="user-email"
										value="${tools.path }" name="path"
										datatype="*2-200" placeholder="请输入解析路劲"
										errormsg="请输入解析路劲(2-200位)" sucmsg="输入正确" nullmsg="请输入解析路劲">
								</div>
							</div>
							<div class="am-form-group">
								<label  class="am-u-sm-3 am-form-label"><small>工具说明</small></label>
								<div class="am-u-sm-9">
								<textarea style="height:17.5rem" name="remark"
										datatype="*2-500" placeholder="请输入工具说明"
										errormsg="请输入工具说明(2-500位)" sucmsg="输入正确" nullmsg="请输入工具说明">${tools.remark }</textarea>
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
<script src="${basePath }/assets/js/ajaxfileupload.js"></script>
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
			url : 'toolsSave.${defSuffix}',
			timeout : 60000,
			success : function(json) {
				alert(json.msg);
				if (json.code == 0) {
					location.href='linkList.${defSuffix}';
				}

			},
			error : function() {
				alert("系统繁忙");
			}

		});
	}
	function fileUpload() {
		$.ajaxFileUpload({
			url : 'upload.${defSuffix}',
			secureuri : false,
			fileElementId : 'ajaxFile',//file标签的id  
			dataType : 'json',//返回数据的类型  
			success : function(data) {
				if (data.code == 0) {
					$("#imgShow").attr("src", data.datas);
					$("#journallogo").val(data.datas);
				}
				alert(data.msg);

			},
			error : function(data, status, e) {
			}
		});
	}
</script>
</html>
