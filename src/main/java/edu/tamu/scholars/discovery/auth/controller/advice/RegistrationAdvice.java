package edu.tamu.scholars.discovery.auth.controller.advice;

import static edu.tamu.scholars.discovery.AppConstants.EMPTY_OBJECT_ARRAY;
import static edu.tamu.scholars.discovery.auth.AuthConstants.METHOD_ARGUMENT_NOTVALID_EXCEPTION_MESSAGE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@ControllerAdvice(assignableTypes = { RegistrationController.class })
public class RegistrationAdvice {

    private final MessageSource messageSource;

    RegistrationAdvice(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String message = messageSource.getMessage(
                METHOD_ARGUMENT_NOTVALID_EXCEPTION_MESSAGE,
                EMPTY_OBJECT_ARRAY,
                LocaleContextHolder.getLocale());
        log.error(message, exception);

        return exception.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public String handleException(Exception exception) {
        return exception.getMessage();
    }

}
