package com.foo.library.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.foo.library.model.Book;
import com.foo.library.model.BookCatalog;
import com.foo.library.model.Penalty;
import com.foo.library.model.PenaltyReason;
import com.foo.library.model.PenaltyType;
import com.foo.library.model.Rent;
import com.foo.library.model.RentResponse;
import com.foo.library.model.ReturnResponse;

public class RentServiceIntegrationTest extends BaseIntegrationTest {

	@Autowired
	private RentService rentService;

	@Autowired
	private BookService bookService;

	@Test
	public void testMarkPenaltyAsContributed() {
		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = constructBookCatalog(bookName, author, isbn);
		BookCatalog addBookCatalogToLibrary = bookService
				.addBookCatalogToLibrary(bookCatalog);

		String comments = "test insert";
		String provider = "chinnu";
		Book book = new Book(provider, true, true, comments);
		book = bookService.addBookToTheCatalog(addBookCatalogToLibrary.getId(),
				book);

		String userId = "chinnu";
		Long bookId = book.getId();
		RentResponse response = rentService.rentBook(userId, bookId);
		assertTrue(response.getIsSuccess());

		Long rentId = response.getRent().getId();

		rentService.logMissingBook(rentId);

		assertBookCatalogAvailability(addBookCatalogToLibrary.getId(), false);

		bookName = "Harry Potter And The Half Blood Prince";
		isbn = "12356677";
		bookCatalog = constructBookCatalog(bookName, author, isbn);
		BookCatalog newBookCatalogToLibrary = bookService
				.addBookCatalogToLibrary(bookCatalog);

		book = new Book(provider, true, true, comments);
		book = bookService.addBookToTheCatalog(newBookCatalogToLibrary.getId(),
				book);
		rentService.markPenaltyAsContributed(rentId, book.getId());

		assertBookCatalogAvailability(newBookCatalogToLibrary.getId(), true);

		List<Penalty> pendingPenaltyForUser = rentService
				.getPendingPenaltyForUser(userId);
		pendingPenaltyForUser = pendingPenaltyForUser.stream()
				.filter(p -> p.getRentId().equals(rentId))
				.collect(Collectors.toList());
		assertEquals(0, pendingPenaltyForUser.size());
	}

	@Test
	public void testLogAMissingBook() {
		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = constructBookCatalog(bookName, author, isbn);
		BookCatalog addBookCatalogToLibrary = bookService
				.addBookCatalogToLibrary(bookCatalog);

		String comments = "test insert";
		String provider = "chinnu";
		Book book = new Book(provider, true, true, comments);
		book = bookService.addBookToTheCatalog(addBookCatalogToLibrary.getId(),
				book);

		String userId = "chinnu";
		Long bookId = book.getId();
		RentResponse response = rentService.rentBook(userId, bookId);
		assertTrue(response.getIsSuccess());

		Long rentId = response.getRent().getId();

		rentService.logMissingBook(rentId);
		List<Rent> allRents = rentService.getAllRents(userId);
		allRents = allRents.stream().filter(r -> r.getId().equals(rentId))
				.collect(Collectors.toList());
		assertFalse(allRents.isEmpty());
		assertTrue(allRents.get(0).getIsClosed());

		assertBookCatalogAvailability(addBookCatalogToLibrary.getId(), false);

		List<Penalty> pendingPenaltyForUser = rentService
				.getPendingPenaltyForUser(userId);
		pendingPenaltyForUser = pendingPenaltyForUser.stream()
				.filter(p -> p.getRentId().equals(rentId))
				.collect(Collectors.toList());
		assertEquals(1, pendingPenaltyForUser.size());
		assertTrue(pendingPenaltyForUser.get(0).getReason()
				.equals(PenaltyReason.LOST));
		assertTrue(pendingPenaltyForUser.get(0).getType()
				.equals(PenaltyType.CONTRIBUTION));

		rentService.markPenaltyAsSuspended(rentId);
		pendingPenaltyForUser = rentService.getPendingPenaltyForUser(userId);
		pendingPenaltyForUser = pendingPenaltyForUser.stream()
				.filter(p -> p.getRentId().equals(rentId))
				.collect(Collectors.toList());
		assertEquals(0, pendingPenaltyForUser.size());
	}

