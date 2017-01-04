<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<link href="assets/css/psearch20131008.css" rel="stylesheet">

<div class="search">
	<form id="formSearch" onSubmit="return false;">
		<input
			class="sbutton" onclick="toPages(1)" type="button"> <input
			class="skey" id="journalTitle"
			onkeydown="if(event.keyCode==13) toPages(1);"
			name="sosJournal.journalTitle" value="${keyword}"
			onfocus="if(value=='请输入搜索词'){value=''}"
			onblur="if(value==''){value='请输入搜索词'}"  placeholder="请输入搜索词" />
	</form>
</div>
<div class="clear">&nbsp;</div>