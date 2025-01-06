package edu.tamu.scholars.discovery.model;

import static edu.tamu.scholars.discovery.AppConstants.CLASS;
import static edu.tamu.scholars.discovery.AppConstants.ID;
import static edu.tamu.scholars.discovery.AppConstants.TYPE;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class Individual {

    private Map<String, Object> content;

    public String getId() {
        return (String) this.content.get(ID);
    }

    public String getProxy() {
        return (String) this.content.get(CLASS);
    }

    public List<String> getType() {
        return (List<String>) this.content.get(TYPE);
    }

    public static Individual of(Map<String, Object> content) {
        Individual individual = new Individual();
        individual.setContent(content);

        return individual;
    }

}
