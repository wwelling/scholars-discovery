package edu.tamu.scholars.middleware.auth.controller.exception;

import edu.tamu.scholars.middleware.auth.model.User;
import edu.tamu.scholars.middleware.auth.service.RegistrationService;

/**
 * {@link User} registration exception used by {@link RegistrationService}.
 */
public class RegistrationException extends Exception {

    private static final long serialVersionUID = 7712767291119883628L;

    public RegistrationException(String message) {
        super(message);
    }

}
