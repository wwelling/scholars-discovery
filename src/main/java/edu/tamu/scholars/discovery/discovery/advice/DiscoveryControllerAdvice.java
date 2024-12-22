package edu.tamu.scholars.discovery.discovery.advice;

import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import edu.tamu.scholars.discovery.discovery.exception.InvalidValuePathException;
import edu.tamu.scholars.discovery.discovery.exception.SolrRequestException;

/**
 * 
 */
@ControllerAdvice
public class DiscoveryControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(DiscoveryControllerAdvice.class);

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = InvalidValuePathException.class)
    public @ResponseBody String handleInvalidValuePathException(InvalidValuePathException exception) {
        logger.warn(exception.getMessage(), exception);
        return exception.getMessage();
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = SolrRequestException.class)
    public @ResponseBody String handleSolrRequestException(SolrRequestException exception) {
        logger.error(exception.getMessage(), exception);
        return exception.getMessage();
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = FileNotFoundException.class)
    public @ResponseBody String handleFileNotFoundException(FileNotFoundException exception) {
        logger.error(exception.getMessage(), exception);
        return exception.getMessage();
    }

}