package edu.tamu.scholars.discovery.theme.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Navbar implements Serializable {

    private static final long serialVersionUID = 340985692834598345L;

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

}
