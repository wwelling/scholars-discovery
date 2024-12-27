package edu.tamu.scholars.discovery.theme.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Banner implements Serializable {

    private static final long serialVersionUID = 786598734589732465L;

    @Column(name = "banner_image_uri")
    private String imageUri;

    @Column(name = "banner_alt_text")
    private String altText;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "theme_banner_variables")
    private List<Style> variables;

    public Banner() {
        super();
        this.variables = new ArrayList<>();
    }

}
