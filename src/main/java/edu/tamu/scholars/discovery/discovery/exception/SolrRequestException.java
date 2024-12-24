package edu.tamu.scholars.discovery.discovery.exception;

public class SolrRequestException extends RuntimeException {

    private static final long serialVersionUID = 385092384750932845L;

    public SolrRequestException(String message) {
        super(message);
    }

    public SolrRequestException(String message, Throwable cause) {
        super(message, cause);
    }

}
