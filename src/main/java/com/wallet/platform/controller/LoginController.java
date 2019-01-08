package com.wallet.platform.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wallet.platform.consts.Const;
import com.wallet.platform.po.Manager;
import com.wallet.platform.po.Menu;
import com.wallet.platform.service.IManagerService;
import com.wallet.platform.service.IMenuService;
import com.wallet.platform.util.MD5;
import com.wallet.platform.util.Utils;

@Controller
public class LoginController extends BaseController {
	
	@Resource
	private IManagerService managerService;
	
	@Resource
	private IMenuService menuService;
	
	@RequestMapping(value = "/manager/login", method = RequestMethod.GET)
	public String login() {
		return managerPath("login");
	}
	
	@RequestMapping(value = "/manager/login", method = RequestMethod.POST)
	public String doLogin(HttpServletRequest request, Model model, String username, String password) {
		
		if (Utils.isEmpty(username) || Utils.isEmpty(password)) {
			add(model, "message", "错误：用户名或密码不能为空！");
			return managerPath("login");
		}
		
		Manager manager = managerService.getById(username);
		if (null == manager) {
			add(model, "message", "错误：用户名或密码错误！");
			return managerPath("login");
		}
		
		password = MD5.getMD5(password, true);
		if (!password.equals(manager.getManagerPwd())) {
			add(model, "message", "错误：用户名或密码错误！");
			manager.setErrorTimes(null == manager.getErrorTimes() ? 1 : manager.getErrorTimes() + 1);
			managerService.updateById(manager);
			return managerPath("login");
		}
		
		manager.setLastLoginTime(new Date());
		manager.setErrorTimes(0);
		managerService.updateById(manager);
		
		session(request, Const.SESSION_LOGIN_MANAGER, manager);
		
		return redirect("/manager/home");
	}
	
	@RequestMapping(value = "/manager/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request) {
		Manager manager = loginManager(request);
		if (null != manager) {
			Manager updateManager = new Manager();
			updateManager.setManagerName(manager.getManagerName());
			updateManager.setLastLogoutTime(new Date());
			managerService.updateById(updateManager);
		}
		removeSession(request, Const.SESSION_LOGIN_MANAGER);
		return redirect("/manager/login");
	}
	
	@RequestMapping(value = "/manager/home", method = RequestMethod.GET)
	public String home() {
		return managerPath("home");
	}
	
	@ResponseBody
	@RequestMapping(value = "/manager/menus", method = RequestMethod.GET, produces = Const.PRODUCES)
	public String menus(HttpServletRequest request) {
		List<Menu> menus = menuService.queryByIds(loginManager(request).getMenus());
		Map<String, Menu> map = new HashMap<String, Menu>();
		List<Menu> result = new ArrayList<Menu>();
		if (null != menus && menus.size() > 0) {
			for (Menu menu : menus) {
				if (Utils.isEmpty(menu.getParentId())) {
					map.put(menu.getId(), menu);
					result.add(menu);
				}
			}
			Menu parentMenu = null;
			for (Menu menu : menus) {
				parentMenu = null;
				if (!Utils.isEmpty(menu.getParentId())) {
					parentMenu = map.get(menu.getParentId());
				}
				if (null != parentMenu) {
					parentMenu.addSubMenu(menu);
				}
			}
		}
		return toJson(true, "", result);
	}
}
