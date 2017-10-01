package com.foo.library.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.foo.library.controller.validator.UserValidator;
import com.foo.library.model.User;
import com.foo.library.service.UserService;

@RunWith(SpringRunner.class)
@WebMvcTest(RegisterController.class)
public class RegisterControllerTest {
	
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
	public void testShowRegisterPage() throws Exception {
		mockMvc.perform(get("/register")).andDo(print())
		.andExpect(status().isOk())
		.andExpect(view().name(equalTo("register")))
		.andExpect(model().attributeExists("user"));
	}
	
	@Test
	public void testInvalidRegistration() throws Exception {

		String userId = "chinnu";
		String password = "dummy";
		String firstName = "Vinodhini";
		String lastName = "Chockalingam";
		String emailId = "dummy@gmail.com";
		
		User user = new User();
		user.setId(userId);
		user.setPassword(password);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmailId(emailId);
		
		String errorString = "dummy";
		Mockito.doThrow(new IllegalArgumentException(errorString)).when(userService).register(user);
		
		mockMvc.perform(post("/register")
				.param("id", userId).param("password", password)
				.param("firstName", firstName).param("lastName", lastName).param("emailId", emailId)).andDo(print())
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("user"))
				.andExpect(model().attribute("registrationError",containsString(errorString)))
				.andExpect(model().attribute("user",hasProperty("id", equalTo(userId))))
				.andExpect(model().attribute("user",hasProperty("password", equalTo(password))))
				.andExpect(model().attribute("user",hasProperty("firstName", equalTo(firstName))))
				.andExpect(model().attribute("user",hasProperty("lastName", equalTo(lastName))))
				.andExpect(model().attribute("user",hasProperty("emailId", equalTo(emailId))))
				.andExpect(view().name(equalTo("register")));
	}
	
	@Test
	public void testValidRegistration() throws Exception {

		String userId = "chinnu";
		String password = "dummy";
		String firstName = "Vinodhini";
		String lastName = "Chockalingam";
		String emailId = "dummy@gmail.com";
		
		User user = new User();
		user.setId(userId);
		user.setPassword(password);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmailId(emailId);
		
		mockMvc.perform(post("/register")
				.param("id", userId).param("password", password)
				.param("firstName", firstName).param("lastName", lastName).param("emailId", emailId)).andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/login"))
				.andExpect(model().attributeDoesNotExist("user"))
				.andExpect(view().name(equalTo("redirect:/login")));
	}

}
