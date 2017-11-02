package com.foo.library.service;

import java.util.List;

import com.foo.library.model.RatingAndReview;

public interface RatingAndReviewService {

	void rateAndReview(Long bookCatalogId, String userId, Integer rating,
			String review);

	void updateRating(Long bookCatalogId, String userId, Integer rating);

	void updateReview(Long bookCatalogId, String userId, String review);

	List<RatingAndReview> getRatingAndReviewsForUser(String userId);

	void insertOrUpdateRating(Long bookCatalogId, String userId, Integer rating);
	
	void insertOrUpdateReview(Long bookCatalogId, String userId, String review);
	
	List<RatingAndReview> getRatingAndReviewsForBookCatalog(Long bookCatalogId);

}
