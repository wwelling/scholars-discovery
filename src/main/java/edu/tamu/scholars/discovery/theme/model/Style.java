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
@EqualsAndHashCode
@NoArgsConstructor
@ToString
@Embeddable
public class Style implements Serializable {

    private static final long serialVersionUID = 832756923563947852L;

    @Column(name = "\"key\"", nullable = false)
    private String key;

    @Column(name = "\"value\"", nullable = false)
    private String value;

}
