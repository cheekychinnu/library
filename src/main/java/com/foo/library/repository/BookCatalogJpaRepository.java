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

	// Note that I could have passed BookCatalog object. But if you are passing a half cooked object, the 
	// query where the books are joined will fail. Hence I have preferred to pass only the Id
	@Query("select b.id from BookCatalog b "
			+ "left join b.books as bb "
			+ "where b.id in :bookCatalogs and bb.isActive = 1 and bb.isAvailable = 1")
	List<Long> getAvailableAndActiveBookCatalogIds(@Param("bookCatalogs") List<Long> bookCatalogIds);
	
	// Note that this join doesn't bother if the BookCatalog is half cooked.
	// so it is safe to pass objects itself. The scenario I would like this getting used for 
	// would be something like. getAvailability -> now cook those objects with more information -> in our case it is rating
	// so what would happen if A -> 5 out of which only 2 are rated. B -> 2. simply add A to B to merge them. such cases would be 
	// handled at service layer
	@Query("select new BookCatalog(b, avg(r.rating)) from RatingAndReview r "
			+ "join r.bookCatalog as b "
			+ "where b in :bookCatalog "
			+ "group by b")
	List<BookCatalog> fillCatalogWithAverageRatingIfRatingsArePresent(@Param("bookCatalog") List<BookCatalog> bookCatalogs);
	
}
