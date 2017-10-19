package com.foo.library.controller.validator;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.foo.library.model.User;

@Component
public class UserValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		try {
			User user = (User) target;
			String userId = user.getId();
			if (userId == null || !StringUtils.hasLength(userId.trim())) {
				errors.rejectValue("id", "required", "userId cannot be empty");
			} else if (!userId.equals("admin") && userId.trim().length() < 6) {
				errors.rejectValue("id", "too short",
						"userId should be of minimum length 6");
			}
			String password = user.getPassword();
			if (password == null || !StringUtils.hasLength(password)) {
				errors.rejectValue("password", "required",
						"password cannot be empty");
			}

			else if (password.trim().length() < 6) {
				errors.rejectValue("password", "too short",
						"Please enter at least 6 characters");
			}

			String email = user.getEmailId();
			if (email != null) {
				Pattern pattern = Pattern.compile(
						"^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
						Pattern.CASE_INSENSITIVE);
				if (!(pattern.matcher(email).matches())) {
					errors.rejectValue("emailId", "invalid",
							"Please enter a valid Email Id");
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
