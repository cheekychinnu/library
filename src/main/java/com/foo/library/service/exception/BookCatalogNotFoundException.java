package com.foo.library.service.exception;

public class BookCatalogNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -8007816787759501865L;

	public BookCatalogNotFoundException(Long bookCatalogId) {
		super("BookCatalogId :" + bookCatalogId + " is not found");
		this.bookCatalogId = bookCatalogId;
	}

	private Long bookCatalogId;

	public Long getBookCatalogId() {
		return bookCatalogId;
	}

	public void setBookCatalogId(Long bookCatalogId) {
		this.bookCatalogId = bookCatalogId;
	}

}
