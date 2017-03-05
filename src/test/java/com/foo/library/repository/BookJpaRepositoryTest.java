package com.foo.library.repository;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.foo.library.model.Book;
import com.foo.library.model.BookCatalog;

@RunWith(SpringRunner.class)
@DataJpaTest
// this by default supports transactional. hence your test data rollbacks after
// each test method
public class BookJpaRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private BookJpaRepository bookJpaRepository;

	@Autowired
	private BookCatalogJpaRepository bookCatalogJpaRepository;

	@Test
	public void testCreateAndUpdate() {

		// Note catalog has to be created before book can be created. you cannot
		// expect both to be created in just create book repository call
		BookCatalog createdCatalog = createTestBookCatalog1();

		// while creating book all I need is the catalog id.
		BookCatalog bookCatalogWithOnlyIdSet = new BookCatalog();
		bookCatalogWithOnlyIdSet.setId(createdCatalog.getId());
		// the bookcatalog of the book can have other properties set to
		// different values as the original one.
		// this will not update book catalog, just like how book catalog cannot
		// be created in a book create call
		bookCatalogWithOnlyIdSet.setAuthor("random");

		String comments = "test insert";
		String provider = "chinnu";
		Book book = new Book(provider, true, true, comments);
		book.setBookCatalog(bookCatalogWithOnlyIdSet);

		Book createdBook = bookJpaRepository.saveAndFlush(book);
		assertNotNull(createdBook);
		assertNotNull(createdBook.getId());

		// clear the persistence context so we don't return the previously
		// cached object
		entityManager.clear();

		/**
		 * direct object comparison is not possible. why?
		 * 
		 * <pre>
		 * java.lang.AssertionError: expected: com.foo.library.model.BookCatalog<BookCatalog [id=10, name=Harry Potter And The Prisoner Of Azkhaban, author=J.K.Rowling, isbn=129847874]> but was: com.foo.library.model.BookCatalog_$$_jvsteb4_0<BookCatalog [id=10, name=Harry Potter And The Prisoner Of Azkhaban, author=J.K.Rowling, isbn=129847874]>
		 * </pre>
		 */
		BookCatalog catalog = bookCatalogJpaRepository.getOne(createdCatalog
				.getId());
		assertEquals(createdCatalog, catalog);

		book = bookJpaRepository.getOne(createdBook.getId());
		/**
		 * direct object comparison not possible. why?
		 * 
		 * <pre>
		 * java.lang.AssertionError: expected: com.foo.library.model.BookCatalog_$$_jvstd61_0<BookCatalog [id=10, name=Harry Potter And The Prisoner Of Azkhaban, author=J.K.Rowling, isbn=129847874]> but was: com.foo.library.model.BookCatalog_$$_jvstd61_0<BookCatalog [id=10, name=Harry Potter And The Prisoner Of Azkhaban, author=J.K.Rowling, isbn=129847874]>
		 * 
		 * I have changed the equals method : use instance of instead of getClass()
		 * And use getters to access other object's properties. I guess this has to do with hibernate's lazy initializers
		 */
		assertEquals(catalog, book.getBookCatalog());

		BookCatalog bookCatalog2 = createTestBookCatalog2();
		book.setBookCatalog(bookCatalog2);
		Book updatedBook = bookJpaRepository.saveAndFlush(book);
		assertEquals(book.getId(), updatedBook.getId());
		updatedBook = bookJpaRepository.getOne(updatedBook.getId());
		assertEquals(book.getBookCatalog().getId(), updatedBook
				.getBookCatalog().getId());
	}

	@Test
	public void testFindByBookCatalog() {
		BookCatalog bookCatalog = createTestBookCatalog1();

		String comments = "test insert";
		String provider = "chinnu";
		Book book = new Book(provider, true, true, comments);
		book.setBookCatalog(bookCatalog);
		bookJpaRepository.saveAndFlush(book);

		List<Book> findByBookCatalog = bookJpaRepository
				.findByBookCatalogId(bookCatalog.getId());
		assertNotNull(findByBookCatalog);
		assertEquals(1, findByBookCatalog.size());
		assertEquals(book, findByBookCatalog.get(0));
	}

	@Test
	public void testFindByBookCatalogIdAndIsActiveTrue() {
		BookCatalog bookCatalog = createTestBookCatalog1();

		String comments = "test insert";
		String provider = "chinnu";
		Book book = new Book(provider, true, false, comments);
		book.setBookCatalog(bookCatalog);
		bookJpaRepository.saveAndFlush(book);

		List<Book> findByBookCatalog = bookJpaRepository
				.findByBookCatalogIdAndIsActiveTrue(bookCatalog.getId());
		assertNotNull(findByBookCatalog);
		assertEquals(1, findByBookCatalog.size());
		assertEquals(book, findByBookCatalog.get(0));
	}

	@Test
	public void testUpdateIsActive()
	{
		BookCatalog bookCatalog = createTestBookCatalog1();

		String comments = "test insert";
		String provider = "chinnu";
		Book book = new Book(provider, true, true, comments);
		book.setBookCatalog(bookCatalog);
		book = bookJpaRepository.saveAndFlush(book);
		int updatedRecords = bookJpaRepository.updateIsActiveAndIsAvailable(book.getId(), false, false);
		assertEquals(1, updatedRecords);
		entityManager.clear();
		book = bookJpaRepository.getOne(book.getId());
		assertNotNull(book);
		assertFalse(book.getIsActive());
		assertFalse(book.getIsAvailable());
	}
	
	@Test
	public void testFindByBookCatalogIdAndIsActiveTrueAndIsAvailableTrue() {
		BookCatalog bookCatalog = createTestBookCatalog1();

		String comments = "test insert";
		String provider = "chinnu";
		Book book = new Book(provider, true, false, comments);
		book.setBookCatalog(bookCatalog);
		bookJpaRepository.saveAndFlush(book);

		List<Book> findByBookCatalog = bookJpaRepository
				.findByBookCatalogIdAndIsActiveTrueAndIsAvailableTrue(bookCatalog.getId());
		assertNotNull(findByBookCatalog);
		assertEquals(0, findByBookCatalog.size());
	}
	
	private BookCatalog createTestBookCatalog1() {
		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = new BookCatalog(bookName, author, isbn);
		return bookCatalogJpaRepository.saveAndFlush(bookCatalog);
	}

	private BookCatalog createTestBookCatalog2() {
		String author = "J.K.Rowling";
		String isbn = "129847872";
		String bookName = "Harry Potter And The Chamber of Secrets";
		BookCatalog bookCatalog = new BookCatalog(bookName, author, isbn);
		return bookCatalogJpaRepository.saveAndFlush(bookCatalog);
	}
}
