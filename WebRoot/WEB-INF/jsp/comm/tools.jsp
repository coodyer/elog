<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<%@ taglib prefix="fmt" uri="/WEB-INF/tld/fmt.tld"%>
<div class="bloglist" id="listJournal">
	<div class="clear">&nbsp;</div>
	<h2>
		<p>
			<span>工具</span>列表
		</p>
	</h2>
	<div id="bloglist1">
		<c:if test="${empty pager ||empty pager.pageData }">
			<div class="blogs">
					<h3 id="baseH3">
						<center>暂无数据</center>
					</h3>
					<script>
					var clientH = $(window).height();//窗口高度
					var pageH = $(document).height();//可见高度
					var heig=clientH;
					if(pageH>clientH){
						heig=pageH;
					}
					$("#baseH3").css("height",pageH+"px");
					</script>
					<div class="dateview">
					    <jsp:useBean id="now" class="java.util.Date" />
						<fmt:formatDate value="${now}" pattern="yyyy-MM-dd" />
					</div>
				</div>
		</c:if>
		<c:if test="${!empty pager &&!empty pager.pageData }">
			<c:forEach items="${pager.pageData }" var="tool" varStatus="index">
				<div class="blogs">
					<h3>
						<a href="${basePath }${tool.url}.${defSuffix}">${tool.title }
						</a>
					</h3>
					<figure>
						<img src="${tool.logo }"
							onerror="this.src='${basePath}/assets/images/default_journal_logo.jpg'" style="width: 172px;height: 114px">
					</figure>
					<ul>
						<p class="articleP">${tool.remark }</p>
						<a href="${basePath }${tool.url}.${defSuffix}" class="readmore" style="background:#457B6A">立即使用&gt;&gt;</a>
					</ul>
					<p class="autor" style="height:5px">
					</p>
					<div class="dateview">
						${tool.title }
					</div>
				</div>

			</c:forEach>
		</c:if>
	</div>
</div>
<c:if test="${!empty pager &&!empty pager.pageData }">
	<div class="blogs">
		<jsp:include page="page.jsp" />
	</div>
</c:if>

