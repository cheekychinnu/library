package com.foo.library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.foo.library.model.BookCatalog;
import com.foo.library.service.LibraryService;

@Controller("/books")
public class BookController {
	
	@Autowired
	private LibraryService libraryService;
	
	@RequestMapping
	public ModelAndView getAllBooks(){
		List<BookCatalog> allBookCatalog = libraryService.getAllBookCatalogsWithRatingsAndAvailability();
		return new ModelAndView("book", "allBooks", allBookCatalog);
	}
}
