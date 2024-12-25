package edu.tamu.scholars.discovery.theme.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class Hero {

    @Column
    private String imageUri;

    @Column
    private String imageAlt;

    @Column
    private String watermarkImageUri;

    @Column
    private String watermarkText;

    @Column(columnDefinition = "TEXT")
    private String helpText;

    @Column(columnDefinition = "TEXT")
    private String baseText;

    @Column
    private String fontColor;

    @Column
    private String linkColor;

    @Column
    private String linkHoverColor;

    @Column
    private int slideInterval;

}
