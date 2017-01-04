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
					<strong class="am-text-primary am-text-lg">权限管理</strong> / <small>后台账户编辑</small>
				</div>
			</div>
			<hr>
			<table class="am-table am-table-bordered">
				<tr>
					<th style="width: 8%">所属子类</th>
					<td colspan="3"><select id="minType" name="types_id">
							<option value="">请选择文章类别</option>
							<c:forEach items="${types }" var="big" varStatus="index">
								<optgroup label="${big.className }">
									<c:forEach items="${big.childTypes }" var="type">
										<option value="${type.id }"
											<c:if test="${journal.types.id==type.id }">selected</c:if>>${type.className }</option>
									</c:forEach>
								</optgroup>
							</c:forEach>

					</select></td>
				</tr>
				<tr>
					<th style="width: 8%">文章标题</th>
					<td colspan="3"><input type="hidden" name="id"
						value="${journal.id }"><input type="text" id="title"
						placeholder="文章标题" value="${journal.title}" name="title">
						<small>请输入文章标题</small></td>
				</tr>
				<tr>
					<th>封面图片</th>
					<td colspan="3"><img alt="${journal.title}"
						src="${journal.logo==null?'/assets/images/default_journal_logo.jpg':journal.logo}"
						onerror="this.src='${basePath}/assets/images/default_journal_logo.jpg'"
						style="max-height:60px;float: left;" id="imgShow">
						<p style="float: left">&nbsp;&nbsp;</p> <input type="hidden"
						name="logo" value="${journal.logo}" id="journallogo" /> <input
						type="file" id="ajaxFile" placeholder="封面图片"
						value="${journal.logo}" name="logoTmp"
						style="float: left;padding-top: 1rem;" onchange="fileUpload()">
					</td>
				</tr>
				<tr>
					<th>文章内容</th>
					<td colspan="3"><textarea cols="120" id="editor1"
							name="editor1" rows="30">${journal.context }</textarea> <textarea
							style="display: none" id="context" name="context">${journal.context }</textarea>
					</td>
				</tr>
				<tr>
					<th>访问权限</th>
					<td><select name="openLevel">
							<option value="1"
								<c:if test="${journal.openLevel==1 }">selected</c:if>>对所有人公开</option>
							<option value="2"
								<c:if test="${journal.openLevel==2 }">selected</c:if>>对所有人保密</option>
							<option value="3"
								<c:if test="${journal.openLevel==3 }">selected</c:if>>添加阅读密码</option>
					</select></td>
					<th>访问密码</th>
					<td><input name="openPwd" value="${journal.openPwd }"
						type="text" placeholder="阅读密码" /> <c:if
							test="${journal.openLevel!=3 }">
							<small>密码未启用</small>
						</c:if></td>
				</tr>
				<tr>
					<td colspan="4">
						<div class="am-form-group" style="float:right">
							<div class="am-u-sm-9 am-u-sm-push-3">
								<button type="submit" class="am-btn am-btn-primary">保存</button>
							</div>
						</div>
					</td>
				</tr>
			</table>
	</form>
</body>
<jsp:include page="base/js.jsp" />
<script src="${basePath }/assets/js/ajaxfileupload.js"></script>
<%@ taglib uri="http://ckeditor.com" prefix="ckeditor"%>
<script>
	var uploadUrl = '${basePath}admin/ckeditorUpload.${defSuffix}';
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
		var oEditor = CKEDITOR.instances.editor1;
		var content = oEditor.getData();
		$("#context").val(content);
		$.ajax({
			type : "POST",
			dataType : 'json',
			data : $("#dataform").serialize(),
			url : 'journalSave.${defSuffix}',
			timeout : 60000,
			success : function(json) {
				alert(json.msg);
				if (json.code == 0) {
					location.href = 'journalList.${defSuffix}';
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

<ckeditor:replace replace="editor1" basePath="${basePath }ckeditor/" />
</html>
