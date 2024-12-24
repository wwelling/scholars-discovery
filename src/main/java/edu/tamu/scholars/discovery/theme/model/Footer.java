package edu.tamu.scholars.discovery.theme.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;

import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Footer {

    @ElementCollection
    @CollectionTable(name = "theme_footer_links")
    private List<Link> links;

    @ElementCollection
    @CollectionTable(name = "theme_footer_variables")
    private List<Style> variables;

    @Column
    private String copyright;

    public Footer() {
        super();
        this.links = new ArrayList<>();
        this.variables = new ArrayList<>();
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public List<Style> getVariables() {
        return variables;
    }

    public void setVariables(List<Style> variables) {
        this.variables = variables;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }
}
