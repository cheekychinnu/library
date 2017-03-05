package com.foo.library.repository;

import static org.junit.Assert.*;

import java.util.Date;

import org.assertj.core.util.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.foo.library.model.Book;
import com.foo.library.model.BookCatalog;
import com.foo.library.model.Penalty;
import com.foo.library.model.PenaltyReason;
import com.foo.library.model.PenaltyStatus;
import com.foo.library.model.PenaltyType;
import com.foo.library.model.Rent;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PenaltyJpaRepositoryTest {
	
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private PenaltyJpaRepository penaltyJpaRepository;
	
	@Autowired
	private BookJpaRepository bookJpaRepository;
	
	@Autowired
	private BookCatalogJpaRepository bookCatalogJpaRepository;
	
	@Autowired
	private RentJpaRepository rentJpaRepository;
	
	@Test
	public void testCreate()
	{
		Rent rent = createTestRent();
		Penalty penalty = new Penalty(rent.getId(), PenaltyReason.MISSED_DUE_DATE, PenaltyStatus.PENDING);
		penaltyJpaRepository.saveAndFlush(penalty);
		entityManager.clear();
		Penalty findOne = penaltyJpaRepository.findOne(rent.getId());
		assertNotNull(findOne);
		assertNotNull(findOne.getRent());
		assertEquals(rent, findOne.getRent());
		assertEquals(penalty, findOne);
	}
	
	@Test
	public void testUpdate()
	{
		Rent rent = createTestRent();
		Penalty penalty = new Penalty(rent.getId(), PenaltyReason.MISSED_DUE_DATE, PenaltyStatus.PENDING);
		penaltyJpaRepository.saveAndFlush(penalty);
		Book testBook = createTestBook();
		
		int updateContribution = penaltyJpaRepository.updateContribution(rent.getId(), testBook.getId());
		assertEquals(1, updateContribution);
		int updateStatus = penaltyJpaRepository.updateStatus(rent.getId(), PenaltyStatus.DONE);
		assertEquals(1, updateStatus);
		
		entityManager.clear();
		Penalty findOne = penaltyJpaRepository.findOne(rent.getId());
		assertNotNull(findOne);
		assertEquals(PenaltyStatus.DONE, findOne.getStatus());
		assertEquals(PenaltyReason.MISSED_DUE_DATE, findOne.getReason());
		assertEquals(testBook, findOne.getBook());
		
		Double amount = new Double(10);
		int updateAmount = penaltyJpaRepository.updateAmount(rent.getId(), amount);
		assertEquals(1, updateAmount);
		int updateType = penaltyJpaRepository.updateType(rent.getId(), PenaltyType.PAYMENT);
		assertEquals(1, updateType);
		
		entityManager.clear();
		
		findOne = penaltyJpaRepository.findOne(rent.getId());
		assertNotNull(findOne);
		assertEquals(amount,findOne.getAmount());
		assertEquals(PenaltyType.PAYMENT, findOne.getType());
	}
	
	private Rent createTestRent()
	{
		Date returnDate = DateUtil.parse("2017-03-04");
		Date issuedDate = DateUtil.parse("2017-02-04");
		String userId = "vino";
		Rent rent = new Rent(userId, issuedDate, returnDate);
		rent.setActualReturnDate(returnDate);
		Book book = createTestBook();
		rent.setBook(book);
		
		return rentJpaRepository.saveAndFlush(rent);	
	}
	
	private Book createTestBook()
	{
		String comments = "test insert";
		String provider = "chinnu";
		Book book = new Book(provider, true, false, comments);
		book.setBookCatalog(createTestBookCatalog());
		return bookJpaRepository.saveAndFlush(book);
	}
	
	private BookCatalog createTestBookCatalog() {
		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = new BookCatalog(bookName, author, isbn);
		return bookCatalogJpaRepository.saveAndFlush(bookCatalog);
	}
}
