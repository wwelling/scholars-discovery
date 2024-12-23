package edu.tamu.scholars.discovery.theme.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Link {

    @Column
    private String label;

    @Column
    private String uri;

    public Link() {
        super();
    }

    public Link(String label, String uri) {
        this();
        this.label = label;
        this.uri = uri;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}
