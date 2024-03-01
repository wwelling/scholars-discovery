package edu.tamu.scholars.middleware.auth.model.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import edu.tamu.scholars.middleware.auth.model.User;

/**
 * {@link User} JPA repository with Long identifier. Suppress Spring DATA Rest
 * controller endpoints to find user by email or that the email exists in
 * system.
 */
public interface UserRepo extends JpaRepository<User, Long> {

    @RestResource(exported = false)
    public Optional<User> findByEmail(String email);

    @RestResource(exported = false)
    public boolean existsByEmail(String email);

}
