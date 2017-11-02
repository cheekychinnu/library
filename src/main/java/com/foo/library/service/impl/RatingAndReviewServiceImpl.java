package com.foo.library.service.impl;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.foo.library.model.RatingAndReview;
import com.foo.library.model.RatingAndReviewPK;
import com.foo.library.repository.RatingAndReviewJpaRepository;
import com.foo.library.service.RatingAndReviewService;

@Component
public class RatingAndReviewServiceImpl implements RatingAndReviewService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private RatingAndReviewJpaRepository ratingAndReviewJpaRepository;

	@Transactional
	@Override
	public void rateAndReview(Long bookCatalogId, String userId,
			Integer rating, String review) {
		RatingAndReviewPK ratingAndReviewPK = new RatingAndReviewPK(userId,
				bookCatalogId);
		RatingAndReview ratingAndReview = new RatingAndReview();
		ratingAndReview.setRating(rating);
		ratingAndReview.setReview(review);
		ratingAndReview.setId(ratingAndReviewPK);

		ratingAndReview = ratingAndReviewJpaRepository
				.saveAndFlush(ratingAndReview);
		entityManager.refresh(ratingAndReview); // refreshing this will
												// refresh bookCatalog
	}

	@Transactional
	@Override
	public void updateRating(Long bookCatalogId, String userId, Integer rating) {
		ratingAndReviewJpaRepository.updateUserRatingForBook(userId,
				bookCatalogId, rating);
		RatingAndReviewPK ratingAndReviewPK = new RatingAndReviewPK(userId,
				bookCatalogId);
		Optional<RatingAndReview> ratingAndReview = ratingAndReviewJpaRepository
				.findById(ratingAndReviewPK);
		entityManager.refresh(ratingAndReview.get());
	}

	@Transactional
	@Override
	public void updateReview(Long bookCatalogId, String userId, String review) {
		ratingAndReviewJpaRepository.updateUserReviewForBook(userId,
				bookCatalogId, review);
		RatingAndReviewPK ratingAndReviewPK = new RatingAndReviewPK(userId,
				bookCatalogId);
		Optional<RatingAndReview> ratingAndReview = ratingAndReviewJpaRepository
				.findById(ratingAndReviewPK);
		entityManager.refresh(ratingAndReview.get());
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

	@Transactional
	@Override
	public void insertOrUpdateRating(Long bookCatalogId, String userId,
			Integer rating) {

		RatingAndReviewPK ratingAndReviewPK = new RatingAndReviewPK(userId,
				bookCatalogId);
		Optional<RatingAndReview> ratingAndReviewOptional = ratingAndReviewJpaRepository
				.findById(ratingAndReviewPK);
		if (ratingAndReviewOptional.isPresent()) {
			if (rating != null) {
				updateRating(bookCatalogId, userId, rating);
			}
		} else {
			RatingAndReview ratingAndReview = new RatingAndReview();
			ratingAndReview.setRating(rating);
			ratingAndReview.setId(ratingAndReviewPK);

			ratingAndReview = ratingAndReviewJpaRepository
					.saveAndFlush(ratingAndReview);
			entityManager.refresh(ratingAndReview); // refreshing this will
													// refresh bookCatalog
		}

	}

	@Transactional
	@Override
	public void insertOrUpdateReview(Long bookCatalogId, String userId,
			String review) {

		RatingAndReviewPK ratingAndReviewPK = new RatingAndReviewPK(userId,
				bookCatalogId);
		Optional<RatingAndReview> ratingAndReviewOptional = ratingAndReviewJpaRepository
				.findById(ratingAndReviewPK);
		if (ratingAndReviewOptional.isPresent()) {
			if (review != null) {
				updateReview(bookCatalogId, userId, review);
			}
		} else {
			RatingAndReview ratingAndReview = new RatingAndReview();
			ratingAndReview.setReview(review);
			ratingAndReview.setId(ratingAndReviewPK);

			ratingAndReview = ratingAndReviewJpaRepository
					.saveAndFlush(ratingAndReview);
			entityManager.refresh(ratingAndReview); // refreshing this will
													// refresh bookCatalog
		}
	}
}
