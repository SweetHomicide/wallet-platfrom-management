package com.wallet.platform.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;

import com.alibaba.fastjson.JSON;
import com.wallet.platform.bean.ResultBean;
import com.wallet.platform.consts.Const;
import com.wallet.platform.po.Manager;

public class BaseController {

	protected String managerPath(String path) {
		return new StringBuffer("manager/").append(path).toString();
	}

	protected String redirect(String path) {
		return new StringBuffer("redirect:").append(path).toString();
	}

	protected void add(Model model, String attr, Object val) {
		model.addAttribute(attr, val);
	}

	protected void session(HttpServletRequest request, String attr, Object val) {
		request.getSession(true).setAttribute(attr, val);
	}
	
	protected Object session(HttpServletRequest request, String attr) {
		HttpSession session = request.getSession(false);
		return (null == session) ? null : session.getAttribute(attr);
	}
	
	protected void removeSession(HttpServletRequest request, String attr) {
		request.getSession(true).removeAttribute(attr);
	}
	
	protected Manager loginManager(HttpServletRequest request) {
		return (Manager) session(request, Const.SESSION_LOGIN_MANAGER);
	}
	
	protected String toJson(boolean success, String message, Object data) {
		ResultBean bean = new ResultBean();
		bean.setSuccess(success);
		bean.setMessage(message);
		bean.setData(data);
		return JSON.toJSONString(bean);
	}
}
