package com.foo.library.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.foo.library.controller.validator.BookCatalogValidator;
import com.foo.library.controller.validator.BookValidator;
import com.foo.library.model.Book;
import com.foo.library.model.BookCatalog;
import com.foo.library.service.LibraryService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private LibraryService libraryService;

	@Autowired
	private BookCatalogValidator bookCatalogValidator;

	@ModelAttribute("bookCatalog")
	public BookCatalog getBookCatalog() {
		return new BookCatalog();
	}
	
	@ModelAttribute("book") 
	public Book getBook(){
		return new Book();
	}
	
	@ModelAttribute("allBookCatalogs")
	public List<BookCatalog> getAllBookCatalogs(){
		List<BookCatalog> allBookCatalogs = libraryService.getAllBookCatalogs();
		return allBookCatalogs;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String showAdminPage(Model model) {
		return "admin";
	}

	@RequestMapping(value = "/bookCatalog/create", method = RequestMethod.POST)
	public String addBookCatalog(
			@ModelAttribute("bookCatalog") @Valid BookCatalog bookCatalog,
			BindingResult bindingResult, Model model,
			@RequestHeader("referer") String referedFrom,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			return "admin";
		}
		try {
			List<BookCatalog> searchBookCatalogByBookName = libraryService
					.searchBookCatalogByBookName(bookCatalog.getName());
			if (!CollectionUtils.isEmpty(searchBookCatalogByBookName)) {
				model.addAttribute(
						"addBookCatalogMessage",
						"Catalog already present with name "
								+ bookCatalog.getName());
			} else {
				List<BookCatalog> searchBookCatalogByIsbn = libraryService
						.searchBookCatalogByIsbn(bookCatalog.getIsbn());
				if (!CollectionUtils.isEmpty(searchBookCatalogByIsbn)) {
					model.addAttribute(
							"addBookCatalogMessage",
							"Catalog already present with ISBN "
									+ bookCatalog.getIsbn());
				} else {
					libraryService.addBookCatalogToLibrary(bookCatalog);
					redirectAttributes.addFlashAttribute(
							"addBookCatalogMessage",
							"Successfully added the catalog");
					return "redirect:" + "/admin";
				}
			}

		} catch (Exception e) {
			System.out.println("Exception thrown: " + e.getMessage());
			model.addAttribute("addBookCatalogMessage",
					"Error adding the catalog. Try after sometime.");
		}
		return "admin";
	}

	@InitBinder("bookCatalog")
	private void initBookCatalogBinder(WebDataBinder binder) {
		binder.setValidator(bookCatalogValidator);
	}
	
	@Autowired
	private BookValidator bookValidator;

	@RequestMapping(value = "/book/create", method = RequestMethod.POST)
	public String addBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult, Model model,
			@RequestHeader("referer") String referedFrom,
			RedirectAttributes redirectAttributes) {
		
		System.out.println("Referer here ;"+referedFrom);
		
		if(bindingResult.hasErrors()) {
			return "admin";
		}
		try {
			book.setIsActive(true);
			book.setIsAvailable(true);
			libraryService.addBookToTheCatalog(book.getBookCatalog().getId(),
					book);
			redirectAttributes.addFlashAttribute("addBookMessage",
					"Successfully added to the catalog");
			return "redirect:" + "/admin";
		} catch (Exception e) {
			System.out.println("Exception thrown: "+e.getMessage());
			model.addAttribute("addBookMessage",
					"Error adding book to the catalog. Try after sometime.");
			return "admin";
		} 
	}
	
	@InitBinder("book")
	private void initBookBinder(WebDataBinder binder) {
		binder.setValidator(bookValidator);
	}

}
