package edu.tamu.scholars.discovery.export.service;

import java.util.List;

import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import reactor.core.publisher.Flux;

import edu.tamu.scholars.discovery.discovery.model.Individual;
import edu.tamu.scholars.discovery.export.argument.ExportArg;
import edu.tamu.scholars.discovery.export.exception.UnsupportedExporterTypeException;

/**
 * 
 */
public interface Exporter {

    public String type();

    public String contentDisposition(String filename);

    public String contentType();

    public default StreamingResponseBody streamIndividuals(Flux<Individual> cursor, List<ExportArg> export) {
        throw new UnsupportedExporterTypeException(String.format(
            "%s exporter does not support export field exports",
            type()
        ));
    }

    public default StreamingResponseBody streamIndividual(Individual individual, String name) {
        throw new UnsupportedExporterTypeException(String.format(
            "%s exporter does not support individual templated exports",
            type()
        ));
    }

}
