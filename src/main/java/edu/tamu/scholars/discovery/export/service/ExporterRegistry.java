package edu.tamu.scholars.discovery.export.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import edu.tamu.scholars.discovery.export.exception.UnknownExporterTypeException;

@Service
public class ExporterRegistry {

    private final List<Exporter> exporters;

    ExporterRegistry(List<Exporter> exporters) {
        this.exporters = exporters;
    }

    public Exporter getExporter(String type) throws UnknownExporterTypeException {
        Optional<Exporter> exporter = exporters.stream().filter(e -> e.type().equals(type)).findAny();
        if (exporter.isPresent()) {
            return exporter.get();
        }
        throw new UnknownExporterTypeException(String.format("Could not find exporter of type %s", type));
    }

}
