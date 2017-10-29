package com.foo.library.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.foo.library.model.BookCatalog;
import com.foo.library.model.RatingAndReview;
import com.foo.library.model.Rent;
import com.foo.library.model.RentResponse;
import com.foo.library.model.ReturnResponse;
import com.foo.library.model.User;
import com.foo.library.service.LibraryService;

@Controller
@RequestMapping("/books")
public class BookController {

	@Autowired
	private LibraryService libraryService;

	@RequestMapping(value="/getAllBooks", method=RequestMethod.GET)
	public ModelAndView getAllBooks(HttpSession session) {
		User user = (User) session.getAttribute("loggedInUser");
		String userId = user.getId();
		List<BookCatalog> allBookCatalog = libraryService
				.getAllBookCatalogsWithRatingsAndAvailability();

		List<RatingAndReview> ratingAndReviewsForUser = libraryService
				.getRatingAndReviewsForUser(userId);
		Map<Long, RatingAndReview> bookCatalogIdToRatingsByUser = ratingAndReviewsForUser
				.stream().collect(
						Collectors.toMap(r -> r.getId().getBookCatalogId(),
								e -> e));

		List<Rent> allRentsForUser = libraryService.getAllRents(userId);
		Map<Long, List<Rent>> bookCatalogIdToRentsByUser = allRentsForUser
				.stream().collect(
						Collectors.groupingBy(r -> r.getBook().getBookCatalog()
								.getId()));

		List<BookCatalogWithUserContext> bookCatalogsWithUserContext = new ArrayList<>();
		for (BookCatalog bookCatalog : allBookCatalog) {
			BookCatalogWithUserContext bookCatalogWithUserContext = new BookCatalogWithUserContext(bookCatalog);

			Long bookCatalogId = bookCatalog.getId();
			List<Rent> rents = bookCatalogIdToRentsByUser.get(bookCatalogId);
			if (rents != null) {
				bookCatalogWithUserContext.setIsAlreadyRented(true);
				Optional<Rent> openRent = rents.stream()
						.filter(r -> !r.getIsClosed()).findFirst();
				if (openRent.isPresent()) {
					bookCatalogWithUserContext.setCurrentOpenRent(openRent
							.get());
				}
			}

			RatingAndReview ratingAndReview = bookCatalogIdToRatingsByUser
					.get(bookCatalogId);
			if (ratingAndReview != null) {
				bookCatalogWithUserContext.setRatingAndReview(ratingAndReview);
			}

			bookCatalogsWithUserContext.add(bookCatalogWithUserContext);
		}
		System.out.println(bookCatalogsWithUserContext);
		return new ModelAndView("book", "allBookCatalogs",
				bookCatalogsWithUserContext);
	}

	@RequestMapping(value = "/return", method=RequestMethod.GET)
	public String returnBook(@RequestParam("rentId") Long rentId, Model model,
			@RequestHeader("referer") String referedFrom, RedirectAttributes redirectAttributes) {
		ReturnResponse returnBookResponse = libraryService.returnBook(rentId);
		Boolean isDueDateMissed = returnBookResponse.getIsDueDateMissed();
		redirectAttributes.addFlashAttribute("isDueDateMissed", isDueDateMissed);
		return "redirect:" + referedFrom;
	}
	
	@RequestMapping(value = "/rent",method=RequestMethod.GET)
	public String rentBook(@RequestParam("bookCatalogId") Long bookCatalogId, Model model, HttpSession session,
			@RequestHeader("referer") String referedFrom, RedirectAttributes redirectAttributes) {
		User user = (User) session.getAttribute("loggedInUser");
		String userId = user.getId();
		
		RentResponse rentResponse = libraryService.rentBook(userId, bookCatalogId);
		if(!rentResponse.getIsSuccess()){
			redirectAttributes.addFlashAttribute("rentResult", rentResponse.getMessage());
		} else {
			redirectAttributes.addFlashAttribute("rentResult", "Please return it before :"+rentResponse.getRent().getDueDate());
		}
		System.out.println("here"+rentResponse.getMessage());
		System.out.println(referedFrom);
		return "redirect:"+referedFrom;
	}
	
	@RequestMapping(value = "/watch",method=RequestMethod.GET)
	public String watchBookCatalog(@RequestParam("bookCatalogId") Long bookCatalogId, Model model, HttpSession session,
			@RequestHeader("referer") String referedFrom, RedirectAttributes redirectAttributes)
	{
		User user = (User) session.getAttribute("loggedInUser");
		String userId = user.getId();
		libraryService.watchForBookCatalog(userId, bookCatalogId);
		redirectAttributes.addFlashAttribute("watchMessage", "We will notify when the book becomes available");
		return "redirect:"+referedFrom;
	}
}
