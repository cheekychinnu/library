package com.foo.library.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.foo.library.controller.validator.UserValidator;
import com.foo.library.model.User;
import com.foo.library.service.UserService;

@RunWith(SpringRunner.class)
@WebMvcTest(LoginController.class)
public class LoginControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserValidator userValidator;

	@MockBean
	private UserService userService;

	@Before
	public void setup() {
		when(userValidator.supports(eq(User.class))).thenReturn(true);
	}

	@Test
	public void testShowLoginForm() throws Exception {
		mockMvc.perform(get("/login")).andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name(equalTo("login")));
	}

	@Test
	public void testLoginForValidCredentials() throws Exception {
		User user = new User();
		String userId = "chinnu";
		user.setId(userId);
		String password = "dummy";
		user.setPassword(password);
		
		when(userService.getUser(userId, password)).thenReturn(user);
		mockMvc.perform(post("/login")
				.param("id", userId).param("password", password)).andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/"))
				.andExpect(view().name(equalTo("redirect:/")));
	}
	
	@Test
	public void testLoginForInvalidCredentials() throws Exception {
		User user = new User();
		String userId = "chinnu";
		user.setId(userId);
		String password = "dummy";
		user.setPassword(password);
		
		when(userService.getUser(userId, password)).thenReturn(null);
		mockMvc.perform(post("/login")
				.param("id", userId).param("password", password)
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name(equalTo("login")))
				.andExpect(model().attribute("loginError",containsString("Error logging in. Please try again")))
				.andExpect(model().attribute("user",hasProperty("id", equalTo(userId))))
				.andExpect(model().attribute("user",hasProperty("password", equalTo(password))));
	}
}

