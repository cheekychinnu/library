package com.foo.library.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.foo.library.model.Book;
import com.foo.library.model.BookCatalog;
import com.foo.library.model.RentResponse;

public class RentServiceIntegrationTest extends BaseIntegrationTest {

	@Autowired
	private RentService rentService;

	@Autowired
	private BookService bookService;

	@Test
	public void testRentAvailableBook() {
		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = constructBookCatalog(bookName, author, isbn);
		BookCatalog addBookCatalogToLibrary = bookService
				.addBookCatalogToLibrary(bookCatalog);

		String comments = "test insert";
		String provider = "chinnu";
		Book book = new Book(provider, true, true, comments);
		bookService.addBookToTheCatalog(addBookCatalogToLibrary.getId(), book);

		String userId = "chinnu";
		Long bookId = book.getId();
		RentResponse response = rentService.rentBook(userId, bookId);
		assertNotNull(response);
		assertTrue(response.getIsSuccess());
		assertNotNull(response.getRent());
		System.out.println(response.getRent().getDueDate());
	}

	@Test
	public void testRentUnavailableBook() {
		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = constructBookCatalog(bookName, author, isbn);
		BookCatalog addBookCatalogToLibrary = bookService
				.addBookCatalogToLibrary(bookCatalog);

		String comments = "test insert";
		String provider = "chinnu";
		Book book = new Book(provider, true, false, comments);
		bookService.addBookToTheCatalog(addBookCatalogToLibrary.getId(), book);

		String userId = "chinnu";
		Long bookId = book.getId();
		RentResponse response = rentService.rentBook(userId, bookId);
		assertNotNull(response);
		assertFalse(response.getIsSuccess());
		assertNotNull(response.getMessage());
	}

	private BookCatalog constructBookCatalog(String bookName, String author,
			String isbn) {
		BookCatalog bookCatalog = new BookCatalog(bookName, author, isbn);
		return bookCatalog;
	}

}
