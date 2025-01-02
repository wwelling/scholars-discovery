package edu.tamu.scholars.discovery.model;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class Individual {

    private String id;

    private String proxy;

    private List<String> types;

    private Map<String, Object> content;

}
