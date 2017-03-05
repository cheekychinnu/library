package com.foo.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.foo.library.model.EventType;
import com.foo.library.model.Subscriber;

@Repository
public interface SubscriberJpaRepository extends JpaRepository<Subscriber, Subscriber.SubscriberPK>{
	
	List<Subscriber> findByEventType(EventType eventType);
	
}
