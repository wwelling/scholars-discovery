package edu.tamu.scholars.middleware.discovery.service;

public interface SolrIndexService {

    public void index();

    public void index(String subject);

    public Class<?> type();

    public String name();
    
    public String collection();

}
