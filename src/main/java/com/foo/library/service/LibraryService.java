package com.foo.library.service;

import java.util.List;

import com.foo.library.model.Book;
import com.foo.library.model.BookCatalog;
import com.foo.library.model.RatingAndReview;
import com.foo.library.model.Subscriber;

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
	
	void updateRating(Long bookCatalogId, String userId, Integer rating);
	
	void updateReview(Long bookCatalogId, String userId, String review);

	List<RatingAndReview> getRatingAndReviewsForBookCatalog(Long bookCatalogId);
	
	List<RatingAndReview> getRatingAndReviewsForUser(String userId);
	
}
