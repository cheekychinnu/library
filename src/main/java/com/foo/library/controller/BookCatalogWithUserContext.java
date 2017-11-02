package com.foo.library.controller;

import com.foo.library.model.BookCatalog;
import com.foo.library.model.RatingAndReview;
import com.foo.library.model.Rent;

public class BookCatalogWithUserContext {
	private Boolean isAlreadyRented = false;
	private RatingAndReview ratingAndReview;
	private Rent currentOpenRent;
	private BookCatalog bookCatalog;
	private Boolean isWatching = false;
	
	public BookCatalogWithUserContext(BookCatalog bookCatalog) {
		this.bookCatalog = bookCatalog;
	}
	
	public BookCatalog getBookCatalog() {
		return bookCatalog;
	}

	public Boolean getIsAlreadyRented() {
		return isAlreadyRented;
	}

	public void setIsAlreadyRented(Boolean isAlreadyRented) {
		this.isAlreadyRented = isAlreadyRented;
	}

	public RatingAndReview getRatingAndReview() {
		return ratingAndReview;
	}

	public void setRatingAndReview(RatingAndReview ratingAndReview) {
		this.ratingAndReview = ratingAndReview;
	}

	public Rent getCurrentOpenRent() {
		return currentOpenRent;
	}

	public void setCurrentOpenRent(Rent currentOpenRent) {
		this.currentOpenRent = currentOpenRent;
	}

	public Boolean getIsWatching() {
		return isWatching;
	}

	public void setIsWatching(Boolean isWatching) {
		this.isWatching = isWatching;
	}

	@Override
	public String toString() {
		return "BookCatalogWithUserContext [isAlreadyRented=" + isAlreadyRented
				+ ", ratingAndReview=" + ratingAndReview + ", currentOpenRent="
				+ currentOpenRent + ", bookCatalog=" + bookCatalog + ", isWatching="+isWatching+"]";
	}
	
}
