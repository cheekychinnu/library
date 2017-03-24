package com.foo.library.repository;

import javax.persistence.EntityManager;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

import org.springframework.stereotype.Component;

import com.foo.library.BeanUtil;
import com.foo.library.model.Book;

@Component
public class LibraryEntityListener {
	
//	@PostPersist
	@PostUpdate
	public void updateBookCatalog(Book book) {
//		EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
//		entityManager.refresh(book);
	}
}
