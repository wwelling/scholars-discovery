package edu.tamu.scholars.discovery.auth.service;

import static edu.tamu.scholars.discovery.auth.AuthConstants.EMAIL_NOT_FOUND_MESSAGE;

import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.tamu.scholars.discovery.auth.config.PasswordConfig;
import edu.tamu.scholars.discovery.auth.details.CustomUserDetails;
import edu.tamu.scholars.discovery.auth.model.User;
import edu.tamu.scholars.discovery.auth.model.repo.UserService;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserService userService;
    private MessageSource messageSource;
    private PasswordConfig passwordConfig;

    CustomUserDetailsService(
        UserService userService,
        MessageSource messageSource,
        PasswordConfig passwordConfig
    ) {
        this.userService = userService;
        this.messageSource = messageSource;
        this.passwordConfig = passwordConfig;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userService.findByEmail(username);
        if (!user.isPresent()) {
            String message = messageSource.getMessage(
            EMAIL_NOT_FOUND_MESSAGE,
            new Object[] { username },
            LocaleContextHolder.getLocale());
            throw new UsernameNotFoundException(message);
        }

        return new CustomUserDetails(user.get(), passwordConfig);
    }

}
