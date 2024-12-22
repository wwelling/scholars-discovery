package edu.tamu.scholars.discovery.auth.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import edu.tamu.scholars.discovery.auth.annotation.AvailableEmail;
import edu.tamu.scholars.discovery.auth.model.repo.UserService;

public class EmailConstraintValidator implements ConstraintValidator<AvailableEmail, String> {

    private final UserService userService;

    public EmailConstraintValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return !userService.existsByEmail(email);
    }

}
