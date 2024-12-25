package edu.tamu.scholars.discovery.etl.load;

import java.util.Collection;
import java.util.Map;

import edu.tamu.scholars.discovery.etl.DataProcessor;

/**
 * Defines the contract for loading processed data into a target system or
 * storage medium. This interface extends {@link DataProcessor} to provide
 * specific loading capabilities while maintaining the standard ETL processing
 * lifecycle.
 * 
 * <p>
 * The DataLoader interface handles the final stage of the ETL process, where
 * transformed data is persistently stored or indexed. It supports both batch
 * loading of multiple documents and individual document loading for flexibility
 * in different loading scenarios.
 * </p>
 * 
 * <p>
 * Implementations typically handle:
 * </p>
 * 
 * <ul>
 *   <li>Database insertions or updates</li>
 *   <li>Search index population</li>
 *   <li>File system storage</li>
 *   <li>Data persistence across various storage systems</li>
 * </ul>
 * 
 * <p>
 * The loading process should ensure:
 * </p>
 * 
 * <ul>
 *   <li>Data integrity during the loading process</li>
 *   <li>Proper error handling and rollback capabilities</li>
 *   <li>Performance optimization for bulk operations</li>
 *   <li>Consistent state management</li>
 * </ul>
 *
 * @since 1.5.0
 * @see DataProcessor
 * @see java.util.Map
 * @see java.util.Collection
 */
public interface DataLoader extends DataProcessor {

    /**
     * Loads a collection of documents into the target system.
     * 
     * <p>
     * This method handles bulk loading operations, optimizing the loading process
     * for multiple documents. It should implement appropriate batching strategies
     * and error handling mechanisms to ensure reliable data loading.
     * </p>
     * 
     * <p>
     * The method should handle the following considerations:
     * </p>
     * 
     * <ul>
     *   <li>Transaction management for bulk operations</li>
     *   <li>Memory efficiency for large collections</li>
     *   <li>Partial success scenarios</li>
     *   <li>Duplicate handling strategies</li>
     * </ul>
     *
     * @param documents a collection of documents where each document is represented
     *                  as a map of field names to their values
     * @throws IllegalArgumentException if the documents collection contains invalid
     *                                  entries
     * @throws RuntimeException         if the loading operation fails
     */
    public void load(Collection<Map<String, Object>> documents);

    /**
     * Loads a single document into the target system.
     * 
     * <p>
     * This method provides a way to load individual documents, which is useful for
     * incremental updates or single-document operations. It should maintain the
     * same level of data integrity and error handling as the bulk loading
     * operation.
     * </p>
     * 
     * <p>
     * The method should handle:
     * </p>
     * 
     * <ul>
     *   <li>Document validation</li>
     *   <li>Single-document transaction management</li>
     *   <li>Update/insert decision logic</li>
     *   <li>Error reporting for individual documents</li>
     * </ul>
     *
     * @param document a map representing the document, where keys are field names
     *                 and values are the corresponding field values
     * @throws IllegalArgumentException if the document is invalid
     * @throws RuntimeException         if the loading operation fails
     */
    public void load(Map<String, Object> document);

}