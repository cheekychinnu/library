package com.foo.library.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.foo.library.model.User;

public class AdminInterceptor extends HandlerInterceptorAdapter{
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String loginUrl = request.getContextPath() + "/login";
		User user = (User) request.getSession().getAttribute("loggedInUser");
		if(user == null || !user.getId().equals("admin")) {
			FlashMap outputFlashMap = RequestContextUtils.getOutputFlashMap(request);
			outputFlashMap.put("loginError", "Please enter admin credentials to continue");
			
			User user2 = new User();
			user2.setId("admin");
			request.setAttribute("user", user2);
			
			RequestContextUtils.saveOutputFlashMap(loginUrl, request, response);
			response.sendRedirect(loginUrl);
			return false;
		}
		return true;
	}
}
