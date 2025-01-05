package edu.tamu.scholars.discovery.controller.advice;

import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class DiscoveryControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(DiscoveryControllerAdvice.class);

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = FileNotFoundException.class)
    public @ResponseBody String handleFileNotFoundException(FileNotFoundException exception) {
        logger.error(exception.getMessage(), exception);
        return exception.getMessage();
    }

}
