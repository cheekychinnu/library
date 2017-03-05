package com.foo.library.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.foo.library.model.Book;
import com.foo.library.model.BookCatalog;
import com.foo.library.repository.BookCatalogJpaRepository;
import com.foo.library.repository.BookJpaRepository;
import com.foo.library.service.LibraryService;

@Component
public class LibraryServiceImpl implements LibraryService {

	@Autowired
	private BookCatalogJpaRepository bookCatalogJpaRepository;

	@Autowired
	private BookJpaRepository bookJpaRepository;

	@Override
	public BookCatalog addBookCatalogToLibrary(BookCatalog bookCatalog) {
		return bookCatalogJpaRepository.saveAndFlush(bookCatalog);
	}

	@Override
	public Book addBookToTheCatalog(Long bookCatalogId, Book book) {
		BookCatalog bookCatalog = new BookCatalog();
		bookCatalog.setId(bookCatalogId);
		book.setBookCatalog(bookCatalog);
		return bookJpaRepository.saveAndFlush(book);
	}

	@Override
	public List<BookCatalog> findBookCatalogByIsbn(String isbn) {
		return bookCatalogJpaRepository.findByIsbn(isbn);
	}

	@Override
	public List<BookCatalog> getAllBookCatalogsWithRatingsAndAvailability() {
		// TODO Auto-generated method stub
		return null;
	}

}
