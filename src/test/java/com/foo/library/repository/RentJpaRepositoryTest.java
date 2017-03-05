package com.foo.library.repository;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.assertj.core.util.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.foo.library.model.Book;
import com.foo.library.model.BookCatalog;
import com.foo.library.model.Rent;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RentJpaRepositoryTest {
	
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private BookJpaRepository bookJpaRepository;
	
	@Autowired
	private BookCatalogJpaRepository bookCatalogJpaRepository;
	

	@Autowired
	private RentJpaRepository rentJpaRepository;
	
	@Test
	public void testCreate()
	{
		Date returnDate = DateUtil.parse("2017-03-04");
		Date issuedDate = DateUtil.parse("2017-02-04");
		String userId = "vino";
		Rent rent = new Rent(userId, issuedDate, returnDate);
		
		Book book = createTestBook();
		rent.setBook(book);
		
		rent = rentJpaRepository.saveAndFlush(rent);
		assertNotNull(rent);
		assertNotNull(rent.getId());
	}

	@Test
	public void testCreateMultipleRentForSameBook()
	{
		Date returnDate = DateUtil.parse("2017-03-04");
		Date issuedDate = DateUtil.parse("2017-02-04");
		String userId = "vino";
		Rent rent = new Rent(userId, issuedDate, returnDate);
		
		Book book = createTestBook();
		rent.setBook(book);
		
		rent = rentJpaRepository.saveAndFlush(rent);
		
		returnDate = DateUtil.parse("2017-04-04");
		issuedDate = DateUtil.parse("2017-03-04");
		Rent rent2 = new Rent(userId, issuedDate, returnDate);
		rent2.setBook(book);
		
		rent2 = rentJpaRepository.saveAndFlush(rent2);
		
		assertNotNull(rent2);
		assertNotNull(rent2.getId());
		assertNotEquals(rent.getId(), rent2.getId());
	}
	
	@Test
	public void testUpdateActualReturnDate()
	{
		Date returnDate = DateUtil.parse("2017-03-04");
		Date issuedDate = DateUtil.parse("2017-02-04");
		String userId = "vino";
		Rent rent = new Rent(userId, issuedDate, returnDate);
		
		Book book = createTestBook();
		rent.setBook(book);
		
		rent = rentJpaRepository.saveAndFlush(rent);
		
		Date actualReturnDate = DateUtil.parse("2017-03-07");;
		int updatedRecords = rentJpaRepository.updateActualReturnDate(rent.getId(), actualReturnDate);
		assertEquals(1, updatedRecords);
		entityManager.clear();
		rent = rentJpaRepository.getOne(rent.getId());
		assertNotNull(rent);
		assertEquals(actualReturnDate,rent.getActualReturnDate());
	}
	
	@Test
	public void testFindByUserId()
	{
		Date returnDate = DateUtil.parse("2017-03-04");
		Date issuedDate = DateUtil.parse("2017-02-04");
		String userId = "vino";
		Rent rent = new Rent(userId, issuedDate, returnDate);
		
		Book book = createTestBook();
		rent.setBook(book);
		
		rent = rentJpaRepository.saveAndFlush(rent);
		List<Rent> findByUserId = rentJpaRepository.findByUserId(userId);
		assertNotNull(findByUserId);
		assertEquals(1, findByUserId.size());
		assertEquals(rent, findByUserId.get(0));
	}
	
	@Test
	public void testFindByUserIdAndNullActualReturnDate()
	{
		Date returnDate = DateUtil.parse("2017-03-04");
		Date issuedDate = DateUtil.parse("2017-02-04");
		String userId = "vino";
		Rent rent = new Rent(userId, issuedDate, returnDate);
		rent.setActualReturnDate(returnDate);
		Book book = createTestBook();
		rent.setBook(book);
		
		rent = rentJpaRepository.saveAndFlush(rent);
		List<Rent> findByUserIdAndActualReturnDateIsNull = rentJpaRepository.findByUserIdAndActualReturnDateIsNull(userId);
		assertNotNull(findByUserIdAndActualReturnDateIsNull);
		assertTrue(findByUserIdAndActualReturnDateIsNull.isEmpty());
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
