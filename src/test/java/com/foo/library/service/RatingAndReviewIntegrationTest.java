package com.foo.library.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.foo.library.model.BookCatalog;
import com.foo.library.model.RatingAndReview;

public class RatingAndReviewIntegrationTest extends BaseIntegrationTest {

	@Autowired
	private RatingAndReviewService libraryService;

	@Autowired
	private BookService bookService;

	@Test
	public void testRatingAndReview() {

		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = new BookCatalog(bookName, author, isbn);
		BookCatalog addedCatalog = bookService
				.addBookCatalogToLibrary(bookCatalog);

		Long bookCatalogId = addedCatalog.getId();
		String userId1 = "chinnu";
		String review = "Good one";
		Integer rating = 4;

		libraryService.rateAndReview(bookCatalogId, userId1, rating, review);

		String userId2 = "vino";
		libraryService.rateAndReview(bookCatalogId, userId2, 3, review);

		List<RatingAndReview> ratingsAndReviews = libraryService
				.getRatingAndReviewsForBookCatalog(bookCatalogId);
		
		assertNotNull(ratingsAndReviews);
		assertEquals(2, ratingsAndReviews.size());
		List<String> ratedUsers = ratingsAndReviews.stream()
				.map(r -> r.getId().getUserId()).collect(Collectors.toList());
		assertTrue(ratedUsers.contains(userId1));
		assertTrue(ratedUsers.contains(userId2));

		List<RatingAndReview> ratingAndReviewsForUser = libraryService
				.getRatingAndReviewsForUser(userId1);
		assertNotNull(ratingAndReviewsForUser);
		assertEquals(1, ratingAndReviewsForUser.size());
		assertEquals(userId1, ratingAndReviewsForUser.get(0).getId()
				.getUserId());
		assertEquals(rating, ratingAndReviewsForUser.get(0).getRating());
		assertEquals(review,ratingAndReviewsForUser.get(0).getReview());

		List<BookCatalog> catalogs = bookService
				.getAllBookCatalogsWithRatingsAndAvailability();
		assertNotNull(catalogs);
		assertEquals(1, catalogs.size());
		assertEquals(new Double(3.5), catalogs.get(0).getAverageRating());

		libraryService.updateRating(bookCatalogId, userId2, 4);
		catalogs = bookService.getAllBookCatalogsWithRatingsAndAvailability();
		assertNotNull(catalogs);
		assertEquals(1, catalogs.size());
		assertEquals(new Double(4), catalogs.get(0).getAverageRating());

		String updatedReview = "updated review";
		libraryService.updateReview(bookCatalogId, userId2, updatedReview);
		ratingAndReviewsForUser = libraryService
				.getRatingAndReviewsForUser(userId2);
		assertNotNull(ratingAndReviewsForUser);
		assertEquals(1, ratingAndReviewsForUser.size());
		assertEquals(userId2, ratingAndReviewsForUser.get(0).getId()
				.getUserId());
		assertEquals(new Integer(4), ratingAndReviewsForUser.get(0).getRating());
		assertEquals(updatedReview, ratingAndReviewsForUser.get(0).getReview());
	}

}
