<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<div class="tj_news">
	<h2>
		<p class="tj_t0">文章分类</p>
	</h2>
	<p style="height: 10px;"></p>
	<div class="new_type">
		<script>
			function isHidden(classname) {
				$("." + classname + "").slideToggle("slow", function() {
				});
			}
		</script>

		<c:forEach items="${types }" var="type" varStatus="index">
		<div>
		<span style="color:#b0e516;">【</span>
			<a target="_parent"
				<c:if test="${!empty type.childTypes }">
			href="javascript:isHidden('articleType${index.index}')"
			</c:if>
				<c:if test="${empty type.childTypes }">
			href="${basePath }journal_1_${type.id }.${defSuffix}"
			</c:if>
				style="cursor:hand;font-size: 16px;color:white;font-family:黑体;"><B>${type.className }(<span
					style="color: red;">${type.count==null?0:type.count }</span>)
			</B></a>
			<span style="color:#b0e516;">】</span>
		</div>
			<c:forEach items="${type.childTypes }" var="childType">
				<ul class="articleType${index.index}" style="display: none;">
					<li id="hotjournal" style="line-height:16px;height:16px"><a
						href="${basePath }journal_1_${childType.id }.${defSuffix}">${childType.className }
							(<span style="color: red;">${childType.count }</span>)
					</a></li>
				</ul>
			</c:forEach>
		</c:forEach>


	</div>
	<br />
	<h2>
		<p class="tj_t1">最新文章</p>
	</h2>
	<ul id="newjournal">
		<c:forEach items="${newJournal }" var="journal">
			<li><a class="hiddenA" href="article_${journal.id }.${defSuffix}">${journal.title}</a></li>
		</c:forEach>

	</ul>
	<h2>
		<p class="tj_t2">推荐文章</p>
	</h2>
	<ul id="hotjournal">
		<c:forEach items="${hotJournal }" var="journal">
			<li><a class="hiddenA" href="article_${journal.id }.${defSuffix}">${journal.title}</a></li>
		</c:forEach>

	</ul>
</div>
<style>
.hiddenA{
width:95%;display:block;white-space:nowrap; overflow:hidden; text-overflow:ellipsis;float:left;}
</style>