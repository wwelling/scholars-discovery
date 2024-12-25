package edu.tamu.scholars.discovery.etl.extract;

import java.util.Map;

import reactor.core.publisher.Flux;

import edu.tamu.scholars.discovery.etl.DataProcessor;

/**
 * Defines the contract for extracting data in a reactive manner as part of the
 * ETL process. This interface extends {@link DataProcessor} to provide specific
 * extraction capabilities while maintaining the standard ETL processing
 * lifecycle.
 * 
 * <p>
 * The DataExtractor interface represents the initial stage of the ETL process,
 * where data is sourced from various systems. It supports both bulk extraction
 * through reactive streams and individual subject-based extraction for targeted
 * data retrieval.
 * </p>
 * 
 * <p>
 * The interface utilizes Project Reactor's {@link Flux} for handling
 * potentially large datasets in a non-blocking, backpressure-aware manner. This
 * makes it suitable for scenarios where:
 * </p>
 * 
 * <ul>
 *   <li>Large volumes of data need to be processed</li>
 *   <li>Data arrives asynchronously from various sources</li>
 *   <li>Memory efficiency is crucial</li>
 *   <li>Non-blocking operation is required</li>
 * </ul>
 * 
 * <p>
 * Typical implementation considerations include:
 * </p>
 * 
 * <ul>
 *   <li>Connection management to data sources</li>
 *   <li>Error handling and retry strategies</li>
 *   <li>Resource cleanup</li>
 *   <li>Rate limiting and backpressure handling</li>
 * </ul>
 * 
 * @since 1.5.0
 * @see DataProcessor
 * @see reactor.core.publisher.Flux
 * @see java.util.Map
 */
public interface DataExtractor extends DataProcessor {

    /**
     * Extracts data from the source system as a reactive stream.
     * 
     * <p>
     * This method provides a non-blocking way to extract potentially large amounts
     * of data. The returned Flux enables downstream operators to process data as it
     * becomes available, with built-in backpressure handling.
     * </p>
     * 
     * <p>
     * The extraction process should handle:
     * </p>
     * 
     * <ul>
     *   <li>Efficient streaming of large datasets</li>
     *   <li>Connection lifecycle management</li>
     *   <li>Error recovery and resilience</li>
     *   <li>Resource cleanup through appropriate disposal</li>
     * </ul>
     *
     * @return a Flux emitting maps where keys represent field names and values
     *         represent the corresponding data values
     * @throws RuntimeException if the extraction process encounters an
     *                          unrecoverable error
     */
    public Flux<Map<String, Object>> extract();

    /**
     * Extracts data for a specific subject from the source system.
     * 
     * <p>
     * This method provides targeted extraction capabilities for scenarios where
     * only specific subject data is needed. It performs a synchronous extraction
     * operation for a single subject identifier.
     * </p>
     * 
     * <p>
     * Implementation considerations should include:
     * </p>
     * 
     * <ul>
     *   <li>Efficient single-subject lookup</li>
     *   <li>Proper error handling for missing subjects</li>
     *   <li>Validation of subject identifiers</li>
     *   <li>Complete data retrieval for the subject</li>
     * </ul>
     *
     * @param subject the identifier of the specific subject to extract
     * @return a map containing the extracted data where keys represent field names
     *         and values represent the corresponding data values
     * @throws IllegalArgumentException if the subject identifier is invalid
     * @throws RuntimeException         if the extraction process fails
     */
    public Map<String, Object> extract(String subject);

}