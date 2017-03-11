package com.foo.library.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.foo.library.model.Book;
import com.foo.library.model.BookCatalog;
import com.foo.library.repository.BookCatalogJpaRepository;
import com.foo.library.repository.BookJpaRepository;

public class BookServiceIntegrationTest extends BaseIntegrationTest {

	@Autowired
	private RatingAndReviewService ratingAndReviewService;
	
	@Autowired
	private BookService bookService;

	@Autowired
	private BookCatalogJpaRepository bookCatalogJpaRepository;

	@Autowired
	private BookJpaRepository bookJpaRepository;
	
	@Test
	public void testGetAllBookCatalogsWithRatingsAndAvailabilityForNoBookAndNoRating() {
		
		clearExistingBooks();
		
		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = constructBookCatalog(bookName, author, isbn);
		BookCatalog addBookCatalogToLibrary = bookService
				.addBookCatalogToLibrary(bookCatalog);

		List<BookCatalog> catalogsWithRatingsAndAvailability = bookService
				.getAllBookCatalogsWithRatingsAndAvailability();
		
		assertNotNull(catalogsWithRatingsAndAvailability);
		assertEquals(1, catalogsWithRatingsAndAvailability.size());
		assertEquals(addBookCatalogToLibrary.getId(),
				catalogsWithRatingsAndAvailability.get(0).getId());
		assertEquals(false, catalogsWithRatingsAndAvailability.get(0)
				.getIsAvailable());
		assertNull(catalogsWithRatingsAndAvailability.get(0).getAverageRating());
	}

	private void clearExistingBooks() {
		bookJpaRepository.deleteAll();
		bookCatalogJpaRepository.deleteAll();
	}

	@Test
	public void testGetAllBookCatalogsWithRatingsAndAvailabilityForBookAndRating() {
		
		clearExistingBooks();
		
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
		provider = "vino";
		book = new Book(provider, true, false, comments);
		bookService.addBookToTheCatalog(addBookCatalogToLibrary.getId(), book);

		Integer rating = 3;
		addRatingsForBookCatalog(addBookCatalogToLibrary.getId(), rating);

		isbn = "129847872";
		bookName = "Harry Potter And The Half Blood Prince";
		bookCatalog = constructBookCatalog(bookName, author, isbn);
		BookCatalog addBookCatalogToLibrary2 = bookService
				.addBookCatalogToLibrary(bookCatalog);
		rating = 2;
		addRatingsForBookCatalog(addBookCatalogToLibrary2.getId(), rating);

		List<BookCatalog> catalogsWithRatingsAndAvailability = bookService
				.getAllBookCatalogsWithRatingsAndAvailability();
		assertNotNull(catalogsWithRatingsAndAvailability);
		assertEquals(2, catalogsWithRatingsAndAvailability.size());

		List<BookCatalog> catalog1 = catalogsWithRatingsAndAvailability
				.stream()
				.filter(c -> Objects.equals(c.getId(),
						addBookCatalogToLibrary.getId()))
				.collect(Collectors.toList());
		assertEquals(1, catalog1.size());
		assertEquals(true, catalog1.get(0).getIsAvailable());
		assertEquals(new Double(3), catalog1.get(0).getAverageRating());
		assertEquals(2, catalog1.get(0).getBooks().size());

		List<BookCatalog> catalog2 = catalogsWithRatingsAndAvailability
				.stream()
				.filter(c -> Objects.equals(c.getId(),
						addBookCatalogToLibrary2.getId()))
				.collect(Collectors.toList());
		assertEquals(1, catalog2.size());
		assertEquals(false, catalog2.get(0).getIsAvailable());
		assertEquals(new Double(2), catalog2.get(0).getAverageRating());
	}

	private void addRatingsForBookCatalog(Long id, Integer rating) {
		String userId = "test";
		String review = "test-review";
		ratingAndReviewService.rateAndReview(id, userId, rating, review);
	}

	@Test
	public void testGetAllBookCatalogsWithRatingsAndAvailabilityForBookAndNoRating() {
		clearExistingBooks();
		
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
		provider = "vino";
		book = new Book(provider, true, false, comments);
		bookService.addBookToTheCatalog(addBookCatalogToLibrary.getId(), book);

		List<BookCatalog> catalogsWithRatingsAndAvailability = bookService
				.getAllBookCatalogsWithRatingsAndAvailability();
		assertNotNull(catalogsWithRatingsAndAvailability);
		assertEquals(1, catalogsWithRatingsAndAvailability.size());
		assertEquals(addBookCatalogToLibrary.getId(),
				catalogsWithRatingsAndAvailability.get(0).getId());
		assertEquals(true, catalogsWithRatingsAndAvailability.get(0)
				.getIsAvailable());
		assertNull(catalogsWithRatingsAndAvailability.get(0).getAverageRating());
		assertEquals(2, catalogsWithRatingsAndAvailability.get(0).getBooks()
				.size());
	}

