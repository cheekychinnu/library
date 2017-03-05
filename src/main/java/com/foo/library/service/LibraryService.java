package com.foo.library.service;

import java.util.List;

import com.foo.library.model.Book;
import com.foo.library.model.BookCatalog;

public interface LibraryService {
	
	BookCatalog addBookCatalogToLibrary(BookCatalog bookCatalog);

	Book addBookToTheCatalog(Long bookCatalogId, Book book);
	
	List<BookCatalog> findBookCatalogByIsbn(String isbn);
	
	List<BookCatalog> getAllBookCatalogsWithRatingsAndAvailability();
	
}
