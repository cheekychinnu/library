package com.foo.library.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.foo.library.model.BookCatalog;
import com.foo.library.model.Rent;
import com.foo.library.model.Subscriber;
import com.foo.library.model.Watcher;
import com.foo.library.service.LibraryService;
import com.foo.library.service.NotificationService;
import com.foo.library.service.UserService;

@Component
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private UserService userService;

	@Autowired
	private LibraryService libraryService;

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${newBookCatalog.notification.enable}")
	private Boolean sendNotificationForNewBookCatalog;

	@Override
	public void notifySubscriberForNewAddition(BookCatalog catalog) {
		
		List<Subscriber> subscribersForNewAddition = libraryService
				.getSubscribersForNewAdditions();
		
		List<String> userEmailIdsToNotify = subscribersForNewAddition.stream()
				.map(s -> userService.getUser(s.getUserId()))
				.map(user -> user.getEmailId()).distinct().collect(Collectors.toList());
		
		// TODO : Implement Async here
		if (sendNotificationForNewBookCatalog) {
			userEmailIdsToNotify.add("vinodhinic@gmail.com");
			userEmailIdsToNotify.add("cheekychinnu@gmail.com");
			if (!userEmailIdsToNotify.isEmpty()) {
				String text = "New Book Added to the catalog :"
						+ catalog.getName() + " Author :" + catalog.getAuthor()
						+ " ISBN :" + catalog.getIsbn();
				String subject = "[LIBRARY] NEW BOOK ALERT!";
				
				SimpleMailMessage message = new SimpleMailMessage();
				String[] to = userEmailIdsToNotify.toArray(new String[userEmailIdsToNotify.size()]);
				message.setTo(to);
				message.setSubject(subject);
				message.setText(text);
				javaMailSender.send(message);

				System.out.println("Users notified:" + userEmailIdsToNotify
						+ " for catalog:" + catalog);
			}
		}
	}

	@Override
	public void notifyWatchers(Long bookCatalogId) {
		List<Watcher> watchers = libraryService.getWatchers(bookCatalogId);
		@SuppressWarnings("unused")
		List<String> usersToNotify = watchers.stream()
				.map(w -> w.getId().getUserId()).collect(Collectors.toList());
		// send mail to all the watchers
	}

	@Override
	public void notifyUpcomingDueDates() {
		List<Rent> rentsDueIn = libraryService.getRentsDueIn(7);
		@SuppressWarnings("unused")
		Map<String, List<Rent>> userIdToRentsMap = rentsDueIn.stream().collect(
				Collectors.groupingBy(r -> r.getUserId()));
		// notify each user with upcoming dues.

		/*
		 * note that the upcoming and past due cases would be identified by
		 * isDueDatePassed() method in Rent
		 */
	}

}
