package com.foo.library.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.foo.library.model.RatingAndReview;
import com.foo.library.service.LibraryService;
import com.foo.library.service.exception.BookCatalogNotFoundException;

@WebMvcTest(RestRatingAndReviewController.class)
@RunWith(SpringRunner.class)
public class RestRatingAndReviewControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	private EnhancedRandom enhancedRandom =  EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build(); 

	   private MediaType applicationJsonMediaType =
	            new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	   
	@MockBean
	private LibraryService libraryService;

	@Test
	public void testGetAllRatingAndReviewForUser() throws Exception {
		String userId = "vinodhini";
		RatingAndReview ratingAndReview = enhancedRandom.nextObject(RatingAndReview.class);
		List<RatingAndReview> ratingAndReviews = Stream.of(ratingAndReview).collect(Collectors.toList());
		when(libraryService.getRatingAndReviewsForUser(userId)).thenReturn(ratingAndReviews);
		mockMvc
		.perform(get("/rest/ratingAndReview/user/"+userId))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$", hasSize(1)))
		.andExpect(jsonPath("$[0].rating", is(ratingAndReview.getRating())))
		.andExpect(jsonPath("$[0].review", is(ratingAndReview.getReview())));
		
		verify(libraryService).getRatingAndReviewsForUser(userId);
	}
	
	@Test
	public void testGetAllRatingAndReviewForBookCatalogAndUser() throws Exception {
		String userId = "vinodhini";
		Long bookCatalogId = 1L;
		
		RatingAndReview ratingAndReview = enhancedRandom.nextObject(RatingAndReview.class);
		Optional<RatingAndReview> optional = Optional.of(ratingAndReview);
		when(libraryService.getRatingAndReviewForBookCatalogAndUser(bookCatalogId, userId)).thenReturn(optional);
		mockMvc
		.perform(get("/rest/ratingAndReview/"+bookCatalogId+"/user/"+userId))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$.rating", is(ratingAndReview.getRating())))
		.andExpect(jsonPath("$.review", is(ratingAndReview.getReview())));
		
		verify(libraryService).getRatingAndReviewForBookCatalogAndUser(bookCatalogId, userId);
	}
	
	@Test
	public void testGetAllRatingAndReviewForBookCatalog() throws Exception {
		Long bookCatalogId = 1L;
		
		RatingAndReview ratingAndReview1 = enhancedRandom.nextObject(RatingAndReview.class);
		RatingAndReview ratingAndReview2 = enhancedRandom.nextObject(RatingAndReview.class);
		List<RatingAndReview> ratingAndReviews = Stream.of(ratingAndReview1, ratingAndReview2).collect(Collectors.toList());
		when(libraryService.getRatingAndReviewsForBookCatalog(bookCatalogId)).thenReturn(ratingAndReviews);
		mockMvc
		.perform(get("/rest/ratingAndReview/"+bookCatalogId))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$", hasSize(ratingAndReviews.size())))
		.andExpect(jsonPath("$[0].rating", is(ratingAndReview1.getRating())))
		.andExpect(jsonPath("$[0].review", is(ratingAndReview1.getReview())));
		
		verify(libraryService).getRatingAndReviewsForBookCatalog(bookCatalogId);
	}
	
	@Test
	public void testAddRatingAndReview() throws Exception {
		String userId = "vinodhini";
		Long bookCatalogId = 1L;
		RatingAndReview ratingAndReview = enhancedRandom.nextObject(RatingAndReview.class);
		
		Optional<RatingAndReview> optional = Optional.of(ratingAndReview);
		when(libraryService.getRatingAndReviewForBookCatalogAndUser(bookCatalogId, userId)).thenReturn(optional);
		
		String jsonOfRatingAndReview = "{ \"rating\":" + ratingAndReview.getRating() + ",\"review\":\"" + ratingAndReview.getReview() +
        "\"}";
         
		mockMvc
		.perform(post("/rest/ratingAndReview/"+bookCatalogId+"/user/"+userId)
				.accept(applicationJsonMediaType)
				.contentType(this.applicationJsonMediaType)
				.content(jsonOfRatingAndReview))
		.andDo(print())
		.andExpect(status().isCreated())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$.rating", is(ratingAndReview.getRating())))
		.andExpect(jsonPath("$.review", is(ratingAndReview.getReview())));
		
		verify(libraryService).rateAndReview(bookCatalogId, userId, ratingAndReview.getRating(), ratingAndReview.getReview());
	}
	
	@Test
	public void testAddRatingAndReviewForExceptions() throws Exception {
		String userId = "vinodhini";
		Long bookCatalogId = 1L;
		RatingAndReview ratingAndReview = enhancedRandom.nextObject(RatingAndReview.class);
		
		Integer rating = ratingAndReview.getRating();
		String review = ratingAndReview.getReview();
		String jsonOfRatingAndReview = "{ \"rating\":" + rating + ",\"review\":\"" + review +
        "\"}";
		
		doThrow(Exception.class).when(libraryService).rateAndReview(bookCatalogId, userId, rating, review);
		
		mockMvc
		.perform(post("/rest/ratingAndReview/"+bookCatalogId+"/user/"+userId)
				.accept(applicationJsonMediaType)
				.contentType(this.applicationJsonMediaType)
				.content(jsonOfRatingAndReview))
		.andDo(print())
		.andExpect(status().isInternalServerError());
		
		verify(libraryService).rateAndReview(bookCatalogId, userId, rating, review);
	}
	
	@Test
	public void testUpdateUserReviewForBookCatalog() throws Exception {
		String userId = "vinodhini";
		Long bookCatalogId = 1L;
		RatingAndReview ratingAndReview = enhancedRandom.nextObject(RatingAndReview.class);
		
		Optional<RatingAndReview> optional = Optional.of(ratingAndReview);
		when(libraryService.getRatingAndReviewForBookCatalogAndUser(bookCatalogId, userId)).thenReturn(optional);
		
		String review = ratingAndReview.getReview();
		String jsonOfRatingAndReview = "{ \"review\":\"" + review +
        "\"}";
         
		mockMvc
		.perform(put("/rest/ratingAndReview/"+bookCatalogId+"/user/"+userId)
				.accept(applicationJsonMediaType)
				.contentType(this.applicationJsonMediaType)
				.content(jsonOfRatingAndReview))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$.rating", is(ratingAndReview.getRating())))
		.andExpect(jsonPath("$.review", is(review)));
		verify(libraryService).insertOrUpdateReview(bookCatalogId, userId, review);
	}
	
	@Test
	public void testUpdateUserRatingForBookCatalogForFailureCase() throws Exception {
		String userId = "vinodhini";
		Long bookCatalogId = 1L;
		RatingAndReview ratingAndReview = enhancedRandom.nextObject(RatingAndReview.class);
		
		Integer rating =ratingAndReview.getRating();
		String jsonOfRatingAndReview = "{ \"rating\":" + rating+
        "}";
         
		mockMvc
		.perform(put("/rest/ratingAndReview/"+bookCatalogId+"/user/"+userId)
				.accept(applicationJsonMediaType)
				.contentType(this.applicationJsonMediaType)
				.content(jsonOfRatingAndReview))
		.andDo(print())
		.andExpect(status().isInternalServerError());
		verify(libraryService).insertOrUpdateRating(bookCatalogId, userId, rating);
		verify(libraryService).getRatingAndReviewForBookCatalogAndUser(bookCatalogId, userId);
	}
	
	@Test
	public void testUpdateUserRatingForBookCatalogForExceptionCase() throws Exception {
		String userId = "vinodhini";
		Long bookCatalogId = 1L;
		RatingAndReview ratingAndReview = enhancedRandom.nextObject(RatingAndReview.class);
		
		Integer rating =ratingAndReview.getRating();
		String jsonOfRatingAndReview = "{ \"rating\":" + rating+
        "}";
        
		doThrow(new BookCatalogNotFoundException(bookCatalogId)).when(libraryService).insertOrUpdateRating(bookCatalogId, userId, rating);
		String expectedContent = "[{\"logref\":\"1\",\"message\":\"BookCatalogId :1 is not found\",\"links\":[]}]";
		MvcResult mvcResult = mockMvc
		.perform(put("/rest/ratingAndReview/"+bookCatalogId+"/user/"+userId)
				.accept(applicationJsonMediaType)
				.contentType(this.applicationJsonMediaType)
				.content(jsonOfRatingAndReview))
		.andDo(print())
		.andExpect(status().isNotFound())
		.andExpect(content().contentType("application/vnd.error"))
		.andExpect(jsonPath("$", hasSize(1)))
		.andExpect(jsonPath("$[0].message", is("BookCatalogId :1 is not found")))
		.andExpect(header().string("Content-Type", "application/vnd.error")).andReturn();
		assertNotNull(mvcResult);
		MockHttpServletResponse response = mvcResult.getResponse();
		assertEquals("application/vnd.error",response.getContentType());
		assertEquals(expectedContent, response.getContentAsString());
		
		verify(libraryService).insertOrUpdateRating(bookCatalogId, userId, rating);
		verify(libraryService, never()).getRatingAndReviewForBookCatalogAndUser(bookCatalogId, userId);
	}
	
	@Test
	public void testUpdateUserRatingAndReviewForBookCatalog() throws Exception {
		String userId = "vinodhini";
		Long bookCatalogId = 1L;
		RatingAndReview ratingAndReview = enhancedRandom.nextObject(RatingAndReview.class);
		
		Optional<RatingAndReview> optional = Optional.of(ratingAndReview);
		when(libraryService.getRatingAndReviewForBookCatalogAndUser(bookCatalogId, userId)).thenReturn(optional);
		
		String review = ratingAndReview.getReview();
		Integer rating = ratingAndReview.getRating();
		String jsonOfRatingAndReview = "{ \"rating\":" + ratingAndReview.getRating() + ",\"review\":\"" + ratingAndReview.getReview() +
		        "\"}";
		          
		mockMvc
		.perform(put("/rest/ratingAndReview/"+bookCatalogId+"/user/"+userId)
				.accept(applicationJsonMediaType)
				.contentType(this.applicationJsonMediaType)
				.content(jsonOfRatingAndReview))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$.rating", is(ratingAndReview.getRating())))
		.andExpect(jsonPath("$.review", is(review)));
		verify(libraryService).insertOrUpdateReview(bookCatalogId, userId, review);
		verify(libraryService).insertOrUpdateRating(bookCatalogId, userId, rating);
	}
	
	@Test
	public void testDeleteRatingAndReview() throws Exception {

		String userId = "vinodhini";
		Long bookCatalogId = 1L;
		
		mockMvc
		.perform(delete("/rest/ratingAndReview/"+bookCatalogId+"/user/"+userId))
		.andDo(print())
		.andExpect(status().isOk());
		verify(libraryService).deleteRatingAndReview(bookCatalogId, userId);
	
	}
	
	@Test
	public void testDeleteRating() throws Exception {

		String userId = "vinodhini";
		Long bookCatalogId = 1L;
		
		mockMvc
		.perform(delete("/rest/ratingAndReview/"+bookCatalogId+"/user/"+userId+"/rating"))
		.andDo(print())
		.andExpect(status().isOk());
		verify(libraryService).deleteRating(bookCatalogId, userId);
	
	}
	
	@Test
	public void testDeleteReview() throws Exception {

		String userId = "vinodhini";
		Long bookCatalogId = 1L;
		
		mockMvc
		.perform(delete("/rest/ratingAndReview/"+bookCatalogId+"/user/"+userId+"/review"))
		.andDo(print())
		.andExpect(status().isOk());
		verify(libraryService).deleteReview(bookCatalogId, userId);
	
	}
	
	@Test
	public void testDeleteReviewForFailure() throws Exception {

		String userId = "vinodhini";
		Long bookCatalogId = 1L;
		RatingAndReview ratingAndReview = enhancedRandom.nextObject(RatingAndReview.class);
		
		Optional<RatingAndReview> optional = Optional.of(ratingAndReview);
		when(libraryService.getRatingAndReviewForBookCatalogAndUser(bookCatalogId, userId)).thenReturn(optional);
		
		mockMvc
		.perform(delete("/rest/ratingAndReview/"+bookCatalogId+"/user/"+userId+"/review"))
		.andDo(print())
		.andExpect(status().isInternalServerError());
		verify(libraryService).deleteReview(bookCatalogId, userId);
	
	}	

}

