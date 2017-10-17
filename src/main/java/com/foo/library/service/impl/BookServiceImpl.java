package com.foo.library.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.foo.library.model.Book;
import com.foo.library.model.BookCatalog;
import com.foo.library.repository.BookCatalogJpaRepository;
import com.foo.library.repository.BookJpaRepository;
import com.foo.library.service.BookService;
import com.foo.library.service.NotificationService;

@Component
public class BookServiceImpl implements BookService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private BookCatalogJpaRepository bookCatalogJpaRepository;

	@Autowired
	private BookJpaRepository bookJpaRepository;

	@Autowired
	private NotificationService notificationService;
	
	@Override
	public BookCatalog addBookCatalogToLibrary(BookCatalog bookCatalog) {
		BookCatalog catalog = bookCatalogJpaRepository
				.saveAndFlush(bookCatalog);
		entityManager.refresh(bookCatalog);
		notificationService.notifySubscriberForNewAddition(catalog);
		return catalog;
	}

	@Override
	public Book addBookToTheCatalog(Long bookCatalogId, Book book) {
		Optional<BookCatalog> bookCatalog = bookCatalogJpaRepository.findById(bookCatalogId);
		if(!bookCatalog.isPresent()){
			throw new IllegalStateException("No Book catalog present for id : "+bookCatalogId);
		}
		book.setBookCatalog(bookCatalog.get());
		book = bookJpaRepository.saveAndFlush(book);
		entityManager.refresh(book); // refreshing book will refresh bookCatalog
		return book;
	}

	@Override
	public List<BookCatalog> searchBookCatalogByIsbn(String isbn) {
		return bookCatalogJpaRepository.findByIsbn(isbn);
	}

	@Override
	public List<BookCatalog> getAllBookCatalogs() {
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

}
