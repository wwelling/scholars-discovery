package edu.tamu.scholars.discovery.view.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort.Direction;

import edu.tamu.scholars.discovery.model.OpKey;

/**
 * Embeddable domain model for {@link CollectionView} result set faceting.
 */
@Getter
@Setter
@NoArgsConstructor
@Embeddable
@JsonInclude(Include.NON_NULL)
public class Facet implements Serializable {

    private static final long serialVersionUID = 223344556677889900L;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String field;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OpKey opKey = OpKey.EQUALS;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FacetType type = FacetType.STRING;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FacetSort sort;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Direction direction = Direction.DESC;

    @Column(nullable = false)
    private int pageSize = 10;

    @Column(nullable = false)
    private int pageNumber = 1;

    @Column(nullable = false)
    private boolean expandable = true;

    @Column(nullable = false)
    private boolean collapsible = true;

    @Column(nullable = false)
    private boolean collapsed = true;

    @Column(nullable = false)
    private boolean useDialog = false;

    @Column(nullable = false)
    private boolean hidden = false;

    @Column(nullable = true)
    private String rangeStart;

    @Column(nullable = true)
    private String rangeEnd;

    @Column(nullable = true)
    private String rangeGap;

}
