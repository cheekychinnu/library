package com.foo.library.repository;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.foo.library.model.EventType;
import com.foo.library.model.Subscriber;
import com.foo.library.model.Subscriber.SubscriberPK;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SubscriberJpaRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private SubscriberJpaRepository subscriberJpaRepository;

	@Test
	public void testCreate() {
		EventType eventType = EventType.NEW_BOOK_CATALOG;
		String userId = "vino";
		Subscriber subscriber = new Subscriber(userId, eventType);
		subscriberJpaRepository.saveAndFlush(subscriber);
		
		SubscriberPK subscriberPk = new SubscriberPK();
		subscriberPk.setUserId(userId);
		subscriberPk.setEventType(eventType);
		entityManager.clear();
		Subscriber subscriber2 = subscriberJpaRepository.getOne(subscriberPk);
		assertEquals(subscriber, subscriber2);
	}

	@Test
	public void testFindByEventType() {
		EventType eventType = EventType.NEW_BOOK_CATALOG;
		String userId = "vino";
		Subscriber subscriber = new Subscriber(userId, eventType);
		subscriberJpaRepository.saveAndFlush(subscriber);

		List<Subscriber> findByEventType = subscriberJpaRepository
				.findByEventType(EventType.NEW_BOOK_CATALOG);
		assertNotNull(findByEventType);
		assertEquals(1, findByEventType.size());
		assertEquals(subscriber, findByEventType.get(0));
	}
}
