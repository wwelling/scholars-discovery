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
public class Footer {

    @ElementCollection
    @CollectionTable(name = "theme_footer_links")
    private List<Link> links = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "theme_footer_variables")
    private List<Style> variables = new ArrayList<>();

    @Column
    private String copyright;

}
