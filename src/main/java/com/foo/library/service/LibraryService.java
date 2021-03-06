package com.foo.library.service;

import java.util.List;
import java.util.Optional;

import com.foo.library.model.Book;
import com.foo.library.model.BookCatalog;
import com.foo.library.model.Penalty;
import com.foo.library.model.RatingAndReview;
import com.foo.library.model.Rent;
import com.foo.library.model.RentResponse;
import com.foo.library.model.ReturnResponse;
import com.foo.library.model.Subscriber;
import com.foo.library.model.Watcher;

public interface LibraryService {
	
	BookCatalog addBookCatalogToLibrary(BookCatalog bookCatalog);

	Book addBookToTheCatalog(Long bookCatalogId, Book book);
	
	List<BookCatalog> getAllBookCatalogs();
	
	List<BookCatalog> searchBookCatalogByIsbn(String isbn);
	
	List<BookCatalog> searchBookCatalogByAuthor(String author);
	
	List<BookCatalog> searchBookCatalogByBookName(String name);
	
	List<BookCatalog> getAllBookCatalogsWithRatingsAndAvailability();
	
	void subscribeForNewAdditions(String userId);
	
	List<Subscriber> getSubscribersForNewAdditions();
	
	void rateAndReview(Long bookCatalogId, String userId,Integer rating, String review);
	
	void insertOrUpdateRating(Long bookCatalogId, String userId, Integer rating);
	
	void insertOrUpdateReview(Long bookCatalogId, String userId, String review);
	
	List<RatingAndReview> getRatingAndReviewsForBookCatalog(Long bookCatalogId);
	
	Optional<RatingAndReview> getRatingAndReviewForBookCatalogAndUser(
			Long bookCatalogId, String userId);
	
	List<RatingAndReview> getRatingAndReviewsForUser(String userId);
	
	RentResponse rentBook(String userId, Long bookCatalogId);
	
	void watchForBookCatalog(String userId, Long bookCatalogId);

	List<Watcher> getWatchers(Long bookCatalogId);
	
	// pending test cases :
	ReturnResponse returnBook(Long rentId, Long bookId);
	
	ReturnResponse returnBook(Long rentId);
	
	void markPenaltyAsPaid(Long rentId);
	
	void logMissingBook(Long rentId);
	
	void markPenaltyAsContributed(Long rentId, Long bookId);
	
	List<Penalty> getPendingPenaltyForUser(String userId);
	
	void markPenaltyAsSuspended(Long rentId);
	
	List<Rent> getAllRents(String userId);
	
	List<Rent> getOpenRents(String userId);
	
	List<Rent> getRentsDueIn(Integer noOfDays);

	List<Watcher> getWatchersForUserId(String userId);

	void deleteRating(Long bookCatalogId, String userId);
	
	void deleteRatingAndReview(Long bookCatalogId, String userId);
	
	void deleteReview(Long bookCatalogId, String userId);

	Boolean isBookCatalogExists(Long bookCatalogId);
	
}
