package edu.tamu.scholars.discovery.view.model;

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
public class ExportField implements Serializable {

    private static final long serialVersionUID = -876543210987654321L;

    @Column(nullable = false)
    private String columnHeader;

    @Column(nullable = false)
    private String valuePath;

}
