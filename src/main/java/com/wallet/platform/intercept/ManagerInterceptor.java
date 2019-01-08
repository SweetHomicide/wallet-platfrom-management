package com.wallet.platform.intercept;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.wallet.platform.config.Config;
import com.wallet.platform.consts.Const;
import com.wallet.platform.po.Manager;

@Component
public class ManagerInterceptor extends HandlerInterceptorAdapter {
	
	private static final Logger LOGGER = Logger.getLogger(ManagerInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String uri = request.getRequestURI();
		String contextPath = request.getContextPath();
		uri = uri.replaceFirst(contextPath, "").replace("//", "/");
		if (uri.indexOf("/") > 0) {
			uri = "/" + uri;
		}
		
		LOGGER.debug("request uri <== " + uri);
		
		if (uri.equals("/manager/login")) {
			return true;
		}
		
		if (uri.equals("/manager/logout")) {
			return true;
		}
		
		Object manager = request.getSession(true).getAttribute(Const.SESSION_LOGIN_MANAGER);
		if (null != manager && (manager instanceof Manager)) {
			LOGGER.debug("login manager <== " + ((Manager)manager).getManagerName());
			request.setAttribute("manager", manager);
			return true;
		}
		
		LOGGER.debug("not login. redirect to login page.");
		response.sendRedirect(Config.get("basePath", "") + "manager/login");
		return false;
	}

}
