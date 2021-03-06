package com.foo.library.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.foo.library.model.Book;
import com.foo.library.model.BookCatalog;
import com.foo.library.model.Penalty;
import com.foo.library.model.RatingAndReview;
import com.foo.library.model.Rent;
import com.foo.library.model.RentResponse;
import com.foo.library.model.ReturnResponse;
import com.foo.library.model.Subscriber;
import com.foo.library.model.Watcher;
import com.foo.library.service.BookService;
import com.foo.library.service.LibraryService;
import com.foo.library.service.RatingAndReviewService;
import com.foo.library.service.RentService;
import com.foo.library.service.SubscriptionService;
import com.foo.library.service.exception.BookCatalogNotFoundException;

@Component
public class LibraryServiceImpl implements LibraryService {

	@Autowired
	private BookService bookService;

	@Autowired
	private RentService rentService;

	@Autowired
	private SubscriptionService subscriptionService;

	@Autowired
	private RatingAndReviewService ratingAndReviewService;

	@Override
	public BookCatalog addBookCatalogToLibrary(BookCatalog bookCatalog) {
		return bookService.addBookCatalogToLibrary(bookCatalog);
	}

	@Override
	public Book addBookToTheCatalog(Long bookCatalogId, Book book) {
		return bookService.addBookToTheCatalog(bookCatalogId, book);
	}

	@Override
	public List<BookCatalog> getAllBookCatalogs() {
		return bookService.getAllBookCatalogs();
	}

	@Override
	public List<BookCatalog> searchBookCatalogByIsbn(String isbn) {
		return bookService.searchBookCatalogByIsbn(isbn);
	}

	@Override
	public List<BookCatalog> searchBookCatalogByAuthor(String author) {
		return bookService.searchBookCatalogByAuthor(author);
	}

	@Override
	public List<BookCatalog> searchBookCatalogByBookName(String name) {
		return bookService.searchBookCatalogByBookName(name);
	}

	@Override
	public List<BookCatalog> getAllBookCatalogsWithRatingsAndAvailability() {
		return bookService.getAllBookCatalogsWithRatingsAndAvailability();
	}

	@Override
	public void rateAndReview(Long bookCatalogId, String userId,
			Integer rating, String review) {
		if(!isBookCatalogExists(bookCatalogId)) {
			throw new BookCatalogNotFoundException(bookCatalogId);
		}
		ratingAndReviewService.rateAndReview(bookCatalogId, userId, rating,
				review);
	}

	@Override
	public List<RatingAndReview> getRatingAndReviewsForBookCatalog(
			Long bookCatalogId) {
		return ratingAndReviewService
				.getRatingAndReviewsForBookCatalog(bookCatalogId);
	}

	@Override
	public List<RatingAndReview> getRatingAndReviewsForUser(String userId) {
		return ratingAndReviewService.getRatingAndReviewsForUser(userId);
	}

	@Override
	public void subscribeForNewAdditions(String userId) {
		subscriptionService.subscribeForNewAdditions(userId);
	}

	@Override
	public List<Subscriber> getSubscribersForNewAdditions() {
		return subscriptionService.getSubscribersForNewAdditions();
	}

	@Override
	public RentResponse rentBook(String userId, Long bookCatalogId) {
		RentResponse response = rentService.rentBook(userId, bookCatalogId);
		if (response.getIsSuccess()) {
			boolean isWatching = subscriptionService.getWatchers(bookCatalogId)
					.stream()
					.anyMatch(w -> w.getId().getUserId().equals(userId));
			if (isWatching) {
				subscriptionService
						.unwatchForBookCatalog(userId, bookCatalogId);
			}
		}
		return response;
	}

	@Override
	public ReturnResponse returnBook(Long rentId, Long bookId) {
		return rentService.returnBook(rentId, bookId);
	}

	@Override
	public void markPenaltyAsPaid(Long rentId) {
		rentService.markPenaltyAsPaid(rentId);
	}

	@Override
	public void logMissingBook(Long rentId) {
		rentService.logMissingBook(rentId);
	}

	@Override
	public void markPenaltyAsContributed(Long rentId, Long bookId) {
		rentService.markPenaltyAsContributed(rentId, bookId);
	}

	@Override
	public List<Penalty> getPendingPenaltyForUser(String userId) {
		return rentService.getPendingPenaltyForUser(userId);
	}

	@Override
	public void markPenaltyAsSuspended(Long rentId) {
		rentService.markPenaltyAsSuspended(rentId);
	}

	@Override
	public List<Rent> getAllRents(String userId) {
		return rentService.getAllRents(userId);
	}

	@Override
	public List<Rent> getOpenRents(String userId) {
		return rentService.getOpenRents(userId);
	}

	@Override
	public List<Rent> getRentsDueIn(Integer noOfDays) {
		return rentService.getRentsDueIn(noOfDays);
	}

	@Override
	public void watchForBookCatalog(String userId, Long bookCatalogId) {
		subscriptionService.watchForBookCatalog(userId, bookCatalogId);
	}

	@Override
	public List<Watcher> getWatchersForUserId(String userId) {
		return subscriptionService.getWatchers(userId);
	}

	@Override
	public List<Watcher> getWatchers(Long bookCatalogId) {
		return subscriptionService.getWatchers(bookCatalogId);
	}

	@Override
	public ReturnResponse returnBook(Long rentId) {
		return rentService.returnBook(rentId);
	}

	@Override
	public void insertOrUpdateRating(Long bookCatalogId, String userId,
			Integer rating) {
		if(!isBookCatalogExists(bookCatalogId)) {
			throw new BookCatalogNotFoundException(bookCatalogId);
		}
		ratingAndReviewService.insertOrUpdateRating(bookCatalogId, userId, rating);
	}

	@Override
	public void insertOrUpdateReview(Long bookCatalogId, String userId,
			String review) {
		if(!isBookCatalogExists(bookCatalogId)) {
			throw new BookCatalogNotFoundException(bookCatalogId);
		}
		ratingAndReviewService.insertOrUpdateReview(bookCatalogId, userId, review);
	}

	@Override
	public Optional<RatingAndReview> getRatingAndReviewForBookCatalogAndUser(
			Long bookCatalogId, String userId) {
		return ratingAndReviewService.getRatingAndReviewForBookCatalogAndUser(bookCatalogId, userId);
		
	}

	@Override
	public void deleteRating(Long bookCatalogId, String userId) {
		if(!isBookCatalogExists(bookCatalogId)) {
			throw new BookCatalogNotFoundException(bookCatalogId);
		}
		ratingAndReviewService.deleteRating(bookCatalogId, userId);
	}

	@Override
	public void deleteRatingAndReview(Long bookCatalogId, String userId) {
		if(!isBookCatalogExists(bookCatalogId)) {
			throw new BookCatalogNotFoundException(bookCatalogId);
		}
		ratingAndReviewService.deleteRatingAndReview(bookCatalogId, userId);
	}

	@Override
	public void deleteReview(Long bookCatalogId, String userId) {
		if(!isBookCatalogExists(bookCatalogId)) {
			throw new BookCatalogNotFoundException(bookCatalogId);
		}
		ratingAndReviewService.deleteReview(bookCatalogId, userId);
	}

	@Override
	public Boolean isBookCatalogExists(Long bookCatalogId) {
		return bookService.isBookCatalogExists(bookCatalogId);
	}

}
