package com.blog.web.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.blog.web.base.page.Pager;
import com.blog.web.cache.AdminCache;
import com.blog.web.cache.AuthorCache;
import com.blog.web.cache.JournalCache;
import com.blog.web.cache.LinkCache;
import com.blog.web.cache.MenuCache;
import com.blog.web.cache.NavCache;
import com.blog.web.cache.RoleCache;
import com.blog.web.cache.SettingCache;
import com.blog.web.cache.SuffixCache;
import com.blog.web.cache.ToolsCache;
import com.blog.web.cache.TypeCache;
import com.blog.web.controllers.base.BaseController;
import com.blog.web.entity.MsgEntity;
import com.blog.web.model.Admin;
import com.blog.web.model.Author;
import com.blog.web.model.Journal;
import com.blog.web.model.Links;
import com.blog.web.model.Menus;
import com.blog.web.model.Nav;
import com.blog.web.model.Role;
import com.blog.web.model.Setting;
import com.blog.web.model.Suffix;
import com.blog.web.model.Tools;
import com.blog.web.model.Types;
import com.blog.web.util.EncryptionUtil;
import com.blog.web.util.StringUtils;
import com.blog.web.util.UploadUtil;

@Controller
public class AdminController extends BaseController {
	
	static final String DIR = "back/";
	@Resource
	SettingCache settingCache;
	@Resource
	AdminCache adminCache;
	@Resource
	SuffixCache suffixCache;
	@Resource
	RoleCache roleCache;
	@Resource
	MenuCache menuCache;
	@Resource
	TypeCache typeCache;
	@Resource
	NavCache navCache;
	@Resource
	JournalCache journalCache;
	@Resource
	AuthorCache authorCache;
	@Resource
	LinkCache linkCache;
	@Resource
	ToolsCache toolsCache;

	@RequestMapping  
	public String login(HttpServletResponse res) {
		return DIR + "login";
	}

