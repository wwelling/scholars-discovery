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
public class Style {

    @Column
    private String key;

    @Column
    private String value;

}
