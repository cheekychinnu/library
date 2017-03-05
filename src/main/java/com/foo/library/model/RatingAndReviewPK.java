package com.foo.library.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class RatingAndReviewPK implements Serializable {

	private static final long serialVersionUID = -6776264468026120940L;
	private String userId;
	@Column(name="book_catalog_id")
	private Long bookCatalogId;

	public RatingAndReviewPK() {
		super();
	}

	public RatingAndReviewPK(String userId, Long bookCatalogIdId) {
		this.userId = userId;
		this.bookCatalogId = bookCatalogIdId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getBookCatalogId() {
		return bookCatalogId;
	}

	public void setBookCatalogId(Long bookCatalogId) {
		this.bookCatalogId = bookCatalogId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((bookCatalogId == null) ? 0 : bookCatalogId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RatingAndReviewPK other = (RatingAndReviewPK) obj;
		if (bookCatalogId == null) {
			if (other.bookCatalogId != null)
				return false;
		} else if (!bookCatalogId.equals(other.bookCatalogId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

}
