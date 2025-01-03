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
public class Link implements Serializable {

    private static final long serialVersionUID = -340957239457230984L;

    @Column
    private String label;

    @Column
    private String uri;

}
