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
public class Banner {

    @Column(name = "banner_image_uri")
    private String imageUri;

    @Column(name = "banner_alt_text")
    private String altText;

    @ElementCollection
    @CollectionTable(name = "theme_banner_variables")
    private List<Style> variables = new ArrayList<>();

}
