package com.foo.library.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.HandlerInterceptor;

import com.foo.library.config.LoginInterceptor;
import com.foo.library.service.LibraryService;

@RunWith(SpringRunner.class)
@WebMvcTest(HomeController.class)
public class HomeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private LibraryService libraryService;

	@Test
	public void testHomePage() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(get("/"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name(equalTo("home")))
				.andExpect(
						model().attribute("welcomeMessage",
								containsString("Welcome to the library!")))
				.andReturn();
		assertNotNull(mvcResult);
		List<HandlerInterceptor> interceptors = Arrays.asList(mvcResult
				.getInterceptors());
		assertTrue(
				"Not expecting login interceptor in this path",
				interceptors.stream().noneMatch(
						i -> i.getClass().equals(LoginInterceptor.class)));
	}

}
