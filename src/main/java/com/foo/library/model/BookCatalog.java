package com.foo.library.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value="books")
@Entity
public class BookCatalog {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String name;
	private String author;
	private String isbn;
	
	@OneToMany(mappedBy="bookCatalog")
	private List<Book> books = new ArrayList<>();
	
	@Transient
	private Double averageRating;
	
	@Transient
	private Boolean isAvailable;
	
	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public BookCatalog()
	{
		super();
	}

	public BookCatalog(BookCatalog bookCatalog, Double averageRating)
	{
		this(bookCatalog);
		this.averageRating = averageRating;
	}
	
	public BookCatalog(String name, String author, String isbn)
	{
		super();
		this.name = name;
		this.author = author;
		this.isbn = isbn;
	}
	
	public BookCatalog(BookCatalog bookCatalog) {
		this.id = bookCatalog.getId();
		this.author = bookCatalog.getAuthor();
		this.isbn = bookCatalog.getIsbn();
		this.books = bookCatalog.getBooks();
		this.name = bookCatalog.getName();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long bookCatalogId) {
		this.id = bookCatalogId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public List<Book> getBooks() {
		return books;
	}

	public Double getAverageRating()
	{
		return averageRating;
	}

	public Boolean getIsAvailable() {
		return isAvailable;
	}

	
	@Override
	public String toString() {
		return "BookCatalog [id=" + id + ", name=" + name + ", author="
				+ author + ", isbn=" + isbn + ", isAvailable ="+getIsAvailable()+" averageRating:"+getAverageRating()
				+"]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((isbn == null) ? 0 : isbn.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof BookCatalog))
			return false;
		BookCatalog other = (BookCatalog) obj;
		if (author == null) {
			if (other.getAuthor() != null)
				return false;
		} else if (!author.equals(other.getAuthor()))
			return false;
		if (id == null) {
			if (other.getId() != null)
				return false;
		} else if (!id.equals(other.getId()))
			return false;
		if (isbn == null) {
			if (other.getIsbn() != null)
				return false;
		} else if (!isbn.equals(other.getIsbn()))
			return false;
		if (name == null) {
			if (other.getName() != null)
				return false;
		} else if (!name.equals(other.getName()))
			return false;
		return true;
	}
}
