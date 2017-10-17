package com.foo.library.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.foo.library.model.Rent;
import com.foo.library.model.User;
import com.foo.library.service.LibraryService;

@Controller
@RequestMapping("/user")
public class UserProfileController {

	@Autowired
	private LibraryService libraryService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showUserProfilePage(HttpSession session) {
		User loggedInUser = (User) session.getAttribute("loggedInUser");
		String userId = loggedInUser.getId();
		
		List<Rent> openRents = libraryService.getOpenRents(userId);
		
		ModelAndView modelAndView = new ModelAndView("profile");
		modelAndView.addObject("user", loggedInUser);
		modelAndView.addObject("openRents", openRents);
		
		return modelAndView;
	}

}
