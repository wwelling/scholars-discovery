package edu.tamu.scholars.discovery.auth.service;

import static edu.tamu.scholars.discovery.AppConstants.EMPTY_OBJECT_ARRAY;
import static edu.tamu.scholars.discovery.auth.AuthConstants.CONFIRMATION_EMAIL_SUBJECT_MESSAGE;
import static edu.tamu.scholars.discovery.auth.AuthConstants.EMAIL_ALREADY_CONFIRMED_MESSAGE;
import static edu.tamu.scholars.discovery.auth.AuthConstants.EMAIL_NOT_CONFIRMED_MESSAGE;
import static edu.tamu.scholars.discovery.auth.AuthConstants.TOKEN_EXPIRED_MESSAGE;
import static edu.tamu.scholars.discovery.auth.AuthConstants.UNABLE_TO_COMPLETE_EMAIL_NOT_FOUND_MESSAGE;
import static edu.tamu.scholars.discovery.auth.AuthConstants.UNABLE_TO_CONFIRM_EMAIL_NOT_FOUND_MESSAGE;
import static edu.tamu.scholars.discovery.auth.AuthConstants.USERS_CHANNEL;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.token.Token;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import edu.tamu.scholars.discovery.auth.config.AuthConfig;
import edu.tamu.scholars.discovery.auth.controller.exception.RegistrationException;
import edu.tamu.scholars.discovery.auth.controller.request.Registration;
import edu.tamu.scholars.discovery.auth.model.Role;
import edu.tamu.scholars.discovery.auth.model.User;
import edu.tamu.scholars.discovery.auth.model.repo.UserService;
import edu.tamu.scholars.discovery.messaging.CreateEntityMessage;
import edu.tamu.scholars.discovery.service.EmailService;
import edu.tamu.scholars.discovery.service.TemplateService;

@Service
public class RegistrationService {

    private final AuthConfig authConfig;
    private final UserService userService;
    private final TemplateService templateService;
    private final EmailService emailService;
    private final TokenService tokenService;
    private final MessageSource messageSource;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final SimpMessagingTemplate simpMessageTemplate;

    RegistrationService(
        AuthConfig authConfig,
        UserService userService,
        TemplateService templateService,
        EmailService emailService,
        TokenService tokenService,
        MessageSource messageSource,
        ObjectMapper objectMapper,
        PasswordEncoder passwordEncoder,
        SimpMessagingTemplate simpMessageTemplate
    ) {
        this.authConfig = authConfig;
        this.userService = userService;
        this.templateService = templateService;
        this.emailService = emailService;
        this.tokenService = tokenService;
        this.messageSource = messageSource;
        this.objectMapper = objectMapper;
        this.passwordEncoder = passwordEncoder;
        this.simpMessageTemplate = simpMessageTemplate;
    }

    public Registration submit(Registration registration) throws JsonProcessingException {
        String registrationJson = objectMapper.writeValueAsString(registration);
        Token token = tokenService.allocateToken(registrationJson);
        String subject = messageSource.getMessage(
            CONFIRMATION_EMAIL_SUBJECT_MESSAGE,
            EMPTY_OBJECT_ARRAY,
            LocaleContextHolder.getLocale()
        );
        String message = templateService.templateConfirmRegistrationMessage(registration, token.getKey());
        createUser(registration);
        emailService.send(registration.getEmail(), subject, message);

        return registration;
    }

    public Registration confirm(String key)
        throws IOException, RegistrationException {
        Token token = tokenService.verifyToken(key);
        String registrationJson = token.getExtendedInformation();
        Registration registration = objectMapper.readValue(registrationJson, Registration.class);
        Optional<User> user = userService.findByEmail(registration.getEmail());
        if (!user.isPresent()) {
            throw new RegistrationException(messageSource.getMessage(
                UNABLE_TO_CONFIRM_EMAIL_NOT_FOUND_MESSAGE,
                new Object[] { registration.getEmail() },
                LocaleContextHolder.getLocale()
            ));
        }
        if (isTokenExpired(token)) {
            userService.delete(user.get());
            throw new RegistrationException(messageSource.getMessage(
                TOKEN_EXPIRED_MESSAGE,
                EMPTY_OBJECT_ARRAY,
                LocaleContextHolder.getLocale()
            ));
        }
        if (user.get().isConfirmed()) {
            throw new RegistrationException(messageSource.getMessage(
                EMAIL_ALREADY_CONFIRMED_MESSAGE,
                new Object[] { registration.getEmail() },
                LocaleContextHolder.getLocale()
            ));
        }
        user.get().setConfirmed(true);
        userService.save(user.get());

        return registration;
    }

    public User complete(String key, Registration registration) throws RegistrationException {
        Token token = tokenService.verifyToken(key);
        Optional<User> user = userService.findByEmail(registration.getEmail());
        if (!user.isPresent()) {
            throw new RegistrationException(messageSource.getMessage(
                UNABLE_TO_COMPLETE_EMAIL_NOT_FOUND_MESSAGE,
                new Object[] { registration.getEmail() },
                LocaleContextHolder.getLocale()
            ));
        }
        if (isTokenExpired(token)) {
            userService.delete(user.get());
            throw new RegistrationException(messageSource.getMessage(
                TOKEN_EXPIRED_MESSAGE,
                EMPTY_OBJECT_ARRAY,
                LocaleContextHolder.getLocale()
            ));
        }
        if (!user.get().isConfirmed()) {
            throw new RegistrationException(messageSource.getMessage(
                EMAIL_NOT_CONFIRMED_MESSAGE,
                new Object[] { registration.getEmail() },
                LocaleContextHolder.getLocale()
            ));
        }
        user.get().setEnabled(true);
        user.get().setPassword(passwordEncoder.encode(registration.getPassword()));

        return userService.save(user.get());
    }

    private synchronized void createUser(Registration registration) {
        User user = new User();
        user.setFirstName(registration.getFirstName());
        user.setLastName(registration.getLastName());
        user.setEmail(registration.getEmail());
        if (userService.count() == 0) {
            user.setRole(Role.ROLE_SUPER_ADMIN);
        } else if (userService.count() == 1) {
            user.setRole(Role.ROLE_ADMIN);
        } else if (userService.count() == 2) {
            user.setRole(Role.ROLE_MANAGER);
        } else {
            user.setRole(Role.ROLE_USER);
        }
        user = userService.save(user);
        simpMessageTemplate.convertAndSend(USERS_CHANNEL, new CreateEntityMessage<User>(user));
    }

    private boolean isTokenExpired(Token token) {
        Calendar currentTime = Calendar.getInstance();
        Calendar creationTime = Calendar.getInstance();
        creationTime.setTimeInMillis(token.getKeyCreationTime());

        return ChronoUnit.DAYS.between(
            creationTime.toInstant(),
            currentTime.toInstant()
        ) >= authConfig.getRegistrationTokenDuration();
    }

}
