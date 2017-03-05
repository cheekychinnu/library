package com.foo.library.repository;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.foo.library.model.Book;
import com.foo.library.model.BookCatalog;
import com.foo.library.model.RatingAndReview;
import com.foo.library.model.RatingAndReviewPK;
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

	@Autowired
	private RatingAndReviewJpaRepository ratingAndReviewJpaRepository;

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
				.queryByIdAndActiveAvailableBooks(bookCatalog.getId());
		assertNotNull(bookCatalogs);
		assertEquals(0, bookCatalogs.size());
	}

	@Test
	public void testAverageRating() {
		BookCatalog bookCatalog = createTestBookCatalog();
		String userId = "vino";

		RatingAndReview ratingAndReview = new RatingAndReview();
		ratingAndReview.setRating(2);
		ratingAndReview.setReview("test review");
		RatingAndReviewPK ratingAndReviewPK = new RatingAndReviewPK(userId,
				bookCatalog.getId());
		ratingAndReview.setId(ratingAndReviewPK);

		ratingAndReviewJpaRepository.saveAndFlush(ratingAndReview);

		userId = "chinnu";
		ratingAndReview = new RatingAndReview();
		ratingAndReview.setRating(5);
		ratingAndReview.setReview("test review");
		ratingAndReviewPK = new RatingAndReviewPK(userId, bookCatalog.getId());
		ratingAndReview.setId(ratingAndReviewPK);
		ratingAndReviewJpaRepository.saveAndFlush(ratingAndReview);
		entityManager.clear();

		List<BookCatalog> bookCatalogs = new ArrayList<>();
		bookCatalogs.add(bookCatalog);
		List<BookCatalog> averageRatingForAllBookCatalogs = bookCatalogJpaRepository
				.fillCatalogWithAverageRatingIfRatingsArePresent(bookCatalogs);

		assertNotNull(averageRatingForAllBookCatalogs);
		assertEquals(1, averageRatingForAllBookCatalogs.size());
		assertEquals(bookCatalog, averageRatingForAllBookCatalogs.get(0));
		Double averageRating = averageRatingForAllBookCatalogs.get(0)
				.getAverageRating();
		assertNotNull(averageRating);
		assertEquals(new Double(3.5d), averageRating);
	}

	@Test
	public void testAverageRatingForBookWithOneNullRating() {
		BookCatalog bookCatalog = createTestBookCatalog();
		String userId = "chinnu";

		RatingAndReview ratingAndReview = new RatingAndReview();
		ratingAndReview.setReview("test review");
		RatingAndReviewPK ratingAndReviewPK = new RatingAndReviewPK(userId,
				bookCatalog.getId());
		ratingAndReview.setId(ratingAndReviewPK);

		ratingAndReviewJpaRepository.saveAndFlush(ratingAndReview);

		userId = "vino";
		ratingAndReview = new RatingAndReview();
		ratingAndReview.setRating(5);
		ratingAndReview.setReview("test review");
		ratingAndReviewPK = new RatingAndReviewPK(userId, bookCatalog.getId());
		ratingAndReview.setId(ratingAndReviewPK);
		ratingAndReviewJpaRepository.saveAndFlush(ratingAndReview);
		entityManager.clear();

		List<BookCatalog> bookCatalogs = new ArrayList<>();
		bookCatalogs.add(bookCatalog);
		List<BookCatalog> averageRatingForAllBookCatalogs = bookCatalogJpaRepository
				.fillCatalogWithAverageRatingIfRatingsArePresent(bookCatalogs);
		assertNotNull(averageRatingForAllBookCatalogs);
		assertEquals(1, averageRatingForAllBookCatalogs.size());
		assertEquals(bookCatalog, averageRatingForAllBookCatalogs.get(0));
		Double averageRating = averageRatingForAllBookCatalogs.get(0)
				.getAverageRating();
		assertNotNull(averageRating);
		assertEquals(new Double(5), averageRating);
	}

	@Test
	public void testAverageRatingForBookWithNoRating() {
		BookCatalog bookCatalog = createTestBookCatalog();
		String userId = "chinnu";

		RatingAndReview ratingAndReview = new RatingAndReview();
		ratingAndReview.setReview("test review");
		RatingAndReviewPK ratingAndReviewPK = new RatingAndReviewPK(userId,
				bookCatalog.getId());
		ratingAndReview.setId(ratingAndReviewPK);

		ratingAndReviewJpaRepository.saveAndFlush(ratingAndReview);

		List<BookCatalog> bookCatalogs = new ArrayList<>();
		bookCatalogs.add(bookCatalog);
		List<BookCatalog> averageRatingForAllBookCatalogs = bookCatalogJpaRepository
				.fillCatalogWithAverageRatingIfRatingsArePresent(bookCatalogs);
		assertNotNull(averageRatingForAllBookCatalogs);
		assertEquals(1, averageRatingForAllBookCatalogs.size());
		assertEquals(bookCatalog, averageRatingForAllBookCatalogs.get(0));
		Double averageRating = averageRatingForAllBookCatalogs.get(0)
				.getAverageRating();
		assertNull(averageRating);
	}

	@Test
	public void testAverageRatingForBookWithNoRatingEntry() {
		BookCatalog bookCatalog = createTestBookCatalog();
		List<BookCatalog> bookCatalogs = new ArrayList<>();
		bookCatalogs.add(bookCatalog);
		List<BookCatalog> averageRatingForAllBookCatalogs = bookCatalogJpaRepository
				.fillCatalogWithAverageRatingIfRatingsArePresent(bookCatalogs);
		assertNotNull(averageRatingForAllBookCatalogs);
		assertEquals(0, averageRatingForAllBookCatalogs.size());
	}

	@Test
	public void testAvailability() {
		Book book = createTestBook();
		List<Long> bookCatalogs = new ArrayList<>();
		entityManager.clear();
		BookCatalog bookCatalog = bookCatalogJpaRepository.findOne(book
				.getBookCatalog().getId());
		bookCatalogs.add(bookCatalog.getId());
		assertEquals(book, bookCatalog.getBooks().get(0));
		List<Long> updateCatalogWithAvailability = bookCatalogJpaRepository
				.getAvailableAndActiveBookCatalogIds(bookCatalogs);
		assertNotNull(updateCatalogWithAvailability);
		assertEquals(1, updateCatalogWithAvailability.size());
		assertEquals(bookCatalog.getId(), updateCatalogWithAvailability.get(0));

		book.setIsActive(false);
		bookJpaRepository.saveAndFlush(book);
		entityManager.clear();
		updateCatalogWithAvailability = bookCatalogJpaRepository
				.getAvailableAndActiveBookCatalogIds(bookCatalogs);
		assertNotNull(updateCatalogWithAvailability);
		assertEquals(0, updateCatalogWithAvailability.size());
	}

	private Book createTestBook() {
		String comments = "test insert";
		String provider = "chinnu";
		Book book = new Book(provider, true, true, comments);
		book.setBookCatalog(createTestBookCatalog());
		return bookJpaRepository.saveAndFlush(book);
	}

	private BookCatalog createTestBookCatalog() {
		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = new BookCatalog(bookName, author, isbn);
		return bookCatalogJpaRepository.saveAndFlush(bookCatalog);
	}
}
