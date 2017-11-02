package com.foo.library.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.Collections;
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
import com.foo.library.model.Rent;
import com.foo.library.model.RentResponse;
import com.foo.library.model.ReturnResponse;
import com.foo.library.model.User;
import com.foo.library.service.LibraryService;

@WebMvcTest(BookController.class)
@RunWith(SpringRunner.class)
public class BookControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private LibraryService libraryService;

	@Test
	public void testForNotLoggedInCall() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(get("/books/getAllBooks"))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(
						flash().attribute("loginError",
								equalTo("Please login to continue")))
				.andExpect(redirectedUrl("/login")).andReturn();
		assertNotNull(mvcResult);
		List<HandlerInterceptor> interceptors = Arrays.asList(mvcResult
				.getInterceptors());
		assertTrue(
				"Expecting login interceptor in this path for cases where there is no logged in user",
				interceptors.stream().anyMatch(
						i -> i.getClass().equals(LoginInterceptor.class)));
	}

	@Test
	public void testForLoggedInCall() throws Exception {
		User user = new User();
		user.setId("vinodhini");
		MvcResult mvcResult = mockMvc
				.perform(
						get("/books/getAllBooks").sessionAttr("loggedInUser",
								user)).andDo(print())
				.andExpect(status().isOk())
				.andExpect(view().name(equalTo("book"))).andReturn();
		assertNotNull(mvcResult);
		List<HandlerInterceptor> interceptors = Arrays.asList(mvcResult
				.getInterceptors());
		assertTrue(
				"Expecting login interceptor in this path for cases where there is no logged in user",
				interceptors.stream().anyMatch(
						i -> i.getClass().equals(LoginInterceptor.class)));
	}

	@Test
	public void testForGetAllBooks() throws Exception {
		User user = new User();
		user.setId("vinodhini");

		when(libraryService.getAllBookCatalogsWithRatingsAndAvailability())
				.thenReturn(Collections.emptyList());

		mockMvc.perform(
				get("/books/getAllBooks").sessionAttr("loggedInUser", user))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(view().name(equalTo("book")))
				.andExpect(model().attributeExists("allBookCatalogs"));
		
		verify(libraryService).getAllBookCatalogsWithRatingsAndAvailability();
	}
	
	@Test
	public void testRentForSuccess() throws Exception {
		User user = new User();
		String userId = "vinodhini";
		user.setId(userId);

		Long bookCatalogId = 1L;
		RentResponse rentResponse = new RentResponse();
		rentResponse.setRent(new Rent());
		rentResponse.setIsSuccess(true);

		when(libraryService.rentBook(userId, bookCatalogId)).thenReturn(
				rentResponse);

		String referer = "something";
		mockMvc.perform(
				get("/books/rent?bookCatalogId=1").sessionAttr("loggedInUser", user)
						.header("referer", referer))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(
						flash().attribute("rentResult",
								containsString("Please return it before :")))
				.andExpect(view().name(equalTo("redirect:" + referer)));
		verify(libraryService).rentBook(userId, bookCatalogId);
	}
	
	@Test
	public void testRentForFailure() throws Exception {
		User user = new User();
		String userId = "vinodhini";
		user.setId(userId);

		Long bookCatalogId = 1L;
		RentResponse rentResponse = new RentResponse();
		rentResponse.setIsSuccess(false);
		String failureMessage = "some failure message";
		rentResponse.setMessage(failureMessage);

		when(libraryService.rentBook(userId, bookCatalogId)).thenReturn(
				rentResponse);

		String referer = "something";
		mockMvc.perform(
				get("/books/rent?bookCatalogId=1").sessionAttr("loggedInUser", user)
						.header("referer", referer))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(
						flash().attribute("rentResult",
								equalTo(failureMessage)))
				.andExpect(view().name(equalTo("redirect:" + referer)));
		
		verify(libraryService).rentBook(userId, bookCatalogId);
	}
	
	@Test
	public void testReturnForDueDateNotMissed() throws Exception {
		User user = new User();
		user.setId("vinodhini");

		Long rentId = 1L;
		ReturnResponse returnResponse = new ReturnResponse();
		boolean isDueDateMissed = false;
		returnResponse.setIsDueDateMissed(isDueDateMissed);
		
		when(libraryService.returnBook(rentId)).thenReturn(
				returnResponse);

		String referer = "something";
		mockMvc.perform(
				get("/books/return?rentId=1").sessionAttr("loggedInUser", user)
						.header("referer", referer))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(
						flash().attribute("isDueDateMissed",
								equalTo(isDueDateMissed)))
				.andExpect(view().name(equalTo("redirect:" + referer)));
		verify(libraryService).returnBook(rentId);
	}
	
	@Test
	public void testReturnForDueDateMissed() throws Exception {
		User user = new User();
		user.setId("vinodhini");

		Long rentId = 1L;
		ReturnResponse returnResponse = new ReturnResponse();
		boolean isDueDateMissed = true;
		returnResponse.setIsDueDateMissed(isDueDateMissed);
		
		when(libraryService.returnBook(rentId)).thenReturn(
				returnResponse);

		String referer = "something";
		mockMvc.perform(
				get("/books/return?rentId=1").sessionAttr("loggedInUser", user)
						.header("referer", referer))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(
						flash().attribute("isDueDateMissed",
								equalTo(isDueDateMissed)))
				.andExpect(view().name(equalTo("redirect:" + referer)));
		
		verify(libraryService).returnBook(rentId);
	}
	
	@Test
	public void testWatch() throws Exception {
		User user = new User();
		String userId = "vinodhini";
		user.setId(userId);

		Long bookCatalogId = 1L;
		
		String referer = "something";
		mockMvc.perform(
				get("/books/watch?bookCatalogId=1").sessionAttr("loggedInUser", user)
						.header("referer", referer))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(
						flash().attribute("watchMessage", equalTo("We will notify when the book becomes available")))
				.andExpect(view().name(equalTo("redirect:" + referer)));
		
		verify(libraryService).watchForBookCatalog(userId, bookCatalogId);
	}
}
