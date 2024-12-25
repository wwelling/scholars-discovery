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
public class Footer implements Serializable {

    private static final long serialVersionUID = -349875623948756398L;

    @ElementCollection
    @CollectionTable(name = "theme_footer_links")
    private List<Link> links;

    @ElementCollection
    @CollectionTable(name = "theme_footer_variables")
    private List<Style> variables;

    @Column
    private String copyright;

    public Footer() {
        super();
        this.links = new ArrayList<>();
        this.variables = new ArrayList<>();
    }

}
