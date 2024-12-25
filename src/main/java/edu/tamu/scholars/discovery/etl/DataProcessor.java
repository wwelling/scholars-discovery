package edu.tamu.scholars.discovery.etl;

/**
 * Defines the contract for data processing operations within the ETL (Extract,
 * Transform, Load) pipeline. Implementations of this interface handle the
 * initialization and processing stages of data manipulation, allowing for both
 * pre-processing and post-processing operations.
 * 
 * <p>
 * The typical lifecycle of a DataProcessor implementation follows these stages:
 * </p>
 * 
 * <ol>
 *   <li>Initialization ({@link #init()})</li>
 *   <li>Pre-processing ({@link #preProcess()})</li>
 *   <li>Main processing (handled by implementing class)</li>
 *   <li>Post-processing ({@link #postProcess()})</li>
 * </ol>
 * 
 * <p>
 * This interface is designed to be implemented by classes that need to perform
 * data processing tasks in a structured and consistent manner. It's
 * particularly useful for ETL operations where data needs to be prepared,
 * transformed, and finalized in distinct phases.
 * </p>
 *
 * @since 1.5.0
 * @see edu.tamu.scholars.discovery.etl
 */
public interface DataProcessor {

    /**
     * Initializes the data processor.
     * 
     * <p>
     * This method should be called before any processing begins. It should handle
     * tasks such as:
     * </p>
     * 
     * <ul>
     *   <li>Setting up required resources</li>
     *   <li>Establishing connections</li>
     *   <li>Loading configuration settings</li>
     *   <li>Preparing internal state</li>
     * </ul>
     * 
     * <p>
     * Implementations should ensure that all necessary resources are properly
     * initialized and the processor is ready for subsequent processing operations.
     * </p>
     * 
     * @throws RuntimeException if initialization fails
     */
    public void init();

    /**
     * Performs pre-processing operations on the data.
     * 
     * <p>
     * This method should be called after initialization but before the main
     * processing occurs. It should handle preliminary data preparation tasks such
     * as:
     * </p>
     * 
     * <ul>
     *   <li>Data validation</li>
     *   <li>Format standardization</li>
     *   <li>Initial data cleaning</li>
     *   <li>Setting up temporary processing structures</li>
     * </ul>
     * 
     * <p>
     * Implementations should ensure that data is properly prepared for the main
     * processing phase that follows.
     * </p>
     * 
     * @throws RuntimeException if pre-processing operations fail
     */
    public void preProcess();

    /**
     * Performs post-processing operations on the data.
     * 
     * <p>
     * This method should be called after the main processing has completed. It
     * should handle cleanup and finalization tasks such as:
     * </p>
     * 
     * <ul>
     *   <li>Data validation</li>
     *   <li>Resource cleanup</li>
     *   <li>Final data transformations</li>
     *   <li>Generating processing reports or metrics</li>
     * </ul>
     * 
     * <p>
     * Implementations should ensure that all resources are properly cleaned up and
     * the processed data is in its final required state.
     * </p>
     * 
     * @throws RuntimeException if post-processing operations fail
     */
    public void postProcess();

    /**
     * Destroys the data processor.
     * 
     * <p>
     * This method should be called after all processing completes. It should handle
     * tasks such as:
     * </p>
     * 
     * <ul>
     *   <li>Closing connections</li>
     *   <li>Unloading configuration settings</li>
     *   <li>Restoring internal state</li>
     * </ul>
     * 
     * <p>
     * Implementations should ensure that all necessary resources are properly
     * closed and destroyed and the processor is finished.
     * </p>
     * 
     * @throws RuntimeException if destroy fails
     */
    public void destroy();

}