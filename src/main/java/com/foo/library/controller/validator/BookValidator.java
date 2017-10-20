package com.foo.library.controller.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.foo.library.model.Book;

@Component
public class BookValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Book.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		try {
			Book book = (Book) target;
			if(book.getBookCatalog() == null || book.getBookCatalog().getId() == null) {
				errors.rejectValue("bookCatalog", "required", "book catalog cannot be empty");
			}
			
			String provider = book.getProvider();
			if (provider == null || !StringUtils.hasLength(provider.trim())) {
				errors.rejectValue("provider", "required", "provider cannot be empty");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
