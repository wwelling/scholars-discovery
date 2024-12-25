package edu.tamu.scholars.discovery.view.model;

import static jakarta.persistence.EnumType.STRING;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A persistent representation of how a UI should render a display view.
 * 
 * <p>See `src/main/resources/defaults/displayViews.yml`</p>
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
    name = "display_views",
    indexes = {
        @jakarta.persistence.Index(name = "idx_display_view_name", columnList = "name")
})
public class DisplayView extends View {

    private static final long serialVersionUID = 501234567890987654L;

    @ElementCollection
    @CollectionTable(name = "display_view_types")
    private List<String> types = new ArrayList<>();

    @Column(nullable = false, columnDefinition = "TEXT")
    private String mainContentTemplate;

    @Column(columnDefinition = "TEXT")
    private String leftScanTemplate;

    @Column(columnDefinition = "TEXT")
    private String rightScanTemplate;

    @Column(columnDefinition = "TEXT")
    private String asideTemplate;

    @Enumerated(STRING)
    @Column(nullable = false)
    private Side asideLocation = Side.RIGHT;

    @JoinColumn(name = "export_view_id")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ExportView> exportViews = new ArrayList<>();

    @ElementCollection
    @MapKeyColumn(name = "name")
    @Column(name = "meta_template", columnDefinition = "TEXT")
    private Map<String, String> metaTemplates = new HashMap<>();

    @ElementCollection
    @MapKeyColumn(name = "name")
    @Column(name = "embed_template", columnDefinition = "TEXT")
    private Map<String, String> embedTemplates = new HashMap<>();

    @JoinColumn(name = "display_view_id")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<DisplayTabView> tabs = new ArrayList<>();

}
