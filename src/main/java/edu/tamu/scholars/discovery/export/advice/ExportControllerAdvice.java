package edu.tamu.scholars.discovery.export.advice;

import jakarta.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import edu.tamu.scholars.discovery.export.exception.ExportException;
import edu.tamu.scholars.discovery.export.exception.ExportQueryParameterRequiredException;
import edu.tamu.scholars.discovery.export.exception.UnauthorizedExportException;
import edu.tamu.scholars.discovery.export.exception.UnknownExporterTypeException;
import edu.tamu.scholars.discovery.export.exception.UnsupportedExporterTypeException;

@ControllerAdvice
public class ExportControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExportControllerAdvice.class);

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = UnknownExporterTypeException.class)
    public @ResponseBody String handleUnknownExporterTypeException(UnknownExporterTypeException exception) {
        return exception.getMessage();
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = EntityNotFoundException.class)
    public @ResponseBody String handleEntityNotFoundException(EntityNotFoundException exception) {
        return exception.getMessage();
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = UnsupportedExporterTypeException.class)
    public @ResponseBody String handleUnsupportedExporterTypeException(UnsupportedExporterTypeException exception) {
        return exception.getMessage();
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = ExportQueryParameterRequiredException.class)
    public @ResponseBody String handleExportQueryParameterRequiredException(
        ExportQueryParameterRequiredException exception
    ) {
        return exception.getMessage();
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = UnauthorizedExportException.class)
    public @ResponseBody String handleUnauthorizedExportException(UnauthorizedExportException exception) {
        return exception.getMessage();
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = ExportException.class)
    public @ResponseBody String handleExportException(ExportException exception) {
        logger.error("Failed to export", exception);
        return exception.getMessage();
    }

}