	@Test
	public void testGetAllRents() {
		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = constructBookCatalog(bookName, author, isbn);
		BookCatalog addBookCatalogToLibrary = bookService
				.addBookCatalogToLibrary(bookCatalog);

		String comments = "test insert";
		String provider = "chinnu";
		Book book1 = new Book(provider, true, true, comments);
		book1 = bookService.addBookToTheCatalog(
				addBookCatalogToLibrary.getId(), book1);

		provider = "vino";
		Book book2 = new Book(provider, true, true, comments);
		book2 = bookService.addBookToTheCatalog(
				addBookCatalogToLibrary.getId(), book2);

		String userId = "chinnu";
		Long bookId1 = book1.getId();
		RentResponse response = rentService.rentBook(userId, bookId1);
		assertTrue(response.getIsSuccess());
		Long rentId1 = response.getRent().getId();

		assertBookCatalogAvailability(addBookCatalogToLibrary.getId(), true);

		Long bookId2 = book2.getId();
		response = rentService.rentBook(userId, bookId2);
		assertTrue(response.getIsSuccess());
		Long rentId2 = response.getRent().getId();

		assertBookCatalogAvailability(addBookCatalogToLibrary.getId(), false);

		ReturnResponse returnBook = rentService.returnBook(rentId1, bookId1);
		assertFalse(returnBook.getIsDueDateMissed());

		assertBookCatalogAvailability(addBookCatalogToLibrary.getId(), true);

		List<Rent> allRents = rentService.getAllRents(userId);
		assertNotNull(allRents);
		Map<Long, List<Rent>> rentIdToRents = allRents.stream().collect(
				Collectors.groupingBy(r -> r.getId()));
		List<Rent> rentForRentId1 = rentIdToRents.get(rentId1);
		assertFalse(rentForRentId1.isEmpty());
		assertTrue(rentForRentId1.get(0).getIsClosed());

		List<Rent> rentForRentId2 = rentIdToRents.get(rentId2);
		assertFalse(rentForRentId2.isEmpty());
		assertFalse(rentForRentId2.get(0).getIsClosed());
	}

	private void assertBookCatalogAvailability(Long id, boolean isAvailable) {
		List<BookCatalog> catalogs = bookService
				.getAllBookCatalogsWithRatingsAndAvailability();
		Map<Long, List<BookCatalog>> idToCatalogsMap = catalogs.stream()
				.collect(Collectors.groupingBy(c -> c.getId()));
		List<BookCatalog> catalogsForId = idToCatalogsMap.get(id);
		assertFalse(catalogsForId.isEmpty());
		assertEquals(isAvailable, catalogsForId.get(0).getIsAvailable());
	}

	@Test
	public void testGetOpenRents() {
		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = constructBookCatalog(bookName, author, isbn);
		BookCatalog addBookCatalogToLibrary = bookService
				.addBookCatalogToLibrary(bookCatalog);

		String comments = "test insert";
		String provider = "chinnu";
		Book book = new Book(provider, true, true, comments);
		book = bookService.addBookToTheCatalog(addBookCatalogToLibrary.getId(),
				book);

		String userId = "chinnu";
		Long bookId = book.getId();
		RentResponse response = rentService.rentBook(userId, bookId);
		assertTrue(response.getIsSuccess());

		Long rentId = response.getRent().getId();

		List<Rent> openRents = rentService.getOpenRents(userId);
		assertNotNull(openRents);
		openRents = openRents.stream().filter(r -> r.getId().equals(rentId))
				.collect(Collectors.toList());
		assertFalse(openRents.isEmpty());
		assertFalse(openRents.get(0).getIsClosed());
		assertFalse(openRents.get(0).isDueDatePassed());

		rentService.returnBook(rentId, bookId);
		openRents = rentService.getOpenRents(userId);
		assertNotNull(openRents);
		openRents = openRents.stream().filter(r -> r.getId().equals(rentId))
				.collect(Collectors.toList());
		assertTrue(openRents.isEmpty());
	}

