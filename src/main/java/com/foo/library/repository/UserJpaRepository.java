package com.foo.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.foo.library.model.User;

public interface UserJpaRepository  extends JpaRepository<User, String>{
	@Query("select new User(u.id, u.emailId, u.firstName, u.lastName) from User u "
			+ "where u.id = :id and u.password = :password")
	List<User> queryByIdAndPassword(@Param("id") String id, @Param("password")String password);
	boolean existsById(String id);
}
