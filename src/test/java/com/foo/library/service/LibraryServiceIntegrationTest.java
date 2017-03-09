package com.foo.library.service;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.foo.library.model.Book;
import com.foo.library.model.BookCatalog;
import com.foo.library.model.RatingAndReview;
import com.foo.library.model.RatingAndReviewPK;
import com.foo.library.model.RentResponse;
import com.foo.library.model.Subscriber;
import com.foo.library.repository.BookCatalogJpaRepository;
import com.foo.library.repository.RatingAndReviewJpaRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Rollback
public class LibraryServiceIntegrationTest {

	@Autowired
	private RatingAndReviewJpaRepository ratingAndReviewJpaRepository;

	@Autowired
	private LibraryService libraryService;

	@Autowired
	private BookCatalogJpaRepository bookCatalogJpaRepository;

	@Test
	public void testRatingAndReview()
	{
		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = constructBookCatalog(bookName, author, isbn);
		BookCatalog addedCatalog = libraryService.addBookCatalogToLibrary(bookCatalog);
		
		Long bookCatalogId = addedCatalog.getId();
		String userId1 = "chinnu";
		String review ="Good one";
		Integer rating = 4;
		
		libraryService.rateAndReview(bookCatalogId, userId1, rating, review);
		 
		String userId2 = "vino";
		libraryService.rateAndReview(bookCatalogId, userId2, 3, review);
		
		List<RatingAndReview> ratingsAndReviews = libraryService.getRatingAndReviewsForBookCatalog(bookCatalogId);
		assertNotNull(ratingsAndReviews);
		assertEquals(2, ratingsAndReviews.size());
		List<String> ratedUsers = ratingsAndReviews.stream().map(r->r.getId().getUserId()).collect(Collectors.toList());
		assertTrue(ratedUsers.contains(userId1));
		assertTrue(ratedUsers.contains(userId2));
		
		List<RatingAndReview> ratingAndReviewsForUser = libraryService.getRatingAndReviewsForUser(userId1);
		assertNotNull(ratingAndReviewsForUser);
		assertEquals(1, ratingAndReviewsForUser.size());
		assertEquals(userId1, ratingAndReviewsForUser.get(0).getId().getUserId());
		assertEquals(rating, ratingAndReviewsForUser.get(0).getRating());
		
		List<BookCatalog> catalogs = libraryService.getAllBookCatalogsWithRatingsAndAvailability();
		assertNotNull(catalogs);
		assertEquals(1, catalogs.size());
		assertEquals(new Double(3.5), catalogs.get(0).getAverageRating());
		
		libraryService.updateRating(bookCatalogId, userId2, 4);
		catalogs = libraryService.getAllBookCatalogsWithRatingsAndAvailability();
		assertNotNull(catalogs);
		assertEquals(1, catalogs.size());
		assertEquals(new Double(4), catalogs.get(0).getAverageRating());
		
		String updatedReview = "updated review";
		libraryService.updateReview(bookCatalogId, userId2, updatedReview);
		ratingAndReviewsForUser = libraryService.getRatingAndReviewsForUser(userId2);
		assertNotNull(ratingAndReviewsForUser);
		assertEquals(1, ratingAndReviewsForUser.size());
		assertEquals(userId2, ratingAndReviewsForUser.get(0).getId().getUserId());
		assertEquals(updatedReview,ratingAndReviewsForUser.get(0).getReview());
	}
	
	@Test
	public void testRentAvailableBook()
	{
		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = constructBookCatalog(bookName, author, isbn);
		BookCatalog addBookCatalogToLibrary = libraryService
				.addBookCatalogToLibrary(bookCatalog);

		String comments = "test insert";
		String provider = "chinnu";
		Book book = new Book(provider, true, true, comments);
		libraryService.addBookToTheCatalog(addBookCatalogToLibrary.getId(),
				book);
		
		String userId = "chinnu";
		Long bookId = book.getId();
		RentResponse response = libraryService.rentBook(userId, bookId);
		assertNotNull(response);
		assertTrue(response.getIsSuccess());
		assertNotNull(response.getRent());
		System.out.println(response.getRent().getDueDate());
	}