	@Test
	public void testRentsDueIn() {
		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = constructBookCatalog(bookName, author, isbn);
		BookCatalog addBookCatalogToLibrary = bookService
				.addBookCatalogToLibrary(bookCatalog);

		String comments = "test insert";
		String provider = "chinnu";
		Book book = new Book(provider, true, true, comments);
		book = bookService.addBookToTheCatalog(addBookCatalogToLibrary.getId(),
				book);

		String userId = "chinnu";
		Long bookId = book.getId();
		RentResponse response = rentService.rentBook(userId, bookId);
		assertTrue(response.getIsSuccess());

		Long rentId = response.getRent().getId();

		List<Rent> rentsDueIn = rentService.getRentsDueIn(40);
		assertNotNull(rentsDueIn);
		rentsDueIn = rentsDueIn.stream().filter(r -> r.getId().equals(rentId))
				.collect(Collectors.toList());
		assertFalse(rentsDueIn.isEmpty());
		assertFalse(rentsDueIn.get(0).getIsClosed());
		assertFalse(rentsDueIn.get(0).isDueDatePassed());
	}

	@Test
	public void testReturnBookBeforeDueDate() {
		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = constructBookCatalog(bookName, author, isbn);
		BookCatalog addBookCatalogToLibrary = bookService
				.addBookCatalogToLibrary(bookCatalog);

		String comments = "test insert";
		String provider = "chinnu";
		Book book = new Book(provider, true, true, comments);
		book = bookService.addBookToTheCatalog(addBookCatalogToLibrary.getId(),
				book);

		String userId = "chinnu";
		Long bookId = book.getId();
		RentResponse response = rentService.rentBook(userId, bookId);
		assertTrue(response.getIsSuccess());

		Long rentId = response.getRent().getId();
		ReturnResponse returnBookResponse = rentService.returnBook(rentId,
				bookId);
		assertNotNull(returnBookResponse);
		assertFalse(returnBookResponse.getIsDueDateMissed());

		List<BookCatalog> allCatalogs = bookService
				.getAllBookCatalogsWithRatingsAndAvailability();
		assertNotNull(allCatalogs);
		allCatalogs = allCatalogs.stream()
				.filter(c -> c.getId().equals(addBookCatalogToLibrary.getId()))
				.collect(Collectors.toList());
		assertEquals(1, allCatalogs.size());
		assertTrue(allCatalogs.get(0).getIsAvailable());
		List<Book> books = allCatalogs.get(0).getBooks();
		assertEquals(book.getId(), books.get(0).getId());
		assertTrue(books.get(0).getIsAvailable());
	}

	@Test
	public void testRentAvailableBook() {
		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = constructBookCatalog(bookName, author, isbn);
		BookCatalog addBookCatalogToLibrary = bookService
				.addBookCatalogToLibrary(bookCatalog);

		String comments = "test insert";
		String provider = "chinnu";
		Book book = new Book(provider, true, true, comments);
		book = bookService.addBookToTheCatalog(addBookCatalogToLibrary.getId(),
				book);

		String userId = "chinnu";
		Long bookId = book.getId();
		RentResponse response = rentService.rentBook(userId, bookId);
		assertNotNull(response);
		assertTrue(response.getIsSuccess());
		assertNotNull(response.getRent());

		List<BookCatalog> allCatalogs = bookService
				.getAllBookCatalogsWithRatingsAndAvailability();
		assertNotNull(allCatalogs);
		allCatalogs = allCatalogs.stream()
				.filter(c -> c.getId().equals(addBookCatalogToLibrary.getId()))
				.collect(Collectors.toList());
		assertEquals(1, allCatalogs.size());
		assertFalse(allCatalogs.get(0).getIsAvailable());
		List<Book> books = allCatalogs.get(0).getBooks();
		assertEquals(book.getId(), books.get(0).getId());
		assertFalse(books.get(0).getIsAvailable());
	}

	@Test
	public void testRentUnavailableBook() {
		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = constructBookCatalog(bookName, author, isbn);
		BookCatalog addBookCatalogToLibrary = bookService
				.addBookCatalogToLibrary(bookCatalog);

		String comments = "test insert";
		String provider = "chinnu";
		Book book = new Book(provider, true, false, comments);
		book = bookService.addBookToTheCatalog(addBookCatalogToLibrary.getId(),
				book);

		String userId = "chinnu";
		Long bookId = book.getId();
		RentResponse response = rentService.rentBook(userId, bookId);
		assertNotNull(response);
		assertFalse(response.getIsSuccess());
		assertNotNull(response.getMessage());
	}

	private BookCatalog constructBookCatalog(String bookName, String author,
			String isbn) {
		BookCatalog bookCatalog = new BookCatalog(bookName, author, isbn);
		return bookCatalog;
	}

}
