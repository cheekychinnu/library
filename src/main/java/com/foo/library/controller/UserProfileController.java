package com.foo.library.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.foo.library.model.User;

@Controller
@RequestMapping("/user")
public class UserProfileController {

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showUserProfilePage(HttpSession session) {
		User loggedInUser = (User) session.getAttribute("loggedInUser");
		ModelAndView modelAndView = new ModelAndView("profile");
		modelAndView.addObject("user", loggedInUser);
		return modelAndView;
	}

}
