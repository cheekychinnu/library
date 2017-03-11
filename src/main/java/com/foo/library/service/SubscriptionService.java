package com.foo.library.service;

import java.util.List;

import com.foo.library.model.Subscriber;

public interface SubscriptionService {
	
	void subscribeForNewAdditions(String userId);

	List<Subscriber> getSubscribersForNewAdditions();

}