	@RequestMapping
	public void loginOut(HttpServletResponse res) {
		delSessionAdmin();
		try {
			res.sendRedirect(getAttribute("basePath") + "admin/login."
					+ getAttribute("defSuffix"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping
	public String modifyAdmin(HttpServletResponse res) {
		return DIR + "modify_admin";
	}


	@RequestMapping
	public String sysUserAdmin(HttpServletResponse res) {
		List<Admin> admins = adminCache.loadAdmins();
		setAttribute("admins", admins);
		return DIR + "admin_list";
	}

	@RequestMapping
	public String adminEdit(HttpServletResponse res) {
		Integer id = getParaInteger("id");
		if (!StringUtils.isNullOrEmpty(id)) {
			Admin admin = adminCache.getAdmin(id);
			setAttribute("admin", admin);
		}
		List<Role> roles = roleCache.loadRoles();
		setAttribute("roles", roles);
		return DIR + "admin_edit";
	}

	@RequestMapping
	public void adminDel(HttpServletResponse res) {
		Integer id = getParaInteger("id");
		if (StringUtils.isNullOrEmpty(id)) {
			printMsg(res, new MsgEntity(1, "参数有误"));
			return;
		}
		adminCache.delete(id);
		printMsg(res, new MsgEntity(0, "操作成功"));
	}

	@RequestMapping
	public void adminSave(HttpServletResponse res) {
		Admin admin = (Admin) getBeanAll(Admin.class);
		if (StringUtils.findEmptyIndex(admin.getUserName(), admin.getUserPwd(),
				admin.getRole().getId()) > -1) {
			printMsg(res, new MsgEntity(1, "参数有误"));
			return;
		}
		admin.setUserPwd(EncryptionUtil.customEnCode(admin.getUserPwd()));
		adminCache.save(admin);
		printMsg(res, new MsgEntity(0, "操作成功"));
	}

	@RequestMapping
	public String menuList(HttpServletResponse res) {
		List<Menus> menuList = menuCache.loadBaseMenus();
		setAttribute("menuList", menuList);
		return DIR + "menu_list";
	}

	@RequestMapping
	public String menuEdit(HttpServletResponse res) {
		Integer id = getParaInteger("id");
		if (!StringUtils.isNullOrEmpty(id)) {
			Menus menu = menuCache.getMenu(id);
			setAttribute("menu", menu);
		}
		List<Menus> menuList = menuCache.loadBaseMenus();
		setAttribute("menuList", menuList);
		return DIR + "menu_edit";
	}

	@RequestMapping
	public void menuSave(HttpServletResponse res) {
		Menus menu = (Menus) getBeanAccept(Menus.class, "id", "menus.id",
				"title", "url", "type", "seq");
		if (StringUtils.findEmptyIndex(menu.getTitle(), menu.getType(),
				menu.getUrl(), menu.getSeq()) > -1) {
			printMsg(res, new MsgEntity(1, "参数有误"));
			return;
		}
		menuCache.save(menu);
		printMsg(res, new MsgEntity(0, "操作成功"));
	}

	@RequestMapping
	public void menuDel(HttpServletResponse res) {
		Integer id = getParaInteger("id");
		if (StringUtils.isNullOrEmpty(id)) {
			printMsg(res, new MsgEntity(1, "参数有误"));
			return;
		}
		menuCache.delete(id);
		printMsg(res, new MsgEntity(0, "操作成功"));
	}

	@RequestMapping
	public String inviteImport(HttpServletResponse res) {
		return DIR + "invite_import";
	}

	@RequestMapping
	public void saveCurrAdmin(HttpServletResponse res) {
		Admin currAdmin = getSessionAdmin();
		String newUsername = getPara("username");
		String password = getPara("password");
		String newPwd = getPara("newPwd");
		if (StringUtils.findEmptyIndex(newUsername, password, newPwd) > -1) {
			printMsg(res, new MsgEntity(1, "用户名或密码为空"));
			return;
		}
		password = EncryptionUtil.customEnCode(password);
		System.out.println(password);
		System.out.println(currAdmin.getUserPwd());
		if (!password.equals(currAdmin.getUserPwd())) {
			printMsg(res, new MsgEntity(4, "旧密码有误"));
			return;
		}
		Admin admin = adminCache.getAdmin(newUsername);
		if (!StringUtils.isNullOrEmpty(admin)
				&& admin.getId().intValue() != currAdmin.getId().intValue()) {
			printMsg(res, new MsgEntity(6, "该用户名已被使用"));
			return;
		}
		System.out.println(newPwd);
		newPwd = EncryptionUtil.customEnCode(newPwd);
		currAdmin.setUserName(newUsername);
		currAdmin.setUserPwd(newPwd);
		adminCache.save(currAdmin);
		setSessionAdmin(admin);
		printMsg(res, new MsgEntity(0, "操作成功"));
		return;
	}

	@RequestMapping
	public void doLogin(HttpServletResponse res) {
		String username = getPara("username");
		String password = getPara("password");
		System.out.println(EncryptionUtil.customEnCode(password));
		String verCode = getPara("verCode");
		if (StringUtils.isNullOrEmpty(verCode)) {
			printMsg(res, new MsgEntity(3, "验证码为空"));
			return;
		}
		if (StringUtils.findEmptyIndex(username, password) > -1) {
			printMsg(res, new MsgEntity(1, "用户名或密码为空"));
			return;
		}
		String sessionCode = (String) getSessionPara("piccode");
		setSessionPara("piccode", null);
		if (sessionCode == null || !sessionCode.equals(verCode)) {
			printMsg(res, new MsgEntity(4, "验证码有误"));
			return;
		}
		Admin admin = adminCache.getAdmin(username);
		if (StringUtils.isNullOrEmpty(admin)) {
			printMsg(res, new MsgEntity(2, "该用户不存在"));
			return;
		}
		password = EncryptionUtil.customEnCode(password);
		if (!password.equals(admin.getUserPwd())) {
			printMsg(res, new MsgEntity(3, "密码有误"));
			return;
		}
		setSessionAdmin(admin);
		setSessionPara("loginTime", new Date());
		printMsg(res, new MsgEntity(0, "登录成功"));
		return;
	}

	@RequestMapping
	public String roleList(HttpServletRequest req) {
		List<Role> roleList = roleCache.loadRoles();
		setAttribute("roleList", roleList);
		return DIR + "role_list";
	}
	@RequestMapping
	public void roleDel(HttpServletResponse res) {
		Integer id = getParaInteger("id");
		if (!StringUtils.isNullOrEmpty(id)) {
		roleCache.del(id);
		}
		printMsg(res, new MsgEntity(0, "操作成功"));
	}

	@RequestMapping
	public String roleEdit(HttpServletResponse res) {
		Integer id = getParaInteger("id");
		if (!StringUtils.isNullOrEmpty(id)) {
			Role role =roleCache.loadRole(id);
			setAttribute("role", role);
		}
		return DIR + "role_edit";
	}

	@RequestMapping
	public void roleSave(HttpServletResponse res) {
		Integer id=getParaInteger("id");
		Role role=new Role();
		if(!StringUtils.isNullOrEmpty(id)){
			role =roleCache.loadRole(id);
		}
		role = (Role) getBeanAll(role);
		roleCache.save(role);
		printMsg(res, new MsgEntity(0, "操作成功"));
	}

	@RequestMapping
	public String disPermis(HttpServletRequest req) {
		Integer id = getParaInteger("id");
		Role role = roleCache.loadRole(id);
		if (StringUtils.isNullOrEmpty(role)) {
			return "404";
		}
		setAttribute("role", role);
		List<Menus> menuList = menuCache.loadBaseMenus();
		setAttribute("menuList", menuList);
		if (!StringUtils.isNullOrEmpty(role.getMenus())) {
			Integer[] ids = StringUtils.splitByMosaicIntegers(role.getMenus(),
					",");
			Map<Integer, Object> roleMap = new HashMap<Integer, Object>();
			for (Integer tmp : ids) {
				roleMap.put(tmp, tmp);
			}
			setAttribute("roleMap", roleMap);
		}
		return DIR + "dis_permis";
	}

	@RequestMapping
	public void savePermis(HttpServletResponse res) {
		Integer id = getParaInteger("roleId");
		Role role = roleCache.loadRole(id);
		if (StringUtils.isNullOrEmpty(role)) {
			printMsg(res, new MsgEntity(-1, "参数有误"));
			return;
		}
		try {
			Integer[] ids = getParaIntegers("ids[]");
			String menuIds = StringUtils.collectionMosaic(ids, ",");
			role.setMenus(menuIds);
			roleCache.save(role);
			printMsg(res, new MsgEntity(0, "操作成功"));
			return;
		} catch (Exception e) {
			printMsg(res, new MsgEntity(1, "系统出错"));
			return;
		}
	}

	@RequestMapping
	public String adminSuffix(HttpServletRequest req) {
		List<Suffix> suffixList = suffixCache.loadSuffix();
		setAttribute("suffixList", suffixList);
		return DIR + "suffix";
	}

	@RequestMapping
	public void updateSuffix(HttpServletResponse res) throws IOException {
		String[] suffix = getParas("suffix[]");
		if (suffix == null || suffix.length < 1) {
			printMsg(res, new MsgEntity(1, "至少保留一个后缀"));
			return;
		}
		try {
			Integer[] intSuffix = new Integer[suffix.length];
			for (int i = 0; i < suffix.length; i++) {
				intSuffix[i] = Integer.valueOf(suffix[i]);
			}
			suffixCache.updateSuffix(intSuffix);
			printMsg(res, new MsgEntity(0, "操作成功"));
			return;
		} catch (Exception e) {
			e.printStackTrace();
			printMsg(res, new MsgEntity(4, "系统出错"));
			return;
		}
	}

	@RequestMapping
	public void defaultSuffix(HttpServletResponse res) throws Exception {
		try {
			Integer sid = getParaInteger("id");
			Suffix suffix = suffixCache.getSuffix(sid);
			if (suffix == null) {
				printMsg(res, new MsgEntity(3, "后缀不存在"));
				return;
			}
			suffix.setStatus(2);
			suffixCache.updateSuffix(suffix);
			printMsg(res, new MsgEntity(0, "设置成功"));
			return;
		} catch (Exception e) {
			printMsg(res, new MsgEntity(4, "系统出错"));
			return;
		}
	}

	@RequestMapping
	public String setting(HttpServletResponse res) {
		Setting setting = settingCache.loadSetting();
		setAttribute("setting", setting);
		return DIR + "setting";
	}

	@RequestMapping
	public String typeList(HttpServletResponse res) {
		loadTypes(getParaInteger("parentId"));
		keepParas();
		return DIR + "type_list";
	}

	@RequestMapping
	public String typeEdit(HttpServletResponse res) {
		Integer id = getParaInteger("id");
		if (!StringUtils.isNullOrEmpty(id)) {
			Types type = typeCache.getType(id);
			setAttribute("type", type);
		}
		loadTypes();
		keepParas();
		return DIR + "type_edit";
	}

	@RequestMapping
	public void typeSave(HttpServletResponse res) {
		try {
			Types type = (Types) getBeanAll(Types.class);
			typeCache.saveType(type);
			printMsg(res, new MsgEntity(0, "操作成功"));
		} catch (Exception e) {
			e.printStackTrace();
			printMsg(res, new MsgEntity(1, "系统出错"));
		}
	}

	@RequestMapping
	public void typeDel(HttpServletResponse res) {
		try {
			Integer id = getParaInteger("id");
			typeCache.delType(id);
			printMsg(res, new MsgEntity(0, "操作成功"));
		} catch (Exception e) {
			e.printStackTrace();
			printMsg(res, new MsgEntity(1, "系统出错"));
		}
	}

	@RequestMapping
	public void saveSetting(HttpServletResponse res) {
		try {
			Setting setting = (Setting) getBeanAll("setting", Setting.class);
			settingCache.saveSetting(setting);
			printMsg(res, new MsgEntity(0, "保存成功"));
			return;
		} catch (Exception e) {
			e.printStackTrace();
			printMsg(res, new MsgEntity(-1, "系统出错"));
			return;
		}

	}

	@RequestMapping
	public String navList(HttpServletResponse res) {
		List<Nav> navs = navCache.loadNavs();
		setAttribute("navs", navs);
		keepParas();
		return DIR + "nav_list";
	}

	@RequestMapping
	public String navEdit(HttpServletResponse res) {
		Integer id = getParaInteger("id");
		if (!StringUtils.isNullOrEmpty(id)) {
			Nav nav = navCache.getNav(id);
			setAttribute("nav", nav);
		}
		keepParas();
		return DIR + "nav_edit";
	}

	@RequestMapping
	public void navSave(HttpServletResponse res) {
		try {
			Nav nav = (Nav) getBeanAll(Nav.class);
			navCache.saveNav(nav);
			keepParas();
			printMsg(res, new MsgEntity(0, "操作成功"));
		} catch (Exception e) {
			e.printStackTrace();
			printMsg(res, new MsgEntity(1, "系统出错"));
		}
	}

	@RequestMapping
	public void navDel(HttpServletResponse res) {
		try {
			Integer id = getParaInteger("id");
			if (!StringUtils.isNullOrEmpty(id)) {
				navCache.delNav(id);
			}
			keepParas();
			printMsg(res, new MsgEntity(0, "操作成功"));
		} catch (Exception e) {
			e.printStackTrace();
			printMsg(res, new MsgEntity(1, "系统出错"));
		}

	}

	@RequestMapping
	public String journalList(HttpServletResponse res) {
		Pager<Journal> pager = (Pager<Journal>) getBeanAll(Pager.class);
		Journal journal = (Journal) getBeanAll(Journal.class);
		pager = journalCache.loadJournal(journal, pager);
		setAttribute("pager", pager);
		keepParas();
		return DIR + "journal_list";
	}
	@RequestMapping
	public String toolsList(HttpServletResponse res) {
		Pager<Tools> pager = (Pager<Tools>) getBeanAll(Pager.class);
		Tools tool = (Tools) getBeanAll(Tools.class);
		pager = toolsCache.loadUtilPager(tool, pager);
		setAttribute("pager", pager);
		keepParas();
		return DIR + "tools_list";
	}
	@RequestMapping
	public String toolsEdit(HttpServletResponse res) {
		Integer id = getParaInteger("id");
		if (!StringUtils.isNullOrEmpty(id)) {
			Tools tools = toolsCache.getTools(id);
			setAttribute("tools", tools);
		}
		keepParas();
		return DIR + "tools_edit";
	}
	@RequestMapping
	public void toolsDel(HttpServletResponse res) {
		try {
			Integer id = getParaInteger("id");
			if (!StringUtils.isNullOrEmpty(id)) {
				toolsCache.del(id);
			}
			printMsg(res, new MsgEntity(0, "操作成功"));
		} catch (Exception e) {
			e.printStackTrace();
			printMsg(res, new MsgEntity(1, "系统出错"));
		}
	}
	@RequestMapping
	public void toolsSave(HttpServletResponse res) {
		try {
			Tools tool = (Tools) getBeanAll(Tools.class);
			toolsCache.save(tool);
			printMsg(res, new MsgEntity(0, "操作成功"));
		} catch (Exception e) {
			e.printStackTrace();
			printMsg(res, new MsgEntity(1, "系统出错"));
		}
	}
	@RequestMapping
	public String journalEdit(HttpServletResponse res) {
		Integer id = getParaInteger("id");
		if (!StringUtils.isNullOrEmpty(id)) {
			Journal journal = journalCache.getJournal(id);
			setAttribute("journal", journal);
		}
		loadTypes();
		keepParas();
		return DIR + "journal_edit";
	}
	@RequestMapping
	public void journalSave(HttpServletResponse res) {
		try {
			Integer id = getParaInteger("id");
			Journal journal=journalCache.getJournal(id);
			if (StringUtils.isNullOrEmpty(journal)) {
				journal=new Journal();
				journal.setAuthor(getSessionAdmin().getUserName());
				journal.setTime(new Date());
			}
			journal=(Journal) getBeanAll(journal);
			Types type=typeCache.getType(getParaInteger("types_id"));
			journal.setTypeId(getParaInteger("types_id"));
			journal.setTypes(type);
			journalCache.saveJournal(journal);
			printMsg(res, new MsgEntity(0, "操作成功"));
		} catch (Exception e) {
			e.printStackTrace();
			printMsg(res, new MsgEntity(1, "系统出错"));
		}
	}
	@RequestMapping
	public void journalDel(HttpServletResponse res) {
		try {
			Integer id = getParaInteger("id");
			if (!StringUtils.isNullOrEmpty(id)) {
				journalCache.delJournal(id);
			}
			keepParas();
			printMsg(res, new MsgEntity(0, "操作成功"));
		} catch (Exception e) {
			e.printStackTrace();
			printMsg(res, new MsgEntity(1, "系统出错"));
		}

	}
	@RequestMapping
	public String authorSetting(HttpServletResponse res) {
		Author author=authorCache.loadAuthor();
		setAttribute("author", author);
		keepParas();
		return DIR + "author_setting";
	}
	@RequestMapping
	public void authorSave(HttpServletResponse res) {
		try {
		Author author=(Author) getBeanAll(Author.class);
		authorCache.saveAuthor(author);
		keepParas();
		printMsg(res, new MsgEntity(0, "操作成功"));
		} catch (Exception e) {
			e.printStackTrace();
			printMsg(res, new MsgEntity(1, "系统出错"));
		}
	}
	
	@RequestMapping
	public String linkList(HttpServletResponse res) {
		List<Links> links=linkCache.loadLinks();
		setAttribute("links", links);
		keepParas();
		return DIR + "link_list";
	}
	
	@RequestMapping
	public String linkEdit(HttpServletResponse res) {
		Integer id = getParaInteger("id");
		if (!StringUtils.isNullOrEmpty(id)) {
			Links link = linkCache.getLink(id);
			setAttribute("link", link);
		}
		loadTypes();
		keepParas();
		return DIR + "link_edit";
	}
	@RequestMapping
	public void linkSave(HttpServletResponse res) {
		try {
			Links link = (Links) getBeanAll(Links.class);
			linkCache.saveLinks(link);
			keepParas();
			printMsg(res, new MsgEntity(0, "操作成功"));
		} catch (Exception e) {
			e.printStackTrace();
			printMsg(res, new MsgEntity(1, "系统出错"));
		}
	}
	@RequestMapping
	public void linkDel(HttpServletResponse res) {
		try {
			Integer id=getParaInteger("id");
			linkCache.delLinks(id);
			keepParas();
			printMsg(res, new MsgEntity(0, "操作成功"));
		} catch (Exception e) {
			e.printStackTrace();
			printMsg(res, new MsgEntity(1, "系统出错"));
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private void loadTypes() {
		List<Types> types = typeCache.loadTypes();
		setAttribute("types", types);
	}

	private void loadTypes(Integer parentId) {
		List<Types> types = typeCache.loadTypes(parentId);
		setAttribute("types", types);
	}

	@RequestMapping
	public String emailList(HttpServletResponse res) {
		return DIR + "email_list";
	}

	@RequestMapping
	public String index(HttpServletResponse res) {
		return DIR + "index";
	}

	@RequestMapping
	public void upload(HttpServletResponse res) {
		try {
			String url = UploadUtil.doUpload(request);
			printMsg(res, new MsgEntity(0, "上传成功", url));
		} catch (Exception e) {
			e.printStackTrace();
			printMsg(res, new MsgEntity(1, "上传失败"));
		}
	}

	@RequestMapping
	public void ckeditorUpload(HttpServletResponse res) {
		String callback = getPara("CKEditorFuncNum");
		try {
			String url = UploadUtil.doUpload(request);
			PrintWriter out = res.getWriter();
			out.println("<script type=\"text/javascript\">");
			out.println("window.parent.CKEDITOR.tools.callFunction(" + callback
					+ ",'" + url + "','')");
			out.println("</script>");

		} catch (Exception e) {
			e.printStackTrace();
			try {
				PrintWriter out = res.getWriter();
				out.println("<script type=\"text/javascript\">");
				out.print("window.parent.CKEDITOR.tools.callFunction("
						+ callback + ",''," + "'上传失败!');");
				out.println("</script>");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	@RequestMapping
	public String base(HttpServletResponse res) {
		return DIR + "base";
	}

	public static void main(String[] args) {
		System.out.println(EncryptionUtil.customEnCode("xebzechook"));
	}
}
