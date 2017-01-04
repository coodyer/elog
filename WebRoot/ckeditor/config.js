/**
 * @license Copyright (c) 2003-2015, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
	// 换行方式
	 config.enterMode = CKEDITOR.ENTER_BR;
	 // 当输入：shift+Enter是插入的标签
	 config.shiftEnterMode = CKEDITOR.ENTER_BR;// 
	 //图片处理
	 config.pasteFromWordRemoveStyles = true;
	 config.filebrowserImageUploadUrl = uploadUrl;
	 config.language = 'zh-cn'; // 配置语言
	 config.width = '100%'; // 宽度
	 config.height = '400px'; // 高度
	 config.skin = 'kama';// 皮肤
	 // 去掉ckeditor“保存”按钮
	 config.removePlugins = 'save';
	config.toolbarGroups = [
		{ name: 'clipboard',   groups: [ 'clipboard', 'undo' ] },
		{ name: 'editing',     groups: [ 'find', 'selection', 'spellchecker' ] },
		{ name: 'links' },
		{ name: 'insert' },
		{ name: 'forms' },
		{ name: 'tools' },
		{ name: 'document',	   groups: [ 'mode', 'document', 'doctools' ] },
		{ name: 'others' },
		'/',
		{ name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ] },
		{ name: 'paragraph',   groups: [ 'list', 'indent', 'blocks', 'align', 'bidi' ] },
		{ name: 'styles' },
		{ name: 'colors' },
	];

	// Remove some buttons provided by the standard plugins, which are
	// not needed in the Standard(s) toolbar.
	config.removeButtons = 'Underline,Subscript,Superscript';

	// Set the most common block elements.
	config.format_tags = 'p;h1;h2;h3;pre';

	// Simplify the dialog windows.
	config.removeDialogTabs = 'image:advanced;link:advanced';
};
