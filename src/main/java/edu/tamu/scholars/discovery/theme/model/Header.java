package edu.tamu.scholars.discovery.theme.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.FetchType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class Header implements Serializable {

    private static final long serialVersionUID = 309523945723094857L;

    @Embedded
    private Navbar navbar;

    @Embedded
    private Banner banner;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "theme_header_variables")
    private List<Style> variables;

    public Header() {
        super();
        this.navbar = new Navbar();
        this.banner = new Banner();
        this.variables = new ArrayList<>();
    }

}
