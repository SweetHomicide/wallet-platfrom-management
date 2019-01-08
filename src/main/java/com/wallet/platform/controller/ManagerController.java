package com.wallet.platform.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wallet.platform.consts.Const;
import com.wallet.platform.po.Manager;
import com.wallet.platform.service.IManagerService;
import com.wallet.platform.util.MD5;
import com.wallet.platform.util.Utils;

@Controller
@RequestMapping(value = "/manager/manager", produces = Const.PRODUCES)
public class ManagerController extends BaseController {
	
	@Resource
	private IManagerService managerService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String manager() {
		return managerPath("manager/manager");
	}
	
	@RequestMapping(value="/load", method = RequestMethod.GET)
	public String load(Model model, String id, String type) {
		add(model, "type", type);
		if ("EDIT".equals(type) || "DEL".equals(type)) {
			add(model, "mg", managerService.getById(id));
		}
		return managerPath("manager/edit");
	}
	
	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(String type, Manager manager) {
		if ("EDIT".equals(type)) {
			if (Utils.isEmpty(manager.getManagerName())) {
				return toJson(false, "empty manager id", null);
			}
			if (null == manager.getInuse()) {
				manager.setInuse(false);
			}
			if (!Utils.isEmpty(manager.getManagerPwd())) {
				manager.setManagerPwd(MD5.getMD5(manager.getManagerPwd(), true));
			} else {
				manager.setManagerPwd(null);
			}
			managerService.updateById(manager);
			return toJson(true, "修改成功", null);
		} else if ("DEL".equals(type)) {
			if (Utils.isEmpty(manager.getManagerName())) {
				return toJson(false, "empty manager id", null);
			}
			managerService.deleteById(manager.getManagerName());
			return toJson(true, "删除成功", null);
		} else if ("ADD".equals(type)) {
			if (Utils.isEmpty(manager.getManagerName()) || Utils.isEmpty(manager.getManagerPwd())) {
				return toJson(false, "用户名和密码不能为空", null);
			}
			manager.setManagerPwd(MD5.getMD5(manager.getManagerPwd(), true));
			managerService.save(manager);
			return toJson(true, "新增成功", null);
		} else {
			return toJson(false, "unknown type <== " + type, null);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/datas", method = RequestMethod.GET)
	public String datas(HttpServletRequest request, Integer limit, Integer offset) {
		List<Manager> managers = managerService.queryAll((null == offset ? 0 : offset), (null == limit ? 10 : limit));
		Integer total = managerService.getCount();
		JSONObject json = new JSONObject();
		json.put("total", (null == total ? 0 : total));
		json.put("rows", managers);
		return JSON.toJSONString(json);
	}
	
}
