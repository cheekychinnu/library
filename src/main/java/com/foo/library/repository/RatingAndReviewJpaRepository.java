package com.foo.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.foo.library.model.RatingAndReview;
import com.foo.library.model.RatingAndReviewPK;

@Repository
public interface RatingAndReviewJpaRepository extends
		JpaRepository<RatingAndReview, RatingAndReviewPK> {
	
	List<RatingAndReview> findByIdBookCatalogId(Long bookCatalogId);
	
	List<RatingAndReview> findByIdUserId(String userId);
	
	@Modifying
	@Query("update RatingAndReview m set m.review = :review where m.id.userId = :userId and"
			+ " m.id.bookCatalogId = :bookCatalogId")
	int updateUserReviewForBook(@Param("userId") String userId,
			@Param("bookCatalogId") Long bookCatalogId,
			@Param("review") String review);
	
	@Modifying
	@Query("update RatingAndReview m set m.rating = :rating where m.id.userId = :userId and"
			+ " m.id.bookCatalogId = :bookCatalogId")
	int updateUserRatingForBook(@Param("userId") String userId,
			@Param("bookCatalogId") Long bookCatalogId,
			@Param("rating") Integer rating);
	
}
