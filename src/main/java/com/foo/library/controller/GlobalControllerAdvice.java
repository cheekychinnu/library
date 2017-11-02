package com.foo.library.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.foo.library.model.RatingAndReview;
import com.foo.library.model.RatingAndReviewPK;

@ControllerAdvice(assignableTypes={UserProfileController.class, BookController.class})
public class GlobalControllerAdvice {
	
	@ModelAttribute("possibleRatings")
	public List<Integer> getPossibleRatings() {
		return IntStream.rangeClosed(1, 5).mapToObj(Integer::new)
				.collect(Collectors.toList());
	}
	
	@ModelAttribute("ratingAndReview")
	public RatingAndReview ratingAndReview() {
		RatingAndReviewPK id = new RatingAndReviewPK();
		RatingAndReview ratingAndReview = new RatingAndReview();
		ratingAndReview.setId(id);
		return ratingAndReview;
	}	
}
