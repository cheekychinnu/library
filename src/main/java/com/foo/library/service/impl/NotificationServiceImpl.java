package com.foo.library.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.foo.library.model.BookCatalog;
import com.foo.library.model.Subscriber;
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
		// TODO
	}

	@Override
	public void notifyUpcomingDueDates() {
		// TODO
	}

}
