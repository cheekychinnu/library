package com.foo.library.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.HandlerInterceptor;

import com.foo.library.config.AdminInterceptor;
import com.foo.library.controller.validator.BookCatalogValidator;
import com.foo.library.controller.validator.BookValidator;
import com.foo.library.model.Book;
import com.foo.library.model.BookCatalog;
import com.foo.library.model.User;
import com.foo.library.service.LibraryService;

@WebMvcTest(AdminController.class)
@RunWith(SpringRunner.class)
public class AdminControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private LibraryService libraryService;

	@SpyBean
	private BookCatalogValidator bookCatalogValidator;

	@SpyBean
	private BookValidator bookValidator;
	
	@Test
	public void testForNotLoggedInCall() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(get("/admin"))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(
						flash().attribute(
								"loginError",
								equalTo("Please enter admin credentials to continue")))
				.andExpect(redirectedUrl("/login")).andReturn();
		assertNotNull(mvcResult);
		List<HandlerInterceptor> interceptors = Arrays.asList(mvcResult
				.getInterceptors());
		assertTrue(
				"Expecting login interceptor in this path for cases where there is no logged in user",
				interceptors.stream().anyMatch(
						i -> i.getClass().equals(AdminInterceptor.class)));
	}

	@Test
	public void testForLoggedInCall() throws Exception {
		User user = new User();
		user.setId("admin");
		MvcResult mvcResult = mockMvc
				.perform(get("/admin").sessionAttr("loggedInUser", user))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(view().name(equalTo("admin"))).andReturn();
		assertNotNull(mvcResult);
		List<HandlerInterceptor> interceptors = Arrays.asList(mvcResult
				.getInterceptors());
		assertTrue(
				"Expecting login interceptor in this path for cases where there is no logged in user",
				interceptors.stream().anyMatch(
						i -> i.getClass().equals(AdminInterceptor.class)));

	}

	@Test
	public void testForAddBookCatalogWithEmptyName() throws Exception {
		User user = new User();
		user.setId("admin");
		mockMvc.perform(
				post("/admin/bookCatalog/create").header("referer", "something")
						.sessionAttr("loggedInUser", user))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(
						model().attributeHasFieldErrors("bookCatalog", "name",
								"author", "isbn"))
				.andExpect(view().name(equalTo("admin")));
	}

	@Test
	public void testForAlreadyAddedBookCatalog() throws Exception {
		BookCatalog bookCatalog = new BookCatalog();
		bookCatalog.setName("someName");
		bookCatalog.setAuthor("someAuthor");
		bookCatalog.setIsbn("64873");

		List<BookCatalog> searchList = Stream.of(bookCatalog).collect(
				Collectors.toList());
		when(libraryService.searchBookCatalogByBookName(bookCatalog.getName()))
				.thenReturn(searchList);

		User user = new User();
		user.setId("admin");

		mockMvc.perform(
				post("/admin/bookCatalog/create").header("referer", "something")
						.sessionAttr("loggedInUser", user)
						.param("name", bookCatalog.getName())
						.param("isbn", bookCatalog.getIsbn())
						.param("author", bookCatalog.getAuthor()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(
						model().attribute(
								"addBookCatalogMessage",
								equalTo("Catalog already present with name "
										+ bookCatalog.getName())))
				.andExpect(
						model().attribute(
								"bookCatalog",
								hasProperty("name",
										equalTo(bookCatalog.getName()))))
				.andExpect(
						model().attribute(
								"bookCatalog",
								hasProperty("isbn",
										equalTo(bookCatalog.getIsbn()))))
				.andExpect(
						model().attribute(
								"bookCatalog",
								hasProperty("author",
										equalTo(bookCatalog.getAuthor()))))
				.andExpect(view().name(equalTo("admin")));
	}
	
	@Test
	public void testForSuccessfulAddBookCatalog() throws Exception {
		BookCatalog bookCatalog = new BookCatalog();
		bookCatalog.setName("someName");
		bookCatalog.setAuthor("someAuthor");
		bookCatalog.setIsbn("64873");

		when(libraryService.searchBookCatalogByBookName(bookCatalog.getName()))
				.thenReturn(null);
		when(libraryService.searchBookCatalogByIsbn(bookCatalog.getName()))
		.thenReturn(null);

		when(libraryService.addBookCatalogToLibrary(bookCatalog)).thenReturn(bookCatalog);
		User user = new User();
		user.setId("admin");

		String referer = "something";
		mockMvc.perform(
				post("/admin/bookCatalog/create").header("referer", referer)
						.sessionAttr("loggedInUser", user)
						.param("name", bookCatalog.getName())
						.param("isbn", bookCatalog.getIsbn())
						.param("author", bookCatalog.getAuthor()))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(
						flash().attribute(
								"addBookCatalogMessage",
								equalTo("Successfully added the catalog")))
				.andExpect(
						model().attributeDoesNotExist("bookCatalog"))
				.andExpect(view().name(equalTo("redirect:"+referer)));
	}
	
	@Test
	public void testForAddBookWithEmptyProvider() throws Exception {
		User user = new User();
		user.setId("admin");
		mockMvc.perform(
				post("/admin/book/create").header("referer", "something")
						.sessionAttr("loggedInUser", user))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(
						model().attributeHasFieldErrors("book", "provider"))
				.andExpect(view().name(equalTo("admin")));
	}

	@Test
	public void testForSuccessfulAddBook() throws Exception {
		Book book= new Book();
		book.setComments("xyc");
		book.setProvider("chinnu");
		BookCatalog bookCatalog = new BookCatalog();
		Long bookCatalogId = 1L;
		bookCatalog.setId(bookCatalogId);
		book.setBookCatalog(bookCatalog);
		when(libraryService.addBookToTheCatalog(bookCatalogId, book))
				.thenReturn(book);
		
		User user = new User();
		user.setId("admin");

		String referer = "something";
		mockMvc.perform(
				post("/admin/book/create").header("referer", referer)
						.sessionAttr("loggedInUser", user)
						.param("provider", book.getProvider())
						.param("bookCatalog.id", bookCatalogId.toString()))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(
						flash().attribute(
								"addBookMessage",
								equalTo("Successfully added to the catalog")))
				.andExpect(
						model().attributeDoesNotExist("book"))
				.andExpect(view().name(equalTo("redirect:"+referer)));
	}
}