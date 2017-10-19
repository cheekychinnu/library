package com.foo.library.service;

import java.util.List;

import com.foo.library.model.Subscriber;
import com.foo.library.model.Watcher;

public interface SubscriptionService {
	
	void subscribeForNewAdditions(String userId);

	List<Subscriber> getSubscribersForNewAdditions();

	void watchForBookCatalog(String userId, Long bookCatalogId);

	List<Watcher> getWatchers(Long bookCatalogId);

	List<Watcher> getWatchers(String userId);
}

