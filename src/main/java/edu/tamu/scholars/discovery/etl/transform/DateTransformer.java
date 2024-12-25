package edu.tamu.scholars.discovery.etl.transform;

import edu.tamu.scholars.discovery.etl.DataProcessor;

/**
 * Defines a specialized data processor for transforming date-related data
 * between different formats or representations. This interface extends
 * {@link DataProcessor} to provide date-specific transformation capabilities
 * while maintaining the standard ETL processing lifecycle.
 * 
 * <p>
 * The DateTransformer interface uses generic types to allow for flexible input
 * and output data types, making it suitable for various date transformation
 * scenarios such as string-to-date conversions, format changes, or timezone
 * adjustments.
 * </p>
 * 
 * <p>
 * Typical use cases include:
 * </p>
 * 
 * <ul>
 *   <li>Converting string dates to standardized date formats</li>
 *   <li>Normalizing dates across different time zones</li>
 *   <li>Transforming between different date representations (e.g., ISO to custom format)</li>
 *   <li>Handling date-specific data cleansing and validation</li>
 * </ul>
 *
 * @param <I> the input data type to be transformed
 * @param <O> the output data type after transformation
 * 
 * @since 1.5.0
 * @see DataProcessor
 * @see java.time.temporal.Temporal
 * @see java.util.Date
 */
public interface DateTransformer<I, O> extends DataProcessor {

    /**
     * Transforms the input date data into the specified output format.
     * 
     * <p>
     * This method should handle the actual transformation of date data, including
     * any necessary parsing, formatting, or validation steps. Implementations
     * should ensure proper handling of:
     * </p>
     * 
     * <ul>
     *   <li>Invalid date formats</li>
     *   <li>Null or empty input values</li>
     *   <li>Time zone conversions if applicable</li>
     *   <li>Format-specific requirements</li>
     * </ul>
     * 
     * <p>
     * The transformation process should maintain data integrity and handle edge
     * cases appropriately. If the transformation cannot be performed,
     * implementations should throw an appropriate exception with a clear
     * explanation of the failure.
     * </p>
     *
     * @param data the input data to be transformed
     * @return the transformed date data in the output format
     * @throws IllegalArgumentException if the input data is invalid or cannot be
     *                                  transformed
     * @throws NullPointerException     if the input data is null and null values
     *                                  are not supported
     */
    public O transform(I data);

}