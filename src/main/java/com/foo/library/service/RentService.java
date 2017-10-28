package com.foo.library.service;

import java.util.List;

import com.foo.library.model.Penalty;
import com.foo.library.model.Rent;
import com.foo.library.model.RentResponse;
import com.foo.library.model.ReturnResponse;

public interface RentService {
	
	RentResponse rentBook(String userId, Long bookCatalogId);
	
	ReturnResponse returnBook(Long rentId, Long bookId);
	
	void markPenaltyAsPaid(Long rentId);
	
	void logMissingBook(Long rentId);
	
	void markPenaltyAsContributed(Long rentId, Long bookId);
	
	List<Penalty> getPendingPenaltyForUser(String userId);
	
	void markPenaltyAsSuspended(Long rentId);
	
	List<Rent> getAllRents(String userId);
	
	List<Rent> getOpenRents(String userId);
	
	List<Rent> getRentsDueIn(Integer noOfDays);

	ReturnResponse returnBook(Long rentId);
}
