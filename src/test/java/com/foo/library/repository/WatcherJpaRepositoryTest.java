package com.foo.library.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.foo.library.model.BookCatalog;
import com.foo.library.model.Watcher;
import com.foo.library.model.Watcher.WatcherPK;

@RunWith(SpringRunner.class)
@DataJpaTest
public class WatcherJpaRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private WatcherJpaRepository watcherJpaRepository;

	@Autowired
	private BookCatalogJpaRepository bookCatalogJpaRepository;

	@Test
	public void testCreate() {
		String userId = "vino";
		BookCatalog bookCatalog = createTestBookCatalog();
		WatcherPK watcherPK = new WatcherPK(userId, bookCatalog.getId());
		
		Watcher watcher = new Watcher();
		watcher.setId(watcherPK);
		
		watcherJpaRepository.saveAndFlush(watcher);
		
		userId = "chinnu";
		watcherPK = new WatcherPK(userId, bookCatalog.getId());
		Watcher watcher2 = new Watcher();
		watcher2.setId(watcherPK);
		watcherJpaRepository.saveAndFlush(watcher2);
		
		entityManager.clear();
		
		List<Watcher> findAll = watcherJpaRepository.findAll();
		assertNotNull(findAll);
		assertEquals(2, findAll.size());
		assertTrue(findAll.contains(watcher));
		assertTrue(findAll.contains(watcher2));
	}
	
	@Test
	public void testFindByUserId()
	{
		String userId = "vino";
		BookCatalog bookCatalog = createTestBookCatalog();
		WatcherPK watcherPK = new WatcherPK(userId, bookCatalog.getId());
		
		Watcher watcher = new Watcher();
		watcher.setId(watcherPK);
		
		watcherJpaRepository.saveAndFlush(watcher);
		
		userId = "chinnu";
		watcherPK = new WatcherPK(userId, bookCatalog.getId());
		Watcher watcher2 = new Watcher();
		watcher2.setId(watcherPK);
		watcherJpaRepository.saveAndFlush(watcher2);
		
		entityManager.clear();
		
		List<Watcher> findByUserId = watcherJpaRepository.findByIdUserId(userId);
		assertNotNull(findByUserId);
		assertEquals(1, findByUserId.size());
		assertEquals(watcher2,findByUserId.get(0));
		assertEquals(bookCatalog, findByUserId.get(0).getBookCatalog());
	}
	
	@Test
	public void testFindByBookCatalog()
	{
		String userId = "vino";
		BookCatalog bookCatalog = createTestBookCatalog();
		WatcherPK watcherPK = new WatcherPK(userId, bookCatalog.getId());
		
		Watcher watcher = new Watcher();
		watcher.setId(watcherPK);
		
		watcherJpaRepository.saveAndFlush(watcher);
		
		userId = "chinnu";
		watcherPK = new WatcherPK(userId, bookCatalog.getId());
		Watcher watcher2 = new Watcher();
		watcher2.setId(watcherPK);
		watcherJpaRepository.saveAndFlush(watcher2);
		
		entityManager.clear();
		
		List<Watcher> findBy = watcherJpaRepository.findByBookCatalogId(bookCatalog.getId());
		assertNotNull(findBy);
		assertEquals(2, findBy.size());
		assertTrue(findBy.contains(watcher));
		assertTrue(findBy.contains(watcher2));
		findBy.forEach(w->{
			assertEquals(bookCatalog, w.getBookCatalog());
		});
		
		findBy = watcherJpaRepository.findByBookCatalogName(bookCatalog.getName());
		assertNotNull(findBy);
		assertEquals(2, findBy.size());
		assertTrue(findBy.contains(watcher));
		assertTrue(findBy.contains(watcher2));
		findBy.forEach(w->{
			assertEquals(bookCatalog, w.getBookCatalog());
		});
		
	}
	
	private BookCatalog createTestBookCatalog() {
		String author = "J.K.Rowling";
		String isbn = "129847874";
		String bookName = "Harry Potter And The Prisoner Of Azkhaban";
		BookCatalog bookCatalog = new BookCatalog(bookName, author, isbn);
		return bookCatalogJpaRepository.saveAndFlush(bookCatalog);
	}
}