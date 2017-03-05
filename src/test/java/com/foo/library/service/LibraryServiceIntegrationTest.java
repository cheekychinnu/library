package com.foo.library.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.foo.library.model.BookCatalog;
import com.foo.library.repository.BookCatalogJpaRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LibraryServiceIntegrationTest {

	@Autowired
	private LibraryService libraryService;

	@Autowired
	private BookCatalogJpaRepository bookCatalogJpaRepository;

	@Test
	public void testAddBookCatalogToLibrary() {
		BookCatalog bookCatalog = createTestBookCatalog1();
		BookCatalog addBookCatalogToLibrary = libraryService
				.addBookCatalogToLibrary(bookCatalog);
		assertNotNull(addBookCatalogToLibrary);
		assertNotNull(addBookCatalogToLibrary.getId());
		List<BookCatalog> findBookCatalogByIsbn = libraryService
				.findBookCatalogByIsbn(bookCatalog.getIsbn());
		assertNotNull(findBookCatalogByIsbn);
		assertEquals(1, findBookCatalogByIsbn.size());
		assertEquals(addBookCatalogToLibrary, findBookCatalogByIsbn.get(0));
	}
	
	@Test
	public void test()
	{
		assertTrue(bookCatalogJpaRepository.findAll().isEmpty());
		String isbn = "129847874";
		List<BookCatalog> findBookCatalogByIsbn = libraryService
				.findBookCatalogByIsbn(isbn);
		assertNotNull(findBookCatalogByIsbn);
		assertTrue(findBookCatalogByIsbn.isEmpty());
	}
	
	private BookCatalog createTestBookCatalog1() {
		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = new BookCatalog(bookName, author, isbn);
		return bookCatalogJpaRepository.saveAndFlush(bookCatalog);
	}
}
