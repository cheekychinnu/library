package com.foo.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.foo.library.model.BookCatalog;

@Repository
public interface BookCatalogJpaRepository extends
		JpaRepository<BookCatalog, Long> {

	List<BookCatalog> findByAuthorContainingIgnoreCase(String author);
	List<BookCatalog> findByNameContainingIgnoreCase(String bookName);
	List<BookCatalog> findByIsbn(String isbn);
	
	@Query("select b from BookCatalog b "
			+ "join b.books as bb "
			+ "where b.id = :bookCatalogId and bb.isActive = 1 and bb.isAvailable = 1")
	List<BookCatalog> queryByIdAndActiveAvailableBooks(@Param("bookCatalogId") Long bookCatalogId);

}
