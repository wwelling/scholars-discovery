package edu.tamu.scholars.middleware.auth.service;

import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.tamu.scholars.middleware.auth.config.PasswordConfig;
import edu.tamu.scholars.middleware.auth.details.CustomUserDetails;
import edu.tamu.scholars.middleware.auth.model.User;
import edu.tamu.scholars.middleware.auth.model.repo.UserRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepo userRepo;
    private MessageSource messageSource;
    private PasswordConfig passwordConfig;

    CustomUserDetailsService(
        UserRepo userRepo,
        MessageSource messageSource,
        PasswordConfig passwordConfig
    ) {
        this.userRepo = userRepo;
        this.messageSource = messageSource;
        this.passwordConfig = passwordConfig;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByEmail(username);
        if (user.isPresent()) {
            return new CustomUserDetails(user.get(), passwordConfig);
        }
        throw new UsernameNotFoundException(messageSource.getMessage(
                "CustomUserDetailsService.emailNotFound",
                new Object[] { username },
                LocaleContextHolder.getLocale()));
    }

}
