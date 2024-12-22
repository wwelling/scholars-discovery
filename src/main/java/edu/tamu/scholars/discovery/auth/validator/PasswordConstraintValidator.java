package edu.tamu.scholars.discovery.auth.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.HistoryRule;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordData.HistoricalReference;
import org.passay.PasswordData.Reference;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;
import org.passay.spring.SpringMessageResolver;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import edu.tamu.scholars.discovery.auth.annotation.ValidPassword;
import edu.tamu.scholars.discovery.auth.config.PasswordConfig;
import edu.tamu.scholars.discovery.auth.controller.request.Registration;
import edu.tamu.scholars.discovery.auth.model.User;
import edu.tamu.scholars.discovery.auth.model.repo.UserService;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, Registration> {

    private UserService userService;
    private MessageSource messageSource;
    private PasswordConfig passwordConfig;

    PasswordConstraintValidator(
        UserService userService,
        MessageSource messageSource,
        PasswordConfig passwordConfig
    ) {
        this.userService = userService;
        this.messageSource = messageSource;
        this.passwordConfig = passwordConfig;
    }

    @Override
    public boolean isValid(Registration registration, ConstraintValidatorContext context) {
        String email = registration.getEmail();
        String password = registration.getPassword();

        PasswordValidator validator = new PasswordValidator(
                // use spring message resolver
                new SpringMessageResolver(messageSource),

                // length between PASSWORD_MIN_LENGTH and PASSWORD_MAX_LENGTH characters
                new LengthRule(passwordConfig.getMinLength(), passwordConfig.getMaxLength()),

                // at least one upper-case character
                new CharacterRule(EnglishCharacterData.UpperCase, 1),

                // at least one lower-case character
                new CharacterRule(EnglishCharacterData.LowerCase, 1),

                // at least one digit character
                new CharacterRule(EnglishCharacterData.Digit, 1),

                // at least one symbol (special character)
                new CharacterRule(EnglishCharacterData.Special, 1),

                // not previously used
                new HistoryRule(),

                // no whitespace
                new WhitespaceRule());

        PasswordData passwordData = new PasswordData(password);

        Optional<User> user = userService.findByEmail(email);

        List<Reference> passwordReferences;

        if (user.isPresent()) {
            passwordReferences = user.get()
                .getOldPasswords()
                .stream()
                .map(HistoricalReference::new)
                .collect(Collectors.toList());
        } else {
            passwordReferences = new ArrayList<>();
        }

        passwordData.setPasswordReferences(passwordReferences);

        if (password.equals(registration.getConfirm())) {
            RuleResult result = validator.validate(passwordData);
            if (result.isValid()) {
                return true;
            }
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(String.join(", ", validator.getMessages(result)))
                .addConstraintViolation();

            return false;
        }
        context.disableDefaultConstraintViolation();

        context.buildConstraintViolationWithTemplate(messageSource.getMessage(
            "Registration.passwordsDoNotMatch",
            new Object[0],
            LocaleContextHolder.getLocale()
        )).addConstraintViolation();

        return false;
    }

}
