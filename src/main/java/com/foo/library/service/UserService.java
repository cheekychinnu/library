package com.foo.library.service;

import com.foo.library.model.User;

public interface UserService {
	void register(User user);
	boolean isValidLogin(String userId, String password);
}
