package com.foo.library.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity
public class Rent {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToOne(cascade={CascadeType.REFRESH})
	private Book book;

	private String userId;
	private Date issuedDate;
	private Date dueDate;
	private Date actualReturnDate;
	private Boolean isClosed = false;

	@Transient
	private boolean isDueDatePassed;
	
	public Rent() {
		super();
	}

	public Rent(String userId, Date issuedDate, Date dueDate) {
		this.userId = userId;
		this.issuedDate = issuedDate;
		this.dueDate = dueDate;
	}

	public boolean getIsDueDatePassed() {
		return isDueDatePassed;
	}

	public void setIsDueDatePassed(boolean isDueDatePassed) {
		this.isDueDatePassed = isDueDatePassed;
	}

	public Boolean getIsClosed() {
		return isClosed;
	}

	public void setIsClosed(Boolean isClosed) {
		this.isClosed = isClosed;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long rentId) {
		this.id = rentId;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getIssuedDate() {
		return issuedDate;
	}

	public void setIssuedDate(Date issuedDate) {
		this.issuedDate = issuedDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Date getActualReturnDate() {
		return actualReturnDate;
	}

	public void setActualReturnDate(Date actualReturnDate) {
		this.actualReturnDate = actualReturnDate;
	}

	@Override
	public String toString() {
		return "Rent [id=" + id + ", book=" + book + ", userId=" + userId
				+ ", issuedDate=" + issuedDate + ", dueDate=" + dueDate
				+ ", actualReturnDate=" + actualReturnDate + ", isClosed="
				+ isClosed + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((actualReturnDate == null) ? 0 : actualReturnDate.hashCode());
		result = prime * result
				+ ((book == null) ? 0 : book.getId().hashCode());
		result = prime * result
				+ ((issuedDate == null) ? 0 : issuedDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((dueDate == null) ? 0 : dueDate.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result
				+ ((isClosed == null) ? 0 : isClosed.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Rent))
			return false;
		Rent other = (Rent) obj;
		if (actualReturnDate == null) {
			if (other.getActualReturnDate() != null)
				return false;
		} else if (!actualReturnDate.equals(other.getActualReturnDate()))
			return false;
		if (book == null) {
			if (other.getBook() != null)
				return false;
		} else if (!book.getId().equals(other.getBook().getId()))
			return false;
		if (issuedDate == null) {
			if (other.getIssuedDate() != null)
				return false;
		} else if (!issuedDate.equals(other.getIssuedDate()))
			return false;
		if (id == null) {
			if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId()))
			return false;
		if (dueDate == null) {
			if (other.getDueDate() != null)
				return false;
		} else if (!dueDate.equals(other.getDueDate()))
			return false;
		if (userId == null) {
			if (other.getUserId() != null)
				return false;
		} else if (!userId.equals(other.getUserId()))
			return false;
		if (isClosed == null) {
			if (other.getIsClosed() != null)
				return false;
		} else if (!isClosed.equals(other.getIsClosed()))
			return false;
		return true;
	}

}
