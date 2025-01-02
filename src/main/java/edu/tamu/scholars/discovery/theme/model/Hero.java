package edu.tamu.scholars.discovery.theme.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Embeddable
public class Hero implements Serializable {

    private static final long serialVersionUID = 349052349572340955L;

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
