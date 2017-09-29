package com.foo.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foo.library.model.User;

public interface UserJpaRepository  extends JpaRepository<User, String>{
	List<User> findByIdAndPassword(String id, String password);
	boolean exists(String id);
}
