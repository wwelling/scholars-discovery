package edu.tamu.scholars.discovery.theme.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Embeddable
public class Home implements Serializable {

    private static final long serialVersionUID = -749563948562398456L;

    @Column(nullable = false)
    private boolean heroesNavigable = false;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "theme_home_heroes")
    private List<Hero> heroes;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "theme_home_variables")
    private List<Style> variables;

    public Home() {
        super();
        this.heroes = new ArrayList<>();
        this.variables = new ArrayList<>();
    }

}
