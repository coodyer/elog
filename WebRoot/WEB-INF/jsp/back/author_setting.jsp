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
					<strong class="am-text-primary am-text-lg">系统管理</strong> / <small>博主信息</small>
				</div>
			</div>
			<hr>
			<div class="am-panel am-panel-default">
				<div class="am-panel-hd am-cf"
					data-am-collapse="{target: '#collapse-panel-4'}">
					博主信息设置<span class="am-icon-chevron-down am-fr"></span>
				</div>
				<div id="collapse-panel-4" class="am-panel-bd am-collapse am-in">
					<div class="am-g">
						<div class="am-u-sm-12 am-u-md-4 am-u-md-push-8"></div>
						<div class="am-u-sm-12 am-u-md-8 am-u-md-pull-4">

							<div class="am-form-group">
								<label for="user-name" class="am-u-sm-3 am-form-label"><small>博客名字</small></label>
								<div class="am-u-sm-9">
									<input name="id" type="hidden" value="${author.id }" />
									<input type="text" id="user-name" value="${author.blogName }"
										name="blogName" datatype="*2-64" placeholder="请输入博客名字"
										errormsg="请输入正确的博客名字(2-64位)" sucmsg="输入正确" nullmsg="请输入博客名字">
								</div>
							</div>
							<div class="am-form-group">
								<label for="user-phone" class="am-u-sm-3 am-form-label"><small>博主昵称</small></label>
								<div class="am-u-sm-9">
									<input type="text" id="user-phone" value="${author.nickName }"
										name="nickName" datatype="*2-20" placeholder="请输入博主昵称"
										errormsg="请输入正确的博主昵称(2-20位)" sucmsg="输入正确" nullmsg="请输入博主昵称">
								</div>
							</div>
							<div class="am-form-group">
								<label for="user-phone" class="am-u-sm-3 am-form-label"><small>博主头像</small></label>
								<div class="am-u-sm-9">
									<img alt="${author.nickName}" src="${author.logo}"
										onerror="this.src='${basePath}/assets/images/logo.jpg'"
										style="max-height:60px;float: left;" id="imgShow">
									<p style="float: left">&nbsp;&nbsp;</p>
									<input type="hidden" name="logo" value="${author.logo}"
										id="journallogo" /> <input type="file" id="ajaxFile"
										placeholder="封面图片" value="${author.logo}" name="logoTmp"
										style="float: left;padding-top: 1rem;" onchange="fileUpload()">
								</div>
							</div>
							<div class="am-form-group">
								<label for="user-phone" class="am-u-sm-3 am-form-label"><small>邮箱</small></label>
								<div class="am-u-sm-9">
									<input type="text" id="user-phone" value="${author.email }"
										name="email" datatype="e" placeholder="请输入籍贯"
										errormsg="请输入正确的邮箱" sucmsg="输入正确" nullmsg="请输入邮箱">
								</div>
							</div>
							<div class="am-form-group">
								<label for="user-phone" class="am-u-sm-3 am-form-label"><small>博主年龄</small></label>
								<div class="am-u-sm-9">
									<input type="text" id="user-phone" value="${author.age }"
										name="age" datatype="n1-3" placeholder="请输入博主年龄"
										errormsg="请输入正确的博主年龄(1-3位整数)" sucmsg="输入正确" nullmsg="请输入博主年龄">
								</div>





							</div>
							<div class="am-form-group">
								<label for="user-phone" class="am-u-sm-3 am-form-label"><small>博主工作</small></label>
								<div class="am-u-sm-9">
									<input type="text" id="user-phone" value="${author.work }"
										name="work" datatype="*2-30" placeholder="请输入博主工作"
										errormsg="请输入正确的博主工作(2-30位)" sucmsg="输入正确" nullmsg="请输入博主工作">
								</div>
							</div>
							<div class="am-form-group">
								<label for="user-phone" class="am-u-sm-3 am-form-label"><small>籍贯</small></label>
								<div class="am-u-sm-9">
									<input type="text" id="user-phone" value="${author.place }"
										name="place" datatype="*2-64" placeholder="请输入籍贯"
										errormsg="请输入正确的籍贯(2-64位)" sucmsg="输入正确" nullmsg="请输入籍贯">
								</div>



							</div>
							<div class="am-form-group">
								<label class="am-u-sm-3 am-form-label"><small>签名</small></label>
								<div class="am-u-sm-9">
									<input type="text" id="user-email" value="${author.autograph }"
										name="autograph" datatype="*2-100" placeholder="请输入签名"
										errormsg="请输入正确的签名(2-100位)" sucmsg="输入正确" nullmsg="请输入网站关键字">
								</div>
							</div>




							<div class="am-form-group">
								<label for="user-QQ" class="am-u-sm-3 am-form-label"><small>底部版权</small></label>
								<div class="am-u-sm-9">
									<input type="text" id="user-QQ" value="${setting.copyright }"
										name="setting.copyright" datatype="*2-100"
										placeholder="请输入底部版权" errormsg="请输入正确的底部版权(2-100位)"
										sucmsg="输入正确" nullmsg="请输入底部版权">
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="am-form-group">
					<div class="am-u-sm-9 am-u-sm-push-3">
						<button class="am-btn am-btn-primary" type="submit">保存修改</button>
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
			url : 'authorSave.${defSuffix}',
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
