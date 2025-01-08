package edu.tamu.scholars.discovery.view.model;

import java.io.Serializable;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Sort.Direction;

import edu.tamu.scholars.discovery.model.OpKey;
import edu.tamu.scholars.discovery.view.model.converter.DomainConverter;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Embeddable
@JsonInclude(Include.NON_NULL)
public class Facet implements Serializable {

    private static final long serialVersionUID = 223344556677889900L;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String field;

    @Convert(converter = DomainConverter.class)
    @Column(nullable = false, columnDefinition = "TEXT")
    private JsonNode domain;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OpKey opKey;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FacetType type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FacetSort sort;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Direction direction;

    @Column(nullable = false)
    private int pageSize;

    @Column(nullable = false)
    private int pageNumber;

    @Column(nullable = false)
    private boolean expandable;

    @Column(nullable = false)
    private boolean collapsible;

    @Column(nullable = false)
    private boolean collapsed;

    @Column(nullable = false)
    private boolean useDialog;

    @Column(nullable = false)
    private boolean hidden;

    @Column(nullable = true)
    private String rangeStart;

    @Column(nullable = true)
    private String rangeEnd;

    @Column(nullable = true)
    private String rangeGap;

    public Facet() {
        super();
        this.opKey = OpKey.EQUALS;
        this.type = FacetType.STRING;
        this.direction = Direction.DESC;
        this.pageSize = 10;
        this.pageNumber = 1;
        this.expandable = true;
        this.collapsible = true;
        this.collapsed = true;
        this.useDialog = false;
        this.hidden = false;
    }

}
