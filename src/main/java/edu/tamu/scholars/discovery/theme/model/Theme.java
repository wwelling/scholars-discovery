package edu.tamu.scholars.discovery.theme.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import edu.tamu.scholars.discovery.model.Named;

@Getter
@Setter
@Entity
@Table(
    name = "themes",
    indexes = {
        @Index(name = "idx_theme_name", columnList = "name")
    }
)
public class Theme extends Named {

    @Column(nullable = false)
    private String organization;

    @Column(nullable = false)
    private String organizationId;

    @Column(nullable = false)
    private boolean active;

    @Embedded
    private Home home;

    @Embedded
    private Header header;

    @Embedded
    private Footer footer;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "theme_colors")
    private List<Style> colors;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "theme_variants")
    private List<Style> variants;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "theme_variables")
    private List<Style> variables;

    public Theme() {
        super();
        this.active = false;
        this.home = new Home();
        this.header = new Header();
        this.footer = new Footer();
        this.colors = new ArrayList<>();
        this.variants = new ArrayList<>();
        this.variables = new ArrayList<>();
    }

}
