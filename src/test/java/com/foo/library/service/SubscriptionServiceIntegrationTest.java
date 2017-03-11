package com.foo.library.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.foo.library.model.BookCatalog;
import com.foo.library.model.Subscriber;

public class SubscriptionServiceIntegrationTest extends BaseIntegrationTest {
	
	@Autowired
	private SubscriptionService subscriptionService;
	
	@Autowired
	private BookService bookService;
	
	@Test
	public void testSubscribeForNewAdditions() {
		String userId = "chinnu";
		subscriptionService.subscribeForNewAdditions(userId);
		
		List<Subscriber> subscribersForNewAdditions = subscriptionService
				.getSubscribersForNewAdditions();
		assertNotNull(subscribersForNewAdditions);
		assertEquals(1, subscribersForNewAdditions.size());
		assertEquals(userId, subscribersForNewAdditions.get(0).getUserId());
		
		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = constructBookCatalog(bookName, author, isbn);
		// for now testing if the sysout gets printed
		bookService.addBookCatalogToLibrary(bookCatalog);
	}
	
	private BookCatalog constructBookCatalog(String bookName, String author,
			String isbn) {
		BookCatalog bookCatalog = new BookCatalog(bookName, author, isbn);
		return bookCatalog;
	}
}
