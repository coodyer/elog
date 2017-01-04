<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/tld/c.tld"%>
<c:forEach items="${types }" var="big">
	<c:forEach items="${big.childTypes }" var="type">
		<option value="${type.id }"
			<c:if test="${journal.types.id==type.id }">selected</c:if>>${type.className }</option>
	</c:forEach>
</c:forEach>