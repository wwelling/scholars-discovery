package edu.tamu.scholars.discovery.auth.service;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

/**
 * {@link User} registration service for processing registration requests.
 */
@Service
public class RegistrationService {

    private final AuthConfig authConfig;
    private final UserService userService;
    private final TemplateService templateService;
    private final EmailService emailService;
    private final TokenService tokenService;
    private final MessageSource messageSource;
    private final ObjectMapper objectMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final SimpMessagingTemplate simpMessageTemplate;

    RegistrationService(
        AuthConfig authConfig,
        UserService userService,
        TemplateService templateService,
        EmailService emailService,
        TokenService tokenService,
        MessageSource messageSource,
        ObjectMapper objectMapper,
        BCryptPasswordEncoder passwordEncoder,
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
            "RegistrationService.confirmationEmailSubject",
            new Object[0],
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
        if (user.isPresent()) {
            if (!isTokenExpired(token)) {
                if (!user.get().isConfirmed()) {
                    user.get().setConfirmed(true);
                    userService.save(user.get());
                    return registration;
                }
                throw new RegistrationException(messageSource.getMessage(
                    "RegistrationService.emailAlreadyConfirmed",
                    new Object[] { registration.getEmail() },
                    LocaleContextHolder.getLocale()
                ));
            }
            userService.delete(user.get());
            throw new RegistrationException(messageSource.getMessage(
                "RegistrationService.tokenExpired",
                new Object[0],
                LocaleContextHolder.getLocale()
            ));
        }
        throw new RegistrationException(messageSource.getMessage(
            "RegistrationService.unableToConfirmEmailNotFound",
            new Object[] { registration.getEmail() },
            LocaleContextHolder.getLocale()
        ));
    }

    public User complete(String key, Registration registration) throws RegistrationException {
        Token token = tokenService.verifyToken(key);
        Optional<User> user = userService.findByEmail(registration.getEmail());
        if (user.isPresent()) {
            if (!isTokenExpired(token)) {
                if (user.get().isConfirmed()) {
                    user.get().setEnabled(true);
                    user.get().setPassword(passwordEncoder.encode(registration.getPassword()));
                    return userService.save(user.get());
                }
                throw new RegistrationException(messageSource.getMessage(
                    "RegistrationService.emailNotConfirmed",
                    new Object[] { registration.getEmail() },
                    LocaleContextHolder.getLocale()
                ));
            }
            userService.delete(user.get());
            throw new RegistrationException(messageSource.getMessage(
                "RegistrationService.tokenExpired",
                new Object[0],
                LocaleContextHolder.getLocale()
            ));
        }
        throw new RegistrationException(messageSource.getMessage(
            "RegistrationService.unableToCompleteEmailNotFound",
            new Object[] { registration.getEmail() },
            LocaleContextHolder.getLocale()
        ));
    }

    private synchronized void createUser(Registration registration) {
        User user = new User(registration.getFirstName(), registration.getLastName(), registration.getEmail());
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
