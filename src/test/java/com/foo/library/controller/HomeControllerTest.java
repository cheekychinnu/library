package com.foo.library.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import org.junit.Test;

import com.foo.library.BaseMvcTest;

public class HomeControllerTest extends BaseMvcTest{

	@Test
	public void testHomePage() throws Exception {
		mockMvc.perform(get("/"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(
						view().name(equalTo("home")))
				.andExpect(model().attribute("welcomeMessage", containsString("Welcome to the library!")));
	}

}
