package com.foo.library.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.assertj.core.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.foo.library.model.Book;
import com.foo.library.model.Penalty;
import com.foo.library.model.PenaltyReason;
import com.foo.library.model.PenaltyStatus;
import com.foo.library.model.PenaltyType;
import com.foo.library.model.Rent;
import com.foo.library.model.RentResponse;
import com.foo.library.model.ReturnResponse;
import com.foo.library.repository.BookJpaRepository;
import com.foo.library.repository.PenaltyJpaRepository;
import com.foo.library.repository.RentJpaRepository;
import com.foo.library.service.NotificationService;
import com.foo.library.service.RentService;
import com.foo.library.util.Util;

@Component
public class RentServiceImpl implements RentService {

	@Autowired
	private BookJpaRepository bookJpaRepository;

	@Autowired
	private RentJpaRepository rentJpaRepository;

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private PenaltyJpaRepository penaltyJpaRepository;

	@Autowired
	private NotificationService notificationService;

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
				entityManager.refresh(rent); // refreshing rent -> book -> bookCatalog
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
		calendar.setTime(issuedDate);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		calendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, -1);
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
		boolean isDueDatePassed = Util.isDueDatePassed(rent.getDueDate(),
				returnDate);
		Penalty penalty = null;
		if (isDueDatePassed) {
			penalty = enterPenaltyForMissingDueDate(rent);
			returnResponse = new ReturnResponse(penalty);
		} else {
			returnResponse = new ReturnResponse();
		}
		Long bookCatalogId = rent.getBook().getBookCatalog().getId();
		notificationService.notifyWatchers(bookCatalogId);
		
		if(penalty != null){
			entityManager.refresh(penalty); // this will refresh penalty -> rent -> book -> bookCatalog
		}
		else{
			entityManager.refresh(rent); // this will refresh rent -> book -> bookCatalog
		}
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

	@Override
	public void markPenaltyAsPaid(Long rentId) {
		penaltyJpaRepository.updateStatus(rentId, PenaltyStatus.DONE);
		Penalty penalty = penaltyJpaRepository.findOne(rentId);
		entityManager.refresh(penalty);
	}

	@Override
	public void logMissingBook(Long rentId) {
		Rent rent = rentJpaRepository.findOne(rentId);
		Long bookId = rent.getBook().getId();
		
		Penalty penalty = new Penalty(rentId, PenaltyReason.LOST,
				PenaltyStatus.PENDING);
		penalty.setType(PenaltyType.CONTRIBUTION);
		penalty = penaltyJpaRepository.saveAndFlush(penalty);

		bookJpaRepository.updateIsActiveAndIsAvailable(bookId,
				false, false);
		
		rentJpaRepository.markAsClosed(rentId);
		entityManager.refresh(rent);
		
		// I guess here it is important to refresh rent and then the penalty. why? 
		entityManager.refresh(penalty); // refreshing penalty -> rent -> book -> bookCatalog
	}

	@Override
	public void markPenaltyAsContributed(Long rentId, Long bookId) {
		penaltyJpaRepository.updateContribution(rentId, bookId);
		penaltyJpaRepository.updateStatus(rentId, PenaltyStatus.DONE);
		Penalty penalty = penaltyJpaRepository.findOne(rentId);
		entityManager.refresh(penalty);
	}

	@Override
	public List<Penalty> getPendingPenaltyForUser(String userId) {
		return penaltyJpaRepository.findByRentUserIdAndStatus(userId,
				PenaltyStatus.PENDING);
	}

	@Override
	public void markPenaltyAsSuspended(Long rentId) {
		penaltyJpaRepository.updateStatus(rentId, PenaltyStatus.SUSPENDED);
		Penalty penalty = penaltyJpaRepository.findOne(rentId);
		entityManager.refresh(penalty);
	}

	@Override
	public List<Rent> getOpenRents(String userId) {
		List<Rent> openRents = rentJpaRepository
				.findByUserIdAndIsClosedFalse(userId);
		fillIsDueDatePassed(openRents);
		return openRents;
	}

	private void fillIsDueDatePassed(List<Rent> rents) {
		Date currentDate = DateUtil.now();
		for (Rent r : rents) {
			Date dueDate = r.getDueDate();
			if (dueDate == null) {
				continue;
			}
			boolean dueDatePassed = Util.isDueDatePassed(dueDate, currentDate);
			r.setDueDatePassed(dueDatePassed);
		}
	}

	@Override
	public List<Rent> getAllRents(String userId) {
		List<Rent> rents = rentJpaRepository.findByUserId(userId);
		fillIsDueDatePassed(rents);
		return rents;
	}

	@Override
	public List<Rent> getRentsDueIn(Integer noOfDays) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, noOfDays);
		List<Rent> rents = rentJpaRepository
				.findByIsClosedFalseAndDueDateBefore(c.getTime());
		fillIsDueDatePassed(rents);
		return rents;
	}
}
