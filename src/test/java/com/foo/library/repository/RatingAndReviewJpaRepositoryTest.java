package com.foo.library.repository;

import static org.junit.Assert.*;

import java.util.List;

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
	
	@Test
	public void testAverageRating()
	{
		BookCatalog bookCatalog = createTestBookCatalog();
		String userId = "vino";
		
		RatingAndReview ratingAndReview = new RatingAndReview();
		ratingAndReview.setRating(2);
		ratingAndReview.setReview("test review");
		RatingAndReviewPK ratingAndReviewPK = new RatingAndReviewPK(userId, bookCatalog.getId());
		ratingAndReview.setId(ratingAndReviewPK);
		
		ratingAndReviewJpaRepository.saveAndFlush(ratingAndReview);
		
		userId = "chinnu";
		ratingAndReview = new RatingAndReview();
		ratingAndReview.setRating(5);
		ratingAndReview.setReview("test review");
		ratingAndReviewPK = new RatingAndReviewPK(userId, bookCatalog.getId());
		ratingAndReview.setId(ratingAndReviewPK);
		ratingAndReviewJpaRepository.saveAndFlush(ratingAndReview);
		entityManager.clear();
		List<BookCatalog> averageRatingForAllBookCatalogs = ratingAndReviewJpaRepository.computeAverageRatingForAllBookCatalogs();
		assertNotNull(averageRatingForAllBookCatalogs);
		assertEquals(1, averageRatingForAllBookCatalogs.size());
		assertEquals(bookCatalog, averageRatingForAllBookCatalogs.get(0));
		Double averageRating = averageRatingForAllBookCatalogs.get(0).getAverageRating();
		assertNotNull(averageRating);
		assertEquals(new Double(3.5d), averageRating);
	}
	
	@Test
	public void testAverageRatingForBookWithOneNullRating()
	{
		BookCatalog bookCatalog = createTestBookCatalog();
		String userId = "chinnu";
		
		RatingAndReview ratingAndReview = new RatingAndReview();
		ratingAndReview.setReview("test review");
		RatingAndReviewPK ratingAndReviewPK = new RatingAndReviewPK(userId, bookCatalog.getId());
		ratingAndReview.setId(ratingAndReviewPK);
		
		ratingAndReviewJpaRepository.saveAndFlush(ratingAndReview);
		
		userId = "vino";
		ratingAndReview = new RatingAndReview();
		ratingAndReview.setRating(5);
		ratingAndReview.setReview("test review");
		ratingAndReviewPK = new RatingAndReviewPK(userId, bookCatalog.getId());
		ratingAndReview.setId(ratingAndReviewPK);
		ratingAndReviewJpaRepository.saveAndFlush(ratingAndReview);
		entityManager.clear();
		List<BookCatalog> averageRatingForAllBookCatalogs = ratingAndReviewJpaRepository.computeAverageRatingForAllBookCatalogs();
		assertNotNull(averageRatingForAllBookCatalogs);
		assertEquals(1, averageRatingForAllBookCatalogs.size());
		assertEquals(bookCatalog, averageRatingForAllBookCatalogs.get(0));
		Double averageRating = averageRatingForAllBookCatalogs.get(0).getAverageRating();
		assertNotNull(averageRating);
		assertEquals(new Double(5), averageRating);
	}
	
	@Test
	public void testAverageRatingForBookWithNoRating()
	{
		BookCatalog bookCatalog = createTestBookCatalog();
		String userId = "chinnu";
		
		RatingAndReview ratingAndReview = new RatingAndReview();
		ratingAndReview.setReview("test review");
		RatingAndReviewPK ratingAndReviewPK = new RatingAndReviewPK(userId, bookCatalog.getId());
		ratingAndReview.setId(ratingAndReviewPK);
		
		ratingAndReviewJpaRepository.saveAndFlush(ratingAndReview);
		
		List<BookCatalog> averageRatingForAllBookCatalogs = ratingAndReviewJpaRepository.computeAverageRatingForAllBookCatalogs();
		assertNotNull(averageRatingForAllBookCatalogs);
		assertEquals(1, averageRatingForAllBookCatalogs.size());
		assertEquals(bookCatalog, averageRatingForAllBookCatalogs.get(0));
		Double averageRating = averageRatingForAllBookCatalogs.get(0).getAverageRating();
		assertNull(averageRating);
	}
	
	private BookCatalog createTestBookCatalog() {
		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = new BookCatalog(bookName, author, isbn);
		return bookCatalogJpaRepository.saveAndFlush(bookCatalog);
	}
}
