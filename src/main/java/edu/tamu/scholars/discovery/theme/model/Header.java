package edu.tamu.scholars.discovery.theme.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class Header {

    @Embedded
    private Navbar navbar = new Navbar();

    @Embedded
    private Banner banner = new Banner();

    @ElementCollection
    @CollectionTable(name = "theme_header_variables")
    private List<Style> variables = new ArrayList<>();

}
