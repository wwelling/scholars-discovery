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
public class Home {

    @Column(nullable = false)
    private boolean heroesNavigable = false;

    @ElementCollection
    @CollectionTable(name = "theme_home_heroes")
    private List<Hero> heroes = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "theme_home_variables")
    private List<Style> variables = new ArrayList<>();

}
