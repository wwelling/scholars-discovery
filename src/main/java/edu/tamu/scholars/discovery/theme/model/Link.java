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
public class Link {

    @Column
    private String label;

    @Column
    private String uri;

}
