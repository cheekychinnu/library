package com.foo.library.controller;

import java.util.Optional;

import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.foo.library.service.exception.BookCatalogNotFoundException;

@ControllerAdvice(annotations={RestController.class}, assignableTypes=RestRatingAndReviewController.class)
public class RestControllerAdvice {
	 private final MediaType vndErrorMediaType = MediaType.parseMediaType("application/vnd.error");

	    @ExceptionHandler(BookCatalogNotFoundException.class)
	    ResponseEntity<VndErrors> bookCatalogNotFoundException(BookCatalogNotFoundException e) {
	        return error(e, HttpStatus.NOT_FOUND, e.getBookCatalogId() + "");
	    }

	    private <E extends Exception> ResponseEntity<VndErrors> error(E e, HttpStatus httpStatus, String logref) {
	        String msg = Optional.of(e.getMessage()).orElse(e.getClass().getSimpleName());
	        HttpHeaders httpHeaders = new HttpHeaders();
	        httpHeaders.setContentType(this.vndErrorMediaType);
	        return new ResponseEntity<>(new VndErrors(logref, msg), httpHeaders, httpStatus);
	    }
}
