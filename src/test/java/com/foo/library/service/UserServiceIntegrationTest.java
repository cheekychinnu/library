package com.foo.library.service;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.foo.library.model.User;

public class UserServiceIntegrationTest extends BaseIntegrationTest{
	
	@Autowired
	private UserService userService;
	
	@Test
	public void testRegisterForFirstTime(){
		User user = getUser();
		userService.register(user);
		User user2 = userService.getUser(user.getId(), user.getPassword());
		assertNotNull(user2);
		user.setPassword(null);
		assertEquals(user, user2);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testRegisterForAlreadyExistingUser(){
		User user = getUser();
		userService.register(user);
		userService.register(user);
	}
	
	private User getUser() {
		String lastName = "Chockalingam";
		String firstName = "Vinodhini";
		String password = "chinnu";
		String emailId = "vinodhinic@gmail.com";
		String id = "chinnu";
		User user = new User(id, emailId, password, firstName, lastName);
		return user;
	}
}
