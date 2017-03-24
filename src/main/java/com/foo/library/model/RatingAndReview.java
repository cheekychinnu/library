package com.foo.library.model;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class RatingAndReview {

	@EmbeddedId
	private RatingAndReviewPK id;
	
	@OneToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name="book_catalog_id", referencedColumnName="id", insertable=false, updatable=false)
	private BookCatalog bookCatalog; 
	
	private Integer rating;
	private String review;

	public RatingAndReview() {
		super();
	}


	public BookCatalog getBookCatalog() {
		return bookCatalog;
	}

	public void setBookCatalog(BookCatalog bookCatalog) {
		this.bookCatalog = bookCatalog;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}


	public RatingAndReviewPK getId() {
		return id;
	}


	public void setId(RatingAndReviewPK id) {
		this.id = id;
	}
	
}
