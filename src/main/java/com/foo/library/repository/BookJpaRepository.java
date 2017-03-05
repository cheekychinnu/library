package com.foo.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.foo.library.model.Book;

@Repository
public interface BookJpaRepository extends JpaRepository<Book, Long> {
	List<Book> findByBookCatalogId(Long bookCatalogId);

	List<Book> findByBookCatalogIdAndIsActiveTrue(Long bookCatalogId);

	List<Book> findByBookCatalogIdAndIsActiveTrueAndIsAvailableTrue(
			Long bookCatalogId);

	@Modifying
	@Query("update Book set isActive = :isActive , isAvailable = :isAvailable where id = :bookId")
	int updateIsActiveAndIsAvailable(@Param("bookId") Long bookId,
			@Param("isActive") Boolean isActive,
			@Param("isAvailable") Boolean isAvailable);
	
	@Modifying
	@Query("update Book set isAvailable = :isAvailable where id = :bookId")
	int updateIsAvailable(@Param("bookId") Long bookId,
			@Param("isAvailable") Boolean isAvailable);
}
