package com.foo.library.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.foo.library.model.RatingAndReview;
import com.foo.library.service.LibraryService;

@RestController
@RequestMapping(value = "/rest/ratingAndReview")
public class RestRatingAndReviewController {

	@Autowired
	private LibraryService libraryService;

	@RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
	public List<RatingAndReview> getAllRatingAndReviewsByUser(
			@PathVariable String userId) {
		System.out.println("called : getAllRatingAndReviewsByUser");
		List<RatingAndReview> ratingAndReviewsForUser = libraryService
				.getRatingAndReviewsForUser(userId);
		return ratingAndReviewsForUser;
	}

	@RequestMapping(value = "/{bookCatalogId}/user/{userId}", method = RequestMethod.GET)
	public ResponseEntity<RatingAndReview> getUserRatingAndReviewForBookCatalog(
			@PathVariable Long bookCatalogId, @PathVariable String userId) {
		return getResponseEntityWithRatingAndReview(bookCatalogId, userId,
				HttpStatus.OK, HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/{bookCatalogId}/user/{userId}", method = RequestMethod.POST)
	public ResponseEntity<RatingAndReview> addUserRatingAndReviewForBookCatalog(
			@PathVariable Long bookCatalogId, @PathVariable String userId,
			@RequestBody RatingAndReview ratingAndReview) {
		try {
			libraryService.rateAndReview(bookCatalogId, userId,
					ratingAndReview.getRating(), ratingAndReview.getReview());
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return getResponseEntityWithRatingAndReview(bookCatalogId, userId,
				HttpStatus.CREATED, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@RequestMapping(value = "/{bookCatalogId}/user/{userId}", method = RequestMethod.PUT)
	public ResponseEntity<RatingAndReview> updateUserRatingAndReviewForBookCatalog(
			@PathVariable Long bookCatalogId, @PathVariable String userId,
			@RequestBody RatingAndReview ratingAndReview) {
		String review = ratingAndReview.getReview();
		libraryService.insertOrUpdateReview(bookCatalogId, userId, review);

		Integer rating = ratingAndReview.getRating();
		libraryService.insertOrUpdateRating(bookCatalogId, userId, rating);

		return getResponseEntityWithRatingAndReview(bookCatalogId, userId,
				HttpStatus.OK, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@RequestMapping(value = "/{bookCatalogId}/user/{userId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteUserRatingAndReviewForBookCatalog(
			@PathVariable Long bookCatalogId, @PathVariable String userId) {

		libraryService.deleteRatingAndReview(bookCatalogId, userId);

		ResponseEntity<?> responseEntity = getResponseEntityWithRatingAndReview(
				bookCatalogId, userId, HttpStatus.OK, HttpStatus.OK);
		return responseEntity;
	}

	@RequestMapping(value = "/{bookCatalogId}/user/{userId}/review", method = RequestMethod.PUT)
	public ResponseEntity<RatingAndReview> updateUserReviewForBookCatalog(
			@PathVariable Long bookCatalogId, @PathVariable String userId,
			@RequestBody RatingAndReview ratingAndReview) {
		String review = ratingAndReview.getReview();
		libraryService.insertOrUpdateReview(bookCatalogId, userId, review);
		return getResponseEntityWithRatingAndReview(bookCatalogId, userId,
				HttpStatus.OK, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@RequestMapping(value = "/{bookCatalogId}/user/{userId}/review", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteUserReviewForBookCatalog(
			@PathVariable Long bookCatalogId, @PathVariable String userId) {
		libraryService.deleteReview(bookCatalogId, userId);
		ResponseEntity<?> responseEntity = getResponseEntityWithRatingAndReview(
				bookCatalogId, userId, HttpStatus.OK, HttpStatus.OK);
		return responseEntity;
	}

	@RequestMapping(value = "/{bookCatalogId}/user/{userId}/rating", method = RequestMethod.PUT)
	public ResponseEntity<RatingAndReview> updateUserRatingForBookCatalog(
			@PathVariable Long bookCatalogId, @PathVariable String userId,
			@RequestBody RatingAndReview ratingAndReview) {
		Integer rating = ratingAndReview.getRating();
		libraryService.insertOrUpdateRating(bookCatalogId, userId, rating);
		return getResponseEntityWithRatingAndReview(bookCatalogId, userId,
				HttpStatus.OK, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@RequestMapping(value = "/{bookCatalogId}/user/{userId}/rating", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteUserRatingForBookCatalog(
			@PathVariable Long bookCatalogId, @PathVariable String userId) {
		libraryService.deleteRating(bookCatalogId, userId);
		ResponseEntity<?> responseEntity = getResponseEntityWithRatingAndReview(
				bookCatalogId, userId, HttpStatus.OK, HttpStatus.OK);
		return responseEntity;
	}

	@RequestMapping(value = "/{bookCatalogId}", method = RequestMethod.GET)
	public List<RatingAndReview> getAllRatingAndReviewsForBookCatalog(
			@PathVariable Long bookCatalogId) {
		List<RatingAndReview> ratingAndReviewsForBookCatalog = libraryService
				.getRatingAndReviewsForBookCatalog(bookCatalogId);
		return ratingAndReviewsForBookCatalog;
	}

	private ResponseEntity<RatingAndReview> getResponseEntityWithRatingAndReview(
			Long bookCatalogId, String userId, HttpStatus httpStatus,
			HttpStatus failureStatus) {
		Optional<RatingAndReview> ratingAndReviewForBookCatalogAndUser = libraryService
				.getRatingAndReviewForBookCatalogAndUser(bookCatalogId, userId);
		ResponseEntity<RatingAndReview> responseEntity = ratingAndReviewForBookCatalogAndUser
				.map(r -> new ResponseEntity<>(r, httpStatus)).orElse(
						new ResponseEntity<>(failureStatus));
		return responseEntity;
	}
}