	@Test
	public void testSearchBookCatalogByAuthor() {

		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = constructBookCatalog(bookName, author, isbn);
		BookCatalog addBookCatalogToLibrary = bookService
				.addBookCatalogToLibrary(bookCatalog);

		isbn = "129847873";
		bookName = "Harry Potter And The Chamber Of Secrets";
		bookCatalog = constructBookCatalog(bookName, author, isbn);
		BookCatalog addBookCatalogToLibrary2 = bookService
				.addBookCatalogToLibrary(bookCatalog);

		List<BookCatalog> searchBookCatalogByAuthor = bookService
				.searchBookCatalogByAuthor(author);
		assertNotNull(searchBookCatalogByAuthor);
		assertTrue(searchBookCatalogByAuthor.contains(addBookCatalogToLibrary));
		assertTrue(searchBookCatalogByAuthor.contains(addBookCatalogToLibrary2));
	}

	@Test
	public void testSearchBookCatalogByBookName() {

		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = constructBookCatalog(bookName, author, isbn);
		bookService.addBookCatalogToLibrary(bookCatalog);

		isbn = "129847873";
		bookName = "Harry Potter And The Chamber Of Secrets";
		bookCatalog = constructBookCatalog(bookName, author, isbn);
		BookCatalog addBookCatalogToLibrary2 = bookService
				.addBookCatalogToLibrary(bookCatalog);

		List<BookCatalog> searchBookCatalogByBookName = bookService
				.searchBookCatalogByBookName(bookName);
		assertNotNull(searchBookCatalogByBookName);
		assertEquals(1, searchBookCatalogByBookName.size());
		assertTrue(searchBookCatalogByBookName
				.contains(addBookCatalogToLibrary2));
	}

	@Test
	public void testSearchBookCatalogByIsbn() {

		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = constructBookCatalog(bookName, author, isbn);
		bookService.addBookCatalogToLibrary(bookCatalog);

		isbn = "129847873";
		bookName = "Harry Potter And The Chamber Of Secrets";
		bookCatalog = constructBookCatalog(bookName, author, isbn);
		BookCatalog addBookCatalogToLibrary2 = bookService
				.addBookCatalogToLibrary(bookCatalog);

		List<BookCatalog> searchBookCatalogByAuthor = bookService
				.searchBookCatalogByIsbn(isbn);
		assertNotNull(searchBookCatalogByAuthor);
		assertEquals(1, searchBookCatalogByAuthor.size());
		assertTrue(searchBookCatalogByAuthor.contains(addBookCatalogToLibrary2));
	}

	@Test
	public void testGetAllBookCatalogs() {
		clearExistingBooks();
		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = constructBookCatalog(bookName, author, isbn);
		BookCatalog addBookCatalogToLibrary = bookService
				.addBookCatalogToLibrary(bookCatalog);

		assertNotNull(addBookCatalogToLibrary.getId());
		String comments = "test insert";
		String provider = "chinnu";
		Book book = new Book(provider, true, true, comments);
		Book addBookToTheCatalog1 = bookService.addBookToTheCatalog(
				addBookCatalogToLibrary.getId(), book);
		provider = "vino";
		book = new Book(provider, true, true, comments);
		Book addBookToTheCatalog2 = bookService.addBookToTheCatalog(
				addBookCatalogToLibrary.getId(), book);

		List<BookCatalog> allBookCatalogs = bookService.getAllBookCatalogs();
		assertNotNull(allBookCatalogs);
		assertEquals(allBookCatalogs.size(), 1);
		assertEquals(2, allBookCatalogs.get(0).getBooks().size());
		assertTrue(allBookCatalogs.get(0).getBooks()
				.contains(addBookToTheCatalog1));
		assertTrue(allBookCatalogs.get(0).getBooks()
				.contains(addBookToTheCatalog2));
	}

	@Test
	public void testAddBookCatalogToLibrary() {
		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = createBookCatalog(bookName, author, isbn);

		BookCatalog addBookCatalogToLibrary = bookService
				.addBookCatalogToLibrary(bookCatalog);
		assertNotNull(addBookCatalogToLibrary);
		assertNotNull(addBookCatalogToLibrary.getId());
		List<BookCatalog> findBookCatalogByIsbn = bookService
				.searchBookCatalogByIsbn(bookCatalog.getIsbn());
		assertNotNull(findBookCatalogByIsbn);
		assertEquals(1, findBookCatalogByIsbn.size());
		assertEquals(addBookCatalogToLibrary, findBookCatalogByIsbn.get(0));
	}

	@Test
	public void testAddBookToTheCatalog() {
		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = constructBookCatalog(bookName, author, isbn);
		BookCatalog addBookCatalogToLibrary = bookService
				.addBookCatalogToLibrary(bookCatalog);

		String comments = "test insert";
		String provider = "chinnu";
		Book book = new Book(provider, true, true, comments);

		Book addBookToTheCatalog = bookService.addBookToTheCatalog(
				addBookCatalogToLibrary.getId(), book);
		assertNotNull(addBookToTheCatalog);
		assertNotNull(addBookToTheCatalog.getId());
		assertEquals(addBookCatalogToLibrary.getId(), addBookToTheCatalog
				.getBookCatalog().getId());
	}

	private BookCatalog createBookCatalog(String bookName, String author,
			String isbn) {
		BookCatalog bookCatalog = constructBookCatalog(bookName, author, isbn);
		return bookCatalogJpaRepository.saveAndFlush(bookCatalog);
	}

	private BookCatalog constructBookCatalog(String bookName, String author,
			String isbn) {
		BookCatalog bookCatalog = new BookCatalog(bookName, author, isbn);
		return bookCatalog;
	}
}
