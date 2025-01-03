package edu.tamu.scholars.discovery.view.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@JsonInclude(NON_EMPTY)
@Table(
    name = "discovery_views",
    indexes = {
        @Index(name = "idx_discovery_view_name", columnList = "name")
    }
)
public class DiscoveryView extends CollectionView {

    private static final long serialVersionUID = 785672345676567890L;

    @Column(nullable = true)
    private String defaultSearchField;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "discovery_view_highlight_fields")
    private List<String> highlightFields;

    @Column(nullable = true)
    private String highlightPrefix;

    @Column(nullable = true)
    private String highlightPostfix;

    public DiscoveryView() {
        super();
        this.highlightFields = new ArrayList<>();
    }

}
