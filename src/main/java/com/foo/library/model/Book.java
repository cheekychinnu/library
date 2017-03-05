package com.foo.library.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * @author Vinodhini
 *
 */
@Entity
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@ManyToOne
	private BookCatalog bookCatalog;

	private String provider;
	private Boolean isActive;
	private Boolean isAvailable;
	private String comments;

	public Book() {
		super();
	}

	public Book(String provider, Boolean isActive, Boolean isAvailable,
			String comments) {
		this.provider = provider;
		this.isActive = isActive;
		this.isAvailable = isAvailable;
		this.comments = comments;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long bookId) {
		this.id = bookId;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public BookCatalog getBookCatalog() {
		return bookCatalog;
	}

	public void setBookCatalog(BookCatalog bookCatalog) {
		this.bookCatalog = bookCatalog;
	}

	@Override
	public String toString() {
		return "Book [bookId=" + id + ", bookCatalog=" + bookCatalog
				+ ", provider=" + provider + ", isActive=" + isActive
				+ ", isAvailable=" + isAvailable + ", comments=" + comments
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((bookCatalog == null) ? 0 : bookCatalog.getId().hashCode());
		result = prime * result
				+ ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((isActive == null) ? 0 : isActive.hashCode());
		result = prime * result
				+ ((isAvailable == null) ? 0 : isAvailable.hashCode());
		result = prime * result
				+ ((provider == null) ? 0 : provider.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Book))
			return false;
		Book other = (Book) obj;
		if (bookCatalog == null) {
			if (other.getBookCatalog() != null)
				return false;
		} else if (!bookCatalog.getId().equals(other.getBookCatalog().getId()))
			return false;
		if (comments == null) {
			if (other.getComments() != null)
				return false;
		} else if (!comments.equals(other.getComments()))
			return false;
		if (id == null) {
			if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId()))
			return false;
		if (isActive == null) {
			if (other.getIsActive() != null)
				return false;
		} else if (!isActive.equals(other.getIsActive()))
			return false;
		if (isAvailable == null) {
			if (other.getIsAvailable() != null)
				return false;
		} else if (!isAvailable.equals(other.getIsAvailable()))
			return false;
		if (provider == null) {
			if (other.getProvider() != null)
				return false;
		} else if (!provider.equals(other.getProvider()))
			return false;
		return true;
	}

}
