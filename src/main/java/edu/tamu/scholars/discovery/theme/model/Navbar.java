package edu.tamu.scholars.discovery.theme.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;

import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Navbar {

    @Column(name = "navbar_brand_text")
    private String brandText;

    @Column(name = "navbar_brand_uri")
    private String brandUri;

    @Column(name = "navbar_logo_uri")
    private String logoUri;

    @ElementCollection
    @CollectionTable(name = "theme_navbar_links")
    private List<Link> links;

    @ElementCollection
    @CollectionTable(name = "theme_navbar_variables")
    private List<Style> variables;

    public Navbar() {
        super();
        this.links = new ArrayList<>();
        this.variables = new ArrayList<>();
    }

    public String getBrandText() {
        return brandText;
    }

    public void setBrandText(String brandText) {
        this.brandText = brandText;
    }

    public String getBrandUri() {
        return brandUri;
    }

    public void setBrandUri(String brandUri) {
        this.brandUri = brandUri;
    }

    public String getLogoUri() {
        return logoUri;
    }

    public void setLogoUri(String logoUri) {
        this.logoUri = logoUri;
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

}
