package com.foo.library.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.foo.library.controller.validator.UserValidator;
import com.foo.library.model.User;
import com.foo.library.service.UserService;

@Controller
@RequestMapping("/register")
public class RegisterController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserValidator userValidator;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showRegisterForm() {
		return new ModelAndView("register", "user", new User());
	}

	@RequestMapping(method = RequestMethod.POST)
	public String register(@ModelAttribute("user") @Valid User user,
			BindingResult result) {
		if (result.hasErrors()) {
			return "register";
		}
		userService.register(user);
		return "redirect:/login";
	}

	@InitBinder
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(userValidator);
	}
}
