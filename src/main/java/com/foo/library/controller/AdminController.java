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
import com.foo.library.model.BookCatalog;
import com.foo.library.service.LibraryService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private LibraryService libraryService;

	@Autowired
	private BookCatalogValidator bookCatalogValidator;
	
	@RequestMapping(method = RequestMethod.GET)
	public String showAdminPage(Model model) {
		model.addAttribute("bookCatalog", new BookCatalog());
		return "admin";
	}
	
	@RequestMapping(value = "/addBookCatalog", method = RequestMethod.POST)
	public String addBookCatalog(@ModelAttribute @Valid BookCatalog bookCatalog, BindingResult bindingResult,
			@RequestHeader("referer") String referedFrom,
			RedirectAttributes redirectAttributes) {
		String message = null;
		try {
			List<BookCatalog> searchBookCatalogByBookName = libraryService
					.searchBookCatalogByBookName(bookCatalog.getName());
			if (!CollectionUtils.isEmpty(searchBookCatalogByBookName)) {
				message = "Catalog already present with name "
						+ bookCatalog.getName();
			} else {
				List<BookCatalog> searchBookCatalogByIsbn = libraryService
						.searchBookCatalogByIsbn(bookCatalog.getIsbn());
				if (!CollectionUtils.isEmpty(searchBookCatalogByIsbn)) {
					message = "Catalog already present with ISBN "
							+ bookCatalog.getIsbn();
				} else {
					libraryService.addBookCatalogToLibrary(bookCatalog);
					message = "Successfully added the catalog";
				}
			}

		} catch (Exception e) {
			System.out.println("Exception thrown: "+e.getMessage());
			message = "Error adding the catalog. Try after sometime.";
		} finally {
			redirectAttributes.addFlashAttribute("addBookCatalogMessage",
					message);
		}
		return "redirect:" + referedFrom;
	}
	
	@InitBinder
	private void initBinder(WebDataBinder binder) {
		binder.setValidator(bookCatalogValidator);
	}
}
