package com.foo.library.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.foo.library.model.Book;
import com.foo.library.model.BookCatalog;
import com.foo.library.model.EventType;
import com.foo.library.model.RatingAndReview;
import com.foo.library.model.RatingAndReviewPK;
import com.foo.library.model.Subscriber;
import com.foo.library.repository.BookCatalogJpaRepository;
import com.foo.library.repository.BookJpaRepository;
import com.foo.library.repository.RatingAndReviewJpaRepository;
import com.foo.library.repository.SubscriberJpaRepository;
import com.foo.library.service.LibraryService;
import com.foo.library.service.NotificationService;

@Component
public class LibraryServiceImpl implements LibraryService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private BookCatalogJpaRepository bookCatalogJpaRepository;

	@Autowired
	private BookJpaRepository bookJpaRepository;

	@Autowired
	private RatingAndReviewJpaRepository ratingAndReviewJpaRepository;

	@Autowired
	private SubscriberJpaRepository subscriberJpaRepository;

	@Autowired
	private NotificationService notificationService;

	@Override
	public BookCatalog addBookCatalogToLibrary(BookCatalog bookCatalog) {
		BookCatalog catalog = bookCatalogJpaRepository
				.saveAndFlush(bookCatalog);
		notificationService.notifySubscriberForNewAddition(catalog);
		return catalog;
	}

	@Override
	public Book addBookToTheCatalog(Long bookCatalogId, Book book) {
		BookCatalog bookCatalog = new BookCatalog();
		bookCatalog.setId(bookCatalogId);
		book.setBookCatalog(bookCatalog);
		return bookJpaRepository.saveAndFlush(book);
	}

	@Override
	public List<BookCatalog> searchBookCatalogByIsbn(String isbn) {
		return bookCatalogJpaRepository.findByIsbn(isbn);
	}

	@Override
	public List<BookCatalog> getAllBookCatalogs() {
		entityManager.clear();
		return bookCatalogJpaRepository.findAll();
	}

	@Override
	public List<BookCatalog> searchBookCatalogByAuthor(String author) {
		return bookCatalogJpaRepository
				.findByAuthorContainingIgnoreCase(author);
	}

	@Override
	public List<BookCatalog> searchBookCatalogByBookName(String name) {
		return bookCatalogJpaRepository.findByNameContainingIgnoreCase(name);
	}

	@Override
	public List<BookCatalog> getAllBookCatalogsWithRatingsAndAvailability() {
		entityManager.clear();

		List<BookCatalog> allCatalogs = bookCatalogJpaRepository.findAll();

		List<BookCatalog> catalogsWithAverageRating = bookCatalogJpaRepository
				.fillCatalogWithAverageRatingIfRatingsArePresent(allCatalogs);

		Set<BookCatalog> uniqueCatalogs = new HashSet<>();
		uniqueCatalogs.addAll(catalogsWithAverageRating);
		uniqueCatalogs.addAll(allCatalogs);

		List<Long> bookCatalogIds = allCatalogs.stream()
				.map(catalog -> catalog.getId()).collect(Collectors.toList());

		List<Long> availableCatalogIds = bookCatalogJpaRepository
				.getAvailableAndActiveBookCatalogIds(bookCatalogIds);
		uniqueCatalogs.stream().forEach(c -> c.setIsAvailable(false));
		uniqueCatalogs.stream()
				.filter(c -> availableCatalogIds.contains(c.getId()))
				.forEach(c -> c.setIsAvailable(true));

		return new ArrayList<>(uniqueCatalogs);
	}

	@Override
	public void subscribeForNewAdditions(String userId) {
		Subscriber subscriber = new Subscriber(userId,
				EventType.NEW_BOOK_CATALOG);
		subscriberJpaRepository.saveAndFlush(subscriber);
	}

	@Override
	public List<Subscriber> getSubscribersForNewAdditions() {
		return subscriberJpaRepository
				.findByEventType(EventType.NEW_BOOK_CATALOG);
	}

	@Override
	public void rateAndReview(Long bookCatalogId, String userId,
			Integer rating, String review) {

		RatingAndReviewPK ratingAndReviewPK = new RatingAndReviewPK(userId,
				bookCatalogId);
		
		RatingAndReview ratingAndReview = new RatingAndReview();
		ratingAndReview.setRating(rating);
		ratingAndReview.setReview(review);
		ratingAndReview.setId(ratingAndReviewPK);
		
		ratingAndReviewJpaRepository.saveAndFlush(ratingAndReview);
	}

	@Override
	public void updateRating(Long bookCatalogId, String userId, Integer rating) {
		ratingAndReviewJpaRepository.updateUserRatingForBook(userId, bookCatalogId, rating);
	}

	@Override
	public void updateReview(Long bookCatalogId, String userId, String review) {
		ratingAndReviewJpaRepository.updateUserReviewForBook(userId, bookCatalogId, review);
	}

	@Override
	public List<RatingAndReview> getRatingAndReviewsForBookCatalog(
			Long bookCatalogId) {
		return ratingAndReviewJpaRepository.findByIdBookCatalogId(bookCatalogId);
	}

	@Override
	public List<RatingAndReview> getRatingAndReviewsForUser(String userId) {
		return ratingAndReviewJpaRepository.findByIdUserId(userId);
	}

}
