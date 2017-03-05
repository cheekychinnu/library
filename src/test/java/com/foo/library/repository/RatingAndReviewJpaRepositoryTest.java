package com.foo.library.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.foo.library.model.BookCatalog;
import com.foo.library.model.RatingAndReview;
import com.foo.library.model.RatingAndReviewPK;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RatingAndReviewJpaRepositoryTest {
	
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private BookCatalogJpaRepository bookCatalogJpaRepository;
	
	@Autowired
	private RatingAndReviewJpaRepository ratingAndReviewJpaRepository;
	
	@Test
	public void testCreate()
	{
		BookCatalog bookCatalog = createTestBookCatalog();
		String userId = "chinnu";
		
		RatingAndReview ratingAndReview = new RatingAndReview();
		ratingAndReview.setRating(2);
		ratingAndReview.setReview("test review");
		RatingAndReviewPK ratingAndReviewPK = new RatingAndReviewPK(userId, bookCatalog.getId());
		ratingAndReview.setId(ratingAndReviewPK);
		
		ratingAndReview = ratingAndReviewJpaRepository.saveAndFlush(ratingAndReview);
		assertNotNull(ratingAndReview);
		assertEquals(ratingAndReviewPK, ratingAndReview.getId());
		
		entityManager.clear();
		
		ratingAndReview = ratingAndReviewJpaRepository.getOne(ratingAndReviewPK);
		assertNotNull(ratingAndReview.getBookCatalog());
		assertEquals(bookCatalog, ratingAndReview.getBookCatalog());
	}
	
	@Test
	public void testUpdateReview()
	{
		BookCatalog bookCatalog = createTestBookCatalog();
		String userId = "chinnu";
		
		RatingAndReview ratingAndReview = new RatingAndReview();
		ratingAndReview.setRating(2);
		ratingAndReview.setReview("test review");
		RatingAndReviewPK ratingAndReviewPK = new RatingAndReviewPK(userId, bookCatalog.getId());
		ratingAndReview.setId(ratingAndReviewPK);
		
		ratingAndReview = ratingAndReviewJpaRepository.saveAndFlush(ratingAndReview);
		
		String updatedReview = "updated review";
		int updatedRecords = ratingAndReviewJpaRepository.updateUserReviewForBook(userId, bookCatalog.getId(), updatedReview);
		assertEquals(1, updatedRecords);
		entityManager.clear();
		ratingAndReview = ratingAndReviewJpaRepository.getOne(ratingAndReviewPK);
		assertEquals(updatedReview, ratingAndReview.getReview());
	}
	
	@Test
	public void testUpdateRating()
	{
		BookCatalog bookCatalog = createTestBookCatalog();
		String userId = "chinnu";
		
		RatingAndReview ratingAndReview = new RatingAndReview();
		ratingAndReview.setRating(2);
		ratingAndReview.setReview("test review");
		RatingAndReviewPK ratingAndReviewPK = new RatingAndReviewPK(userId, bookCatalog.getId());
		ratingAndReview.setId(ratingAndReviewPK);
		
		ratingAndReview = ratingAndReviewJpaRepository.saveAndFlush(ratingAndReview);
		
		Integer updatedRating = 4;
		int updatedRecords = ratingAndReviewJpaRepository.updateUserRatingForBook(userId, bookCatalog.getId(), updatedRating);
		assertEquals(1, updatedRecords);
		entityManager.clear();
		ratingAndReview = ratingAndReviewJpaRepository.getOne(ratingAndReviewPK);
		assertEquals(updatedRating, ratingAndReview.getRating());
	}
	
	private BookCatalog createTestBookCatalog() {
		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = new BookCatalog(bookName, author, isbn);
		return bookCatalogJpaRepository.saveAndFlush(bookCatalog);
	}
}
