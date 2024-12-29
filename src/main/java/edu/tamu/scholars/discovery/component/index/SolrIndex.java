package edu.tamu.scholars.discovery.component.index;

import lombok.extern.slf4j.Slf4j;

import edu.tamu.scholars.discovery.config.model.IndexConfig;

@Slf4j
public class SolrIndex implements Index {

    private final IndexConfig config;

    public SolrIndex(IndexConfig config) {
        this.config = config;
    }

    public void config() {
        System.out.println(this.config);
    }

}
