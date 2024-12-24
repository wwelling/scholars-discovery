package edu.tamu.scholars.discovery.auth.controller.advice;

import static edu.tamu.scholars.discovery.AppConstants.EMPTY_OBJECT_ARRAY;
import static edu.tamu.scholars.discovery.auth.AuthConstants.METHOD_ARGUMENT_NOTVALID_EXCEPTION_MESSAGE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.scholars.discovery.auth.controller.RegistrationController;

/**
 * Controller advice for handling exceptions in the registration process. This
 * class provides centralized exception handling for the
 * {@link RegistrationController}.
 *
 * @see RegistrationController
 * @see ControllerAdvice
 * @see RestController
 */
@RestController
@ControllerAdvice(assignableTypes = { RegistrationController.class })
public class RegistrationAdvice {

    /** Logger for this class */
    private static final Logger logger = LoggerFactory.getLogger(RegistrationAdvice.class);

    /** Message source for i18n support */
    private final MessageSource messageSource;

    /**
     * Constructs a new RegistrationAdvice with the specified message source.
     *
     * @param messageSource The message source for internationalization support
     */
    RegistrationAdvice(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Handles validation exceptions that occur during method argument validation.
     * Returns the first validation error message and logs the full exception
     * details.
     *
     * @param exception The validation exception that was thrown
     * @return The default message from the first validation error
     * @see MethodArgumentNotValidException
     * @see ResponseStatus
     */
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String message = messageSource.getMessage(
                METHOD_ARGUMENT_NOTVALID_EXCEPTION_MESSAGE,
                EMPTY_OBJECT_ARRAY,
                LocaleContextHolder.getLocale());
        logger.error(message, exception);

        return exception.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();
    }

    /**
     * Generic exception handler for all other exceptions not specifically handled.
     * Returns the exception message directly.
     *
     * @param exception The exception that was thrown
     * @return The exception message
     * @see Exception
     * @see ResponseStatus
     */
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public String handleException(Exception exception) {
        return exception.getMessage();
    }

}
