package com.foo.library.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.foo.library.model.BookCatalog;
import com.foo.library.model.Rent;
import com.foo.library.model.Subscriber;
import com.foo.library.model.Watcher;
import com.foo.library.service.LibraryService;
import com.foo.library.service.NotificationService;

@Component
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private LibraryService libraryService;

	@Override
	public void notifySubscriberForNewAddition(BookCatalog catalog) {
		List<Subscriber> subscribersForNewAddition = libraryService
				.getSubscribersForNewAdditions();
		List<String> userIdsToNotify = subscribersForNewAddition.stream()
				.map(s -> s.getUserId()).collect(Collectors.toList());
		// TODO : implement the logic. async would be a preferrable approach
		// here
		System.out.println("Users to notify :" + userIdsToNotify
				+ " for catalog:" + catalog);
	}

	@Override
	public void notifyWatchers(Long bookCatalogId) {
		List<Watcher> watchers = libraryService.getWatchers(bookCatalogId);
		List<String> usersToNotify = watchers.stream()
				.map(w -> w.getId().getUserId()).collect(Collectors.toList());
		// send mail to all the watchers
	}

	@Override
	public void notifyUpcomingDueDates() {
		List<Rent> rentsDueIn = libraryService.getRentsDueIn(7);
		Map<String, List<Rent>> userIdToRentsMap = rentsDueIn.stream().collect(
				Collectors.groupingBy(r -> r.getUserId()));
		// notify each user with upcoming dues.

		/*
		 * note that the upcoming and past due cases would be identified by
		 * isDueDatePassed() method in Rent
		 */
	}

}