	@Test
	public void testRentUnavailableBook()
	{
		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = constructBookCatalog(bookName, author, isbn);
		BookCatalog addBookCatalogToLibrary = libraryService
				.addBookCatalogToLibrary(bookCatalog);

		String comments = "test insert";
		String provider = "chinnu";
		Book book = new Book(provider, true, false, comments);
		libraryService.addBookToTheCatalog(addBookCatalogToLibrary.getId(),
				book);
		
		String userId = "chinnu";
		Long bookId = book.getId();
		RentResponse response = libraryService.rentBook(userId, bookId);
		assertNotNull(response);
		assertFalse(response.getIsSuccess());
		assertNotNull(response.getMessage());
	}
	
	@Test
	public void testSubscribeForNewAdditions()
	{
		String userId = "chinnu";
		libraryService.subscribeForNewAdditions(userId);
		List<Subscriber> subscribersForNewAdditions = libraryService.getSubscribersForNewAdditions();
		assertNotNull(subscribersForNewAdditions);
		assertEquals(1, subscribersForNewAdditions.size());
		assertEquals(userId, subscribersForNewAdditions.get(0).getUserId());
		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = constructBookCatalog(bookName, author, isbn);
		libraryService.addBookCatalogToLibrary(bookCatalog);
	}
	
	@Test
	public void testGetAllBookCatalogsWithRatingsAndAvailabilityForNoBookAndNoRating() {
		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = constructBookCatalog(bookName, author, isbn);
		BookCatalog addBookCatalogToLibrary = libraryService
				.addBookCatalogToLibrary(bookCatalog);

		List<BookCatalog> catalogsWithRatingsAndAvailability = libraryService
				.getAllBookCatalogsWithRatingsAndAvailability();
		assertNotNull(catalogsWithRatingsAndAvailability);
		assertEquals(1, catalogsWithRatingsAndAvailability.size());
		assertEquals(addBookCatalogToLibrary.getId(),
				catalogsWithRatingsAndAvailability.get(0).getId());
		assertEquals(false, catalogsWithRatingsAndAvailability.get(0)
				.getIsAvailable());
		assertNull(catalogsWithRatingsAndAvailability.get(0).getAverageRating());
	}

	@Test
	public void testGetAllBookCatalogsWithRatingsAndAvailabilityForBookAndRating() {
		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = constructBookCatalog(bookName, author, isbn);
		BookCatalog addBookCatalogToLibrary = libraryService
				.addBookCatalogToLibrary(bookCatalog);

		String comments = "test insert";
		String provider = "chinnu";
		Book book = new Book(provider, true, true, comments);
		libraryService.addBookToTheCatalog(addBookCatalogToLibrary.getId(),
				book);
		provider = "vino";
		book = new Book(provider, true, false, comments);
		libraryService.addBookToTheCatalog(addBookCatalogToLibrary.getId(),
				book);

		Integer rating = 3;
		addRatingsForBookCatalog(addBookCatalogToLibrary.getId(), rating);

		isbn = "129847872";
		bookName = "Harry Potter And The Half Blood Prince";
		bookCatalog = constructBookCatalog(bookName, author, isbn);
		BookCatalog addBookCatalogToLibrary2 = libraryService
				.addBookCatalogToLibrary(bookCatalog);
		rating = 2;
		addRatingsForBookCatalog(addBookCatalogToLibrary2.getId(), rating);

		List<BookCatalog> catalogsWithRatingsAndAvailability = libraryService
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
		RatingAndReview ratingAndReview = new RatingAndReview();
		ratingAndReview.setRating(rating);
		ratingAndReview.setReview("test review");
		RatingAndReviewPK ratingAndReviewPK = new RatingAndReviewPK("vino", id);
		ratingAndReview.setId(ratingAndReviewPK);

		ratingAndReview = ratingAndReviewJpaRepository
				.saveAndFlush(ratingAndReview);
	}

