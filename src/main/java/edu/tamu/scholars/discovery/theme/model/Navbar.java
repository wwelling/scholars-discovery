package edu.tamu.scholars.discovery.theme.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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
    private List<Link> links = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "theme_navbar_variables")
    private List<Style> variables = new ArrayList<>();

}
