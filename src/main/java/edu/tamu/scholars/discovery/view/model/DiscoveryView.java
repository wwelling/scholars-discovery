package edu.tamu.scholars.discovery.view.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A persistent representation of how a UI should render a discovery view and its result set.
 * 
 * <p>See `src/main/resources/defaults/discoveryViews.yml`</p>
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@JsonInclude(NON_EMPTY)
@Table(name = "discovery_views")
public class DiscoveryView extends CollectionView {

    private static final long serialVersionUID = 785672345676567890L;

    @Column(nullable = true)
    private String defaultSearchField;

    @ElementCollection
    private List<String> highlightFields = new ArrayList<>();

    @Column(nullable = true)
    private String highlightPrefix;

    @Column(nullable = true)
    private String highlightPostfix;

}
