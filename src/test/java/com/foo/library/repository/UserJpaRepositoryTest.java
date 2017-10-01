package com.foo.library.repository;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.foo.library.model.User;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserJpaRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private UserJpaRepository userJpaRepository;

	@Test
	public void testRegister() {
		User user = getUser();
		User savedUser = userJpaRepository.saveAndFlush(user);
		assertNotNull(savedUser);
		assertEquals(user, savedUser);
	}

	@Test
	public void testRegisterMultipleTimes() {
		User user = getUser();
		User savedUser = userJpaRepository.saveAndFlush(user);
		assertNotNull(savedUser);
		assertEquals(user, savedUser);
		savedUser = userJpaRepository.saveAndFlush(user);
		entityManager.clear();
		
		String id = user.getId();
		String password = user.getPassword();
		List<User> findByIdAndPassword = userJpaRepository.queryByIdAndPassword(
				id, password);
		assertNotNull(findByIdAndPassword);
		assertEquals(1, findByIdAndPassword.size());
		assertNull(findByIdAndPassword.get(0).getPassword());
	}

	@Test
	public void testExists() {
		User user = getUser();
		String id = user.getId();
		boolean exists = userJpaRepository.exists(id);
		assertFalse(exists);
		userJpaRepository.saveAndFlush(user);
		exists = userJpaRepository.exists(id);
		assertTrue(exists);
	}

	@Test
	public void testLogin() {
		User user = getUser();

		String id = user.getId();
		String password = user.getPassword();

		userJpaRepository.saveAndFlush(user);
		List<User> findByIdAndPassword = userJpaRepository.queryByIdAndPassword(
				id, password);
		assertNotNull(findByIdAndPassword);
		assertEquals(1, findByIdAndPassword.size());
	}

	private User getUser() {
		String lastName = "Chockalingam";
		String firstName = "Vinodhini";
		String password = "chinnu";
		String emailId = "vinodhinic@gmail.com";
		String id = "chinnu";
		User user = new User(id, emailId, password, firstName, lastName);
		return user;
	}
}
