package com.foo.library.service;

import com.foo.library.model.User;

public interface UserService {
	void register(User user);
	User getUser(String userId, String password);
	User getUser(String userId);
}