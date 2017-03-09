package com.foo.library.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.assertj.core.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.foo.library.model.Book;
import com.foo.library.model.BookCatalog;
import com.foo.library.model.EventType;
import com.foo.library.model.Penalty;
import com.foo.library.model.PenaltyReason;
import com.foo.library.model.PenaltyStatus;
import com.foo.library.model.PenaltyType;
import com.foo.library.model.RatingAndReview;
import com.foo.library.model.RatingAndReviewPK;
import com.foo.library.model.Rent;
import com.foo.library.model.RentResponse;
import com.foo.library.model.ReturnResponse;
import com.foo.library.model.Subscriber;
import com.foo.library.repository.BookCatalogJpaRepository;
import com.foo.library.repository.BookJpaRepository;
import com.foo.library.repository.PenaltyJpaRepository;
import com.foo.library.repository.RatingAndReviewJpaRepository;
import com.foo.library.repository.RentJpaRepository;
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
	private RentJpaRepository rentJpaRepository;

	@Autowired
	private PenaltyJpaRepository penaltyJpaRepository;

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
		ratingAndReviewJpaRepository.updateUserRatingForBook(userId,
				bookCatalogId, rating);
	}

	@Override
	public void updateReview(Long bookCatalogId, String userId, String review) {
		ratingAndReviewJpaRepository.updateUserReviewForBook(userId,
				bookCatalogId, review);
	}

	@Override
	public List<RatingAndReview> getRatingAndReviewsForBookCatalog(
			Long bookCatalogId) {
		return ratingAndReviewJpaRepository
				.findByIdBookCatalogId(bookCatalogId);
	}

	@Override
	public List<RatingAndReview> getRatingAndReviewsForUser(String userId) {
		return ratingAndReviewJpaRepository.findByIdUserId(userId);
	}

	@Override
	public RentResponse rentBook(String userId, Long bookId) {

		Date issuedDate = DateUtil.now();
		Date dueDate = computeDueDate(issuedDate);
		Rent rent = new Rent(userId, issuedDate, dueDate);

		RentResponse rentResponse;

		// since this is a singleton bean, "this" is sufficient monitor object
		synchronized (this) {

			Book book = bookJpaRepository.findOne(bookId);
			if (book.getIsAvailable()) {
				rent.setBook(book);
				rent = rentJpaRepository.saveAndFlush(rent);
				bookJpaRepository.updateIsAvailable(bookId, false);
				rentResponse = new RentResponse(rent);
			} else {
				rentResponse = new RentResponse();
				rentResponse
						.markAsFailed("The requested book : "
								+ book.getBookCatalog().getName()
								+ " is not available");
			}
		}

		return rentResponse;
	}

	private Date computeDueDate(Date issuedDate) {
		// it would be easy to produce last friday of the month logic from the
		// native library.
		// I will revisit this once I migrate this
		Calendar calendar = Calendar.getInstance();
		calendar.set(issuedDate.getYear(), issuedDate.getMonth(), 1);
		calendar.set(GregorianCalendar.DAY_OF_WEEK, Calendar.FRIDAY);
		calendar.set(GregorianCalendar.DAY_OF_WEEK_IN_MONTH, -1);
		return calendar.getTime();
	}

	@Override
	public ReturnResponse returnBook(Long rentId, Long bookId) {
		bookJpaRepository.updateIsAvailable(bookId, true);
		Date returnDate = DateUtil.now();
		rentJpaRepository.updateActualReturnDateAndMarkAsClosed(rentId,
				returnDate);
		Rent rent = rentJpaRepository.getOne(rentId);
		ReturnResponse returnResponse;
		boolean isDueDatePassed = isDueDatePassed(rent.getDueDate(), returnDate);
		if (isDueDatePassed) {
			Penalty penalty = enterPenaltyForMissingDueDate(rent);
			returnResponse = new ReturnResponse(penalty);
		} else {
			returnResponse = new ReturnResponse();
		}
		Long bookCatalogId = rent.getBook().getBookCatalog().getId();
		notificationService.notifyWatchers(bookCatalogId);
		return returnResponse;
	}

	private Penalty enterPenaltyForMissingDueDate(Rent rent) {
		Penalty penalty = new Penalty(rent.getId(),
				PenaltyReason.MISSED_DUE_DATE, PenaltyStatus.PENDING);
		penalty.setType(PenaltyType.PAYMENT);
		// Ideally this should be computed based on by how many days the due
		// date is missed
		Double amount = new Double(10);
		penalty.setAmount(amount);
		penalty = penaltyJpaRepository.saveAndFlush(penalty);
		return penalty;
	}

	private boolean isDueDatePassed(Date dueDate, Date returnDate) {
		return returnDate.after(dueDate);
	}

	@Override
	public void markPenaltyAsPaid(Long rentId) {
		penaltyJpaRepository.updateStatus(rentId, PenaltyStatus.DONE);
	}

	@Override
	public void logMissingBook(Long rentId) {
		Penalty penalty = new Penalty(rentId, PenaltyReason.LOST,
				PenaltyStatus.PENDING);
		penalty.setType(PenaltyType.CONTRIBUTION);
		penalty = penaltyJpaRepository.saveAndFlush(penalty);

		rentJpaRepository.markAsClosed(rentId);

		Rent rent = rentJpaRepository.findOne(rentId);
		bookJpaRepository.updateIsActiveAndIsAvailable(rent.getBook().getId(),
				false, false);
	}

	@Override
	public void markPenaltyAsContributed(Long rentId, Long bookId) {
		penaltyJpaRepository.updateContribution(rentId, bookId);
		penaltyJpaRepository.updateStatus(rentId, PenaltyStatus.DONE);
	}

	@Override
	public List<Penalty> getPendingPenaltyForUser(String userId) {
		return penaltyJpaRepository.findByRentUserIdAndStatus(userId,
				PenaltyStatus.PENDING);
	}

	@Override
	public void markPenaltyAsSuspended(Long rentId) {
		penaltyJpaRepository.updateStatus(rentId, PenaltyStatus.SUSPENDED);
	}

}
