package edu.tamu.scholars.discovery.view.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import edu.tamu.scholars.discovery.model.OpKey;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
@JsonInclude(Include.NON_NULL)
public class Filter implements Serializable {

    private static final long serialVersionUID = -213456789987654321L;

    @Column(nullable = false)
    private String field;

    @Column(nullable = false)
    private String value;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OpKey opKey;

    public Filter() {
        super();
        this.opKey = OpKey.EQUALS;
    }

}
