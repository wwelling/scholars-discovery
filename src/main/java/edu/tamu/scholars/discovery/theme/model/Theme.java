package edu.tamu.scholars.discovery.theme.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import edu.tamu.scholars.discovery.model.Named;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
    name = "themes",
    indexes = {
        @Index(name = "idx_theme_name", columnList = "name")
})
public class Theme extends Named {

    @Column(nullable = false)
    private String organization;

    @Column(nullable = false)
    private String organizationId;

    @Column(nullable = false)
    private boolean active = false;

    @Embedded
    private Home home = new Home();

    @Embedded
    private Header header = new Header();

    @Embedded
    private Footer footer = new Footer();

    @ElementCollection
    @CollectionTable(name = "theme_colors")
    private List<Style> colors = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "theme_variants")
    private List<Style> variants = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "theme_variables")
    private List<Style> variables = new ArrayList<>();

}
