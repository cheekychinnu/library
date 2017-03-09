package com.foo.library.service;

import com.foo.library.model.BookCatalog;

public interface NotificationService {
	void notifySubscriberForNewAddition(BookCatalog catalog);
	void notifyWatchers(Long bookCatalogId);
}
