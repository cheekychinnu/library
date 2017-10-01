package com.foo.library.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.foo.library.model.User;
import com.foo.library.repository.UserJpaRepository;
import com.foo.library.service.UserService;

@Component
public class UserServiceImpl implements UserService{

	@Autowired
	private UserJpaRepository userJpaRepository;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	@Override
	public void register(User user) {
		boolean exists = userJpaRepository.exists(user.getId());
		if(exists) {
			throw new IllegalArgumentException(user.getId()+" already exists");
		}
		User saveAndFlush = userJpaRepository.saveAndFlush(user);
		entityManager.refresh(saveAndFlush);
	}

	@Override
	public User getUser(String userId, String password) {
		List<User> findByIdAndPassword = userJpaRepository.findByIdAndPassword(userId, password);
		if (findByIdAndPassword.size() == 1){
			return findByIdAndPassword.get(0);
		} 
		return null;
	}

}
