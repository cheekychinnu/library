package com.foo.library.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.foo.library.model.RatingAndReview;
import com.foo.library.model.RatingAndReviewPK;
import com.foo.library.repository.RatingAndReviewJpaRepository;
import com.foo.library.service.RatingAndReviewService;

@Component
public class RatingAndReviewServiceImpl implements RatingAndReviewService {

	@Autowired
	private RatingAndReviewJpaRepository ratingAndReviewJpaRepository;

	@Override
	public void rateAndReview(Long bookCatalogId, String userId,
			Integer rating, String review) {

		RatingAndReviewPK ratingAndReviewPK = new RatingAndReviewPK(userId,
				bookCatalogId);

		RatingAndReview ratingAndReview = new RatingAndReview();
		ratingAndReview.setRating(rating);
		ratingAndReview.setReview(review);
		ratingAndReview.setId(ratingAndReviewPK);

		ratingAndReviewJpaRepository.saveAndFlush(ratingAndReview);
	}

	@Override
	public void updateRating(Long bookCatalogId, String userId, Integer rating) {
		ratingAndReviewJpaRepository.updateUserRatingForBook(userId,
				bookCatalogId, rating);
	}

	@Override
	public void updateReview(Long bookCatalogId, String userId, String review) {
		ratingAndReviewJpaRepository.updateUserReviewForBook(userId,
				bookCatalogId, review);
	}

	@Override
	public List<RatingAndReview> getRatingAndReviewsForBookCatalog(
			Long bookCatalogId) {
		return ratingAndReviewJpaRepository
				.findByIdBookCatalogId(bookCatalogId);
	}

	@Override
	public List<RatingAndReview> getRatingAndReviewsForUser(String userId) {
		return ratingAndReviewJpaRepository.findByIdUserId(userId);
	}
}