	@Test
	public void testGetAllBookCatalogsWithRatingsAndAvailabilityForBookAndNoRating() {
		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = constructBookCatalog(bookName, author, isbn);
		BookCatalog addBookCatalogToLibrary = libraryService
				.addBookCatalogToLibrary(bookCatalog);

		String comments = "test insert";
		String provider = "chinnu";
		Book book = new Book(provider, true, true, comments);
		libraryService.addBookToTheCatalog(addBookCatalogToLibrary.getId(),
				book);
		provider = "vino";
		book = new Book(provider, true, false, comments);
		libraryService.addBookToTheCatalog(addBookCatalogToLibrary.getId(),
				book);

		List<BookCatalog> catalogsWithRatingsAndAvailability = libraryService
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
		BookCatalog addBookCatalogToLibrary = libraryService
				.addBookCatalogToLibrary(bookCatalog);

		isbn = "129847873";
		bookName = "Harry Potter And The Chamber Of Secrets";
		bookCatalog = constructBookCatalog(bookName, author, isbn);
		BookCatalog addBookCatalogToLibrary2 = libraryService
				.addBookCatalogToLibrary(bookCatalog);

		List<BookCatalog> searchBookCatalogByAuthor = libraryService
				.searchBookCatalogByAuthor(author);
		assertNotNull(searchBookCatalogByAuthor);
		assertEquals(2, searchBookCatalogByAuthor.size());
		assertTrue(searchBookCatalogByAuthor.contains(addBookCatalogToLibrary));
		assertTrue(searchBookCatalogByAuthor.contains(addBookCatalogToLibrary2));
	}

	@Test
	public void testSearchBookCatalogByBookName() {

		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = constructBookCatalog(bookName, author, isbn);
		libraryService.addBookCatalogToLibrary(bookCatalog);

		isbn = "129847873";
		bookName = "Harry Potter And The Chamber Of Secrets";
		bookCatalog = constructBookCatalog(bookName, author, isbn);
		BookCatalog addBookCatalogToLibrary2 = libraryService
				.addBookCatalogToLibrary(bookCatalog);

		List<BookCatalog> searchBookCatalogByBookName = libraryService
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
		libraryService.addBookCatalogToLibrary(bookCatalog);

		isbn = "129847873";
		bookName = "Harry Potter And The Chamber Of Secrets";
		bookCatalog = constructBookCatalog(bookName, author, isbn);
		BookCatalog addBookCatalogToLibrary2 = libraryService
				.addBookCatalogToLibrary(bookCatalog);

		List<BookCatalog> searchBookCatalogByAuthor = libraryService
				.searchBookCatalogByIsbn(isbn);
		assertNotNull(searchBookCatalogByAuthor);
		assertEquals(1, searchBookCatalogByAuthor.size());
		assertTrue(searchBookCatalogByAuthor.contains(addBookCatalogToLibrary2));
	}

	@Test
	public void testGetAllBookCatalogs() {
		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = constructBookCatalog(bookName, author, isbn);
		BookCatalog addBookCatalogToLibrary = libraryService
				.addBookCatalogToLibrary(bookCatalog);

		assertNotNull(addBookCatalogToLibrary.getId());
		String comments = "test insert";
		String provider = "chinnu";
		Book book = new Book(provider, true, true, comments);
		Book addBookToTheCatalog1 = libraryService.addBookToTheCatalog(
				addBookCatalogToLibrary.getId(), book);
		provider = "vino";
		book = new Book(provider, true, true, comments);
		Book addBookToTheCatalog2 = libraryService.addBookToTheCatalog(
				addBookCatalogToLibrary.getId(), book);

		List<BookCatalog> allBookCatalogs = libraryService.getAllBookCatalogs();
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

		BookCatalog addBookCatalogToLibrary = libraryService
				.addBookCatalogToLibrary(bookCatalog);
		assertNotNull(addBookCatalogToLibrary);
		assertNotNull(addBookCatalogToLibrary.getId());
		List<BookCatalog> findBookCatalogByIsbn = libraryService
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
		BookCatalog addBookCatalogToLibrary = libraryService
				.addBookCatalogToLibrary(bookCatalog);

		String comments = "test insert";
		String provider = "chinnu";
		Book book = new Book(provider, true, true, comments);

		Book addBookToTheCatalog = libraryService.addBookToTheCatalog(
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
