package com.foo.library.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.foo.library.model.BookCatalog;
import com.foo.library.service.LibraryService;

@Component
public class BookCatalogValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return BookCatalog.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		try {
			BookCatalog bookCatalog = (BookCatalog) target;
			String name = bookCatalog.getName();
			if (name == null || !StringUtils.hasLength(name.trim())) {
				errors.rejectValue("name", "required", "name cannot be empty");
			}
			
			String isbn = bookCatalog.getIsbn();
			if (isbn == null || !StringUtils.hasLength(isbn.trim())) {
				errors.rejectValue("isbn", "required", "isbn cannot be empty");
			}
			
			String author = bookCatalog.getAuthor();
			if(author ==null || !StringUtils.hasLength(author.trim())) {
				errors.rejectValue("author", "required", "author cannot be empty");
			} 
		} catch (Exception e) {
			System.out.println(e);
		}
	}


}
