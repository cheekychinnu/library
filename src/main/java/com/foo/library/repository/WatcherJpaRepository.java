package com.foo.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foo.library.model.Watcher;
import com.foo.library.model.Watcher.WatcherPK;

public interface WatcherJpaRepository extends JpaRepository<Watcher, WatcherPK>{
	List<Watcher> findByIdUserId(String userId);
	List<Watcher> findByBookCatalogName(String bookName);
	List<Watcher> findByBookCatalogId(Long bookCatalogId);
}
