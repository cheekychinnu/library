package com.foo.library.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.foo.library.model.User;

public class LoginInterceptor extends HandlerInterceptorAdapter{
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String loginUrl = request.getContextPath() + "/login";
		User user = (User) request.getSession().getAttribute("loggedInUser");
		if(user == null ) {
			FlashMap outputFlashMap = RequestContextUtils.getOutputFlashMap(request);
			outputFlashMap.put("loginError", "Please login to continue");
			//New utility added in Spring 5
			RequestContextUtils.saveOutputFlashMap(loginUrl, request, response);
			response.sendRedirect(loginUrl);
			return false;
		}
		if(user.getId().equals("admin")) {
			String adminUrl = request.getContextPath() + "/admin";
			FlashMap outputFlashMap = RequestContextUtils.getOutputFlashMap(request);
			outputFlashMap.put("accessError", "You can only access admin page");
			RequestContextUtils.saveOutputFlashMap(adminUrl, request, response);
			response.sendRedirect(adminUrl);
			return false;
		}
		return true;
	}
}
