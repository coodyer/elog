<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<link href="${basePath }assets/css/page.css" rel="stylesheet">
<div class="page">
	<div id="kkpager">
		<div>
			<span class="pageBtnWrap" style="display:block;white-space:nowrap; overflow:hidden; text-overflow:ellipsis;float:right"> <a target="_top"
				<c:if test="${empty currType }">href="journal_${pager.currentPage-1 }.${defSuffix}"</c:if>
				<c:if test="${!empty currType }">href="journal_${pager.currentPage-1 }_${currType }.${defSuffix}"</c:if>
				${(pager.currentPage<=1)?"class='disabled'":""}>上一页</a>
				<span class="curr">${pager.currentPage }/${pager.totalPages }页</span>
				<a target="_top"
				<c:if test="${empty currType }">href="journal_${pager.currentPage+1 }.${defSuffix}"</c:if>
				<c:if test="${!empty currType }">href="journal_${pager.currentPage+1 }_${currType }.${defSuffix}"</c:if>
				${(pager.currentPage>=pager.totalPages)?"class='disabled'":""}>下一页</a><input type="text" id="kkpager_btn_go_input"
				onkeydown="javascript:if(event.keyCode==13){toPage();}"
				value="${(pager.currentPage+2>=pager.totalPages)?pager.totalPages:pager.currentPage+2 }">
				<input type="button" value="跳转" onclick="toPage()"
				style="cursor: pointer;" /></span>
			<script>
				function toPage() {
					var page = $("#kkpager_btn_go_input").val();
					var type = '${(empty currType)?'':currType}';
					var allPage = ${pager.totalPages};
					if (page > 0 && page <= allPage) {
						var url = "journal_" + page
						if (type != '') {
							url = url + "_" + type;
						}
						url = url + ".${defSuffix}";
						window.location.href = url;
					} else {
						alert("页码有误");
					}
				}
			</script>
		</div>
		<div style="clear:both;"></div>
	</div>
</div>