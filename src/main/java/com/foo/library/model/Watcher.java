package com.foo.library.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Watcher {

	@Embeddable
	public static class WatcherPK implements Serializable {
		private static final long serialVersionUID = 2196449340583231927L;

		private String userId;

		@Column(name = "book_catalog_id")
		private Long bookCatalogId;

		public WatcherPK()
		{
			super();
		}
		
		public WatcherPK(String userId, Long bookCatalogId) {
			this.bookCatalogId = bookCatalogId;
			this.userId = userId;
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
			result = prime * result
					+ ((userId == null) ? 0 : userId.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof WatcherPK))
				return false;
			WatcherPK other = (WatcherPK) obj;
			if (bookCatalogId == null) {
				if (other.getBookCatalogId() != null)
					return false;
			} else if (!bookCatalogId.equals(other.getBookCatalogId()))
				return false;
			if (userId == null) {
				if (other.getUserId() != null)
					return false;
			} else if (!userId.equals(other.getUserId()))
				return false;
			return true;
		}

	}

	@EmbeddedId
	private WatcherPK id;

	@OneToOne(cascade={CascadeType.REFRESH})
	@JoinColumn(name = "book_catalog_id", referencedColumnName = "id", insertable = false, updatable = false)
	private BookCatalog bookCatalog;

	public WatcherPK getId() {
		return id;
	}

	public void setId(WatcherPK id) {
		this.id = id;
	}

	public BookCatalog getBookCatalog() {
		return bookCatalog;
	}

	public void setBookCatalog(BookCatalog bookCatalog) {
		this.bookCatalog = bookCatalog;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Watcher other = (Watcher) obj;
		if (id == null) {
			if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}

}
