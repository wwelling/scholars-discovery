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
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
    name = "display_views",
    indexes = {
        @Index(name = "idx_display_view_name", columnList = "name")
    }
)
public class DisplayView extends View {

    private static final long serialVersionUID = 501234567890987654L;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "display_view_types")
    private List<String> types;

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
    private Side asideLocation;

    @JoinColumn(name = "export_view_id")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ExportView> exportViews;

    @ElementCollection(fetch = FetchType.LAZY)
    @MapKeyColumn(name = "name")
    @Column(name = "meta_template", columnDefinition = "TEXT")
    private Map<String, String> metaTemplates;

    @ElementCollection(fetch = FetchType.LAZY)
    @MapKeyColumn(name = "name")
    @Column(name = "embed_template", columnDefinition = "TEXT")
    private Map<String, String> embedTemplates;

    @JoinColumn(name = "display_view_id")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<DisplayTabView> tabs;

    public DisplayView() {
        super();
        this.types = new ArrayList<>();
        this.asideLocation = Side.RIGHT;
        this.exportViews = new ArrayList<>();
        this.metaTemplates = new HashMap<>();
        this.embedTemplates = new HashMap<>();
        this.tabs = new ArrayList<>();
    }

}
