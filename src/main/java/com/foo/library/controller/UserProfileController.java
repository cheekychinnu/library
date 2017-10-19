package com.foo.library.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.foo.library.model.BookCatalog;
import com.foo.library.model.Rent;
import com.foo.library.model.User;
import com.foo.library.model.Watcher;
import com.foo.library.service.LibraryService;

@Controller
@RequestMapping("/user")
public class UserProfileController {

	@Autowired
	private LibraryService libraryService;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showUserProfilePage(HttpSession session) {
		User loggedInUser = (User) session.getAttribute("loggedInUser");
		String userId = loggedInUser.getId();

		List<Rent> openRents = libraryService.getOpenRents(userId);

		ModelAndView modelAndView = new ModelAndView("profile");
		modelAndView.addObject("user", loggedInUser);
		modelAndView.addObject("openRents", openRents);

		return modelAndView;
	}

	@RequestMapping(value = "/watching", method = RequestMethod.GET)
	public ModelAndView getWatchingBooksByUser(HttpSession session) {
		User loggedInUser = (User) session.getAttribute("loggedInUser");
		String userId = loggedInUser.getId();

		List<BookCatalog> allBookCatalogsWithRatingsAndAvailability = libraryService
				.getAllBookCatalogsWithRatingsAndAvailability();
		Map<Long, BookCatalog> bookCatalogIdToBookCatalogMap = allBookCatalogsWithRatingsAndAvailability
				.stream().collect(Collectors.toMap(b -> b.getId(), b -> b));
		List<Watcher> watchers = libraryService.getWatchersForUserId(userId);

		for (Watcher watcher : watchers) {
			Long bookCatalogId = watcher.getId().getBookCatalogId();
			BookCatalog bookCatalog = bookCatalogIdToBookCatalogMap
					.get(bookCatalogId);
			watcher.setBookCatalog(bookCatalog);
		}
		ModelAndView modelAndView = new ModelAndView("watching");
		modelAndView.addObject("user", loggedInUser);
		modelAndView.addObject("watching", watchers);

		return modelAndView;
	}
}
