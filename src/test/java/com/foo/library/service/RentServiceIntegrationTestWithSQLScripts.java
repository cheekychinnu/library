package com.foo.library.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import com.foo.library.model.Penalty;
import com.foo.library.model.PenaltyReason;
import com.foo.library.model.PenaltyStatus;
import com.foo.library.model.PenaltyType;
import com.foo.library.model.Rent;
import com.foo.library.model.ReturnResponse;

public class RentServiceIntegrationTestWithSQLScripts extends
		BaseIntegrationTest {

	@Autowired
	private RentService rentService;

	@Test
	@Sql("classpath:beforeRentServiceTest.sql")
	public void testRentsDueInWithRentOverdue() {
		List<Rent> rentsDueIn = rentService.getRentsDueIn(7);
		Long rentId = 100L;
		rentsDueIn = rentsDueIn.stream().filter(r -> r.getId().equals(rentId))
				.collect(Collectors.toList());
		assertFalse(rentsDueIn.isEmpty());
		assertTrue(rentsDueIn.get(0).getIsDueDatePassed());
		assertFalse(rentsDueIn.get(0).getIsClosed());
	}

	@Test
	@Sql("classpath:beforeRentServiceTest.sql")
	public void testReturnBookAfterDueDate() {
		Long rentId = 100L;
		Long bookId = 100L;
		ReturnResponse returnBookResponse = rentService.returnBook(rentId,
				bookId);
		assertNotNull(returnBookResponse);
		assertTrue(returnBookResponse.getIsDueDateMissed());

		Penalty penalty = returnBookResponse.getPenalty();
		assertNotNull(penalty);
		assertTrue(penalty.getStatus().equals(PenaltyStatus.PENDING));
		assertTrue(penalty.getType().equals(PenaltyType.PAYMENT));
		assertTrue(penalty.getReason().equals(PenaltyReason.MISSED_DUE_DATE));

		String userId = "vino";
		List<Penalty> pendingPenaltyForUser = rentService
				.getPendingPenaltyForUser(userId);
		pendingPenaltyForUser = pendingPenaltyForUser.stream()
				.filter(p -> p.getRentId().equals(rentId))
				.collect(Collectors.toList());
		assertFalse(pendingPenaltyForUser.isEmpty());

		rentService.markPenaltyAsPaid(rentId);

		pendingPenaltyForUser = rentService.getPendingPenaltyForUser(userId);
		pendingPenaltyForUser = pendingPenaltyForUser.stream()
				.filter(p -> p.getRentId().equals(rentId))
				.collect(Collectors.toList());
		assertTrue(pendingPenaltyForUser.isEmpty());
	}

}
