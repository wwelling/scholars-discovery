package edu.tamu.scholars.discovery.auth.controller.exception;

/**
 * Custom exception class for handling registration-specific errors. This
 * exception is thrown when errors occur during the user registration process.
 * 
 * <p>
 * This class extends {@link Exception} and provides a mechanism for propagating
 * registration-related error messages through the application.
 * </p>
 */
public class RegistrationException extends Exception {

    /**
     * Serialization version UID for maintaining serialization compatibility.
     * Required for serializable classes.
     */
    private static final long serialVersionUID = 7712767291119883628L;

    /**
     * Constructs a new RegistrationException with the specified detail message.
     *
     * @param message The detail message describing the registration error (which is
     *                saved for later retrieval by the {@link #getMessage()} method)
     */
    public RegistrationException(String message) {
        super(message);
    }

}
