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
import com.foo.library.repository.BookCatalogJpaRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BookCatalogJpaRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private BookCatalogJpaRepository bookCatalogJpaRepository;

	@Autowired
	private BookJpaRepository bookJpaRepository;

	@Test
	public void testCreateAndUpdate() {
		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = new BookCatalog(bookName, author, isbn);

		entityManager.clear();

		// create
		bookCatalogJpaRepository.saveAndFlush(bookCatalog);

		List<BookCatalog> bookCatalogs = bookCatalogJpaRepository
				.findByAuthorContainingIgnoreCase(author);

		assertNotNull(bookCatalogs);
		assertFalse(bookCatalogs.isEmpty());

		BookCatalog createdCatalog = bookCatalogs.get(0);

		assertNotNull(createdCatalog.getId());
		assertEquals(author, createdCatalog.getAuthor());
		assertEquals(isbn, createdCatalog.getIsbn());
		assertEquals(bookName, createdCatalog.getName());

		String updatedBookName = "Harry Potter And The Chamber Of Secrets";
		bookCatalog.setName(updatedBookName);

		bookCatalogJpaRepository.saveAndFlush(bookCatalog);

		BookCatalog updatedCatalog = bookCatalogJpaRepository
				.getOne(createdCatalog.getId());
		assertEquals(createdCatalog.getId(), updatedCatalog.getId());
		assertEquals(createdCatalog.getAuthor(), updatedCatalog.getAuthor());
		assertEquals(createdCatalog.getIsbn(), updatedCatalog.getIsbn());
		assertEquals(updatedBookName, updatedCatalog.getName());
	}

	@Test
	public void testDelete() {
		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = new BookCatalog(bookName, author, isbn);
		bookCatalogJpaRepository.saveAndFlush(bookCatalog);

		List<BookCatalog> allCatalogs = bookCatalogJpaRepository.findAll();
		assertNotNull(allCatalogs);
		assertEquals(1, allCatalogs.size());

		Long bookCatalogId = allCatalogs.get(0).getId();
		bookCatalogJpaRepository.delete(bookCatalogId);

		allCatalogs = bookCatalogJpaRepository.findAll();
		assertNotNull(allCatalogs);
		assertTrue(allCatalogs.isEmpty());
	}

	@Test
	public void testFindByXXXContaining() {
		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = new BookCatalog(bookName, author, isbn);

		entityManager.clear();
		bookCatalogJpaRepository.saveAndFlush(bookCatalog);

		List<BookCatalog> catalogs = bookCatalogJpaRepository
				.findByAuthorContainingIgnoreCase("rowling");
		assertNotNull(catalogs);
		assertEquals(1, catalogs.size());

		catalogs = bookCatalogJpaRepository
				.findByNameContainingIgnoreCase("potter");
		assertNotNull(catalogs);
		assertEquals(1, catalogs.size());

		catalogs = bookCatalogJpaRepository.findByIsbn(isbn);
		assertNotNull(catalogs);
		assertEquals(1, catalogs.size());
	}

	@Test
	public void testIfBooksAreReceivedWhileGettingBookCatalog() {
		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = new BookCatalog(bookName, author, isbn);
		bookCatalog = bookCatalogJpaRepository.saveAndFlush(bookCatalog);

		String comments = "test insert";
		String provider = "chinnu";
		Book book = new Book(provider, true, false, comments);
		book.setBookCatalog(bookCatalog);
		book = bookJpaRepository.saveAndFlush(book);

		entityManager.clear();
		BookCatalog getBookCatalog = bookCatalogJpaRepository
				.getOne(bookCatalog.getId());
		assertNotNull(getBookCatalog);
		assertEquals(bookCatalog.getId(), getBookCatalog.getId());
		assertNotNull(getBookCatalog.getBooks());
		assertEquals(1, getBookCatalog.getBooks().size());
		assertEquals(book, getBookCatalog.getBooks().get(0));
	}

	@Test
	public void testFindByIdAndBookIsActiveTrueAndBookIsAvailableTrue() {
		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = new BookCatalog(bookName, author, isbn);
		bookCatalog = bookCatalogJpaRepository.saveAndFlush(bookCatalog);

		String comments = "test insert";
		String provider = "chinnu";
		Book book = new Book(provider, false, true, comments);
		book.setBookCatalog(bookCatalog);
		book = bookJpaRepository.saveAndFlush(book);

		entityManager.clear();
		List<BookCatalog> bookCatalogs = bookCatalogJpaRepository
				.queryByIdAndActiveAvailableBooks(bookCatalog
						.getId());
		assertNotNull(bookCatalogs);
		assertEquals(0, bookCatalogs.size());
	}
}
