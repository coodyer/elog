<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<div class="search">
	<form id="formSearch" onSubmit="return false;">
		<input id="pageNow" name="page" type="hidden" /> <input
			class="sbutton" onclick="toPage(1)" type="button"> <input
			class="skey" id="journalTitle"
			onkeydown="if(event.keyCode==13) toPage(1);"
			name="sosJournal.journalTitle" value="请输入搜索词"
			onfocus="if(value=='请输入搜索词'){value=''}"
			onblur="if(value==''){value='请输入搜索词'}" />
	</form>
</div>
