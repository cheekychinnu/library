package com.foo.library.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

public class LoginInterceptor extends HandlerInterceptorAdapter{
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String loginUrl = request.getContextPath() + "/login";
		if(request.getSession().getAttribute("loggedInUser") == null) {
			FlashMap outputFlashMap = RequestContextUtils.getOutputFlashMap(request);
			outputFlashMap.put("loginError", "Please login to continue");
			//New utility added in Spring 5
			RequestContextUtils.saveOutputFlashMap(loginUrl, request, response);
			response.sendRedirect(loginUrl);
			return false;
		}
		return true;
	}
}
