package edu.tamu.scholars.middleware.auth.controller.advice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.scholars.middleware.auth.controller.RegistrationController;
import edu.tamu.scholars.middleware.auth.model.User;

/**
 * {@link User} registration controller advice to handle bad requests.
 */
@RestController
@ControllerAdvice(assignableTypes = { RegistrationController.class })
public class RegistrationAdvice {

    // TODO: add logging
    // TODO: handle registration exception and other exceptions thrown by the RegistrationController

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public @ResponseBody String handleMethodArgumentNotValidException(
        MethodArgumentNotValidException exception
    ) {
        return exception.getBindingResult()
            .getAllErrors()
            .get(0)
            .getDefaultMessage();
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public @ResponseBody String handleException(Exception exception) {
        return exception.getMessage();
    }

}
