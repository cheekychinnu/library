package com.foo.library.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Penalty {

	@Id
	@Column(name = "rent_id")
	private Long rentId;

	@OneToOne(cascade={CascadeType.REFRESH})
	@JoinColumn(name = "rent_id", referencedColumnName = "id", insertable = false, updatable = false)
	private Rent rent;

	@Enumerated(EnumType.STRING)
	private PenaltyReason reason;

	@Enumerated(EnumType.STRING)
	private PenaltyType type;

	@Enumerated(EnumType.STRING)
	private PenaltyStatus status;
	
	private Double amount;

	@Column(name = "book_id")
	private Long bookId;

	@ManyToOne(cascade={CascadeType.REFRESH})
	@JoinColumn(name = "book_id", referencedColumnName = "id", insertable = false, updatable = false)
	private Book book;

	public Penalty()
	{
		super();
	}
	
	public Penalty(Long rentId, PenaltyReason penaltyReason, PenaltyStatus penaltyStatus)
	{
		this.rentId = rentId;
		this.reason = penaltyReason;
		this.status = penaltyStatus;
	}
	
	public PenaltyStatus getStatus() {
		return status;
	}

	public void setStatus(PenaltyStatus status) {
		this.status = status;
	}

	public Long getRentId() {
		return rentId;
	}

	public void setRentId(Long rentId) {
		this.rentId = rentId;
	}

	public Rent getRent() {
		return rent;
	}

	public void setRent(Rent rent) {
		this.rent = rent;
	}

	public PenaltyReason getReason() {
		return reason;
	}

	public void setReason(PenaltyReason reason) {
		this.reason = reason;
	}

	public PenaltyType getType() {
		return type;
	}

	public void setType(PenaltyType type) {
		this.type = type;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((bookId == null) ? 0 : bookId.hashCode());
		result = prime * result + ((reason == null) ? 0 : reason.hashCode());
		result = prime * result + ((rentId == null) ? 0 : rentId.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Penalty))
			return false;
		Penalty other = (Penalty) obj;
		if (amount == null) {
			if (other.getAmount() != null)
				return false;
		} else if (!amount.equals(other.getAmount()))
			return false;
		if (bookId == null) {
			if (other.getBookId() != null)
				return false;
		} else if (!bookId.equals(other.getBookId()))
			return false;
		if (reason != other.getReason())
			return false;
		if (rentId == null) {
			if (other.getRentId() != null)
				return false;
		} else if (!rentId.equals(other.getRentId()))
			return false;
		if (type != other.getType())
			return false;
		if (status != other.getStatus())
			return false;
		return true;
	}

}
