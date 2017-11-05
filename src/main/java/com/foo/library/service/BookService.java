package com.foo.library.service;

import java.util.List;

import com.foo.library.model.Book;
import com.foo.library.model.BookCatalog;

public interface BookService {

	BookCatalog addBookCatalogToLibrary(BookCatalog bookCatalog);

	Book addBookToTheCatalog(Long bookCatalogId, Book book);

	List<BookCatalog> getAllBookCatalogs();

	List<BookCatalog> searchBookCatalogByIsbn(String isbn);

	List<BookCatalog> searchBookCatalogByAuthor(String author);

	List<BookCatalog> searchBookCatalogByBookName(String name);

	List<BookCatalog> getAllBookCatalogsWithRatingsAndAvailability();
	
	boolean isBookCatalogExists(Long bookCatalogId);
}
