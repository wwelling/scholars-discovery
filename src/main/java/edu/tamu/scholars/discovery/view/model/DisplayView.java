package edu.tamu.scholars.discovery.view.model;

import static jakarta.persistence.EnumType.STRING;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * A persistent representation of how a UI should render a display view.
 * 
 * <p>See `src/main/resources/defaults/displayViews.yml`</p>
 */
@Entity
@Table(name = "display_views")
public class DisplayView extends View {

    private static final long serialVersionUID = 7556127622115170597L;

    @ElementCollection
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
    @OneToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<ExportView> exportViews;

    @ElementCollection
    @MapKeyColumn(name = "name")
    @Column(name = "meta_template", columnDefinition = "TEXT")
    private Map<String, String> metaTemplates;

    @ElementCollection
    @MapKeyColumn(name = "name")
    @Column(name = "embed_template", columnDefinition = "TEXT")
    private Map<String, String> embedTemplates;

    @JoinColumn(name = "display_view_id")
    @OneToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<DisplayTabView> tabs;

    public DisplayView() {
        super();
        types = new ArrayList<>();
        exportViews = new ArrayList<>();
        metaTemplates = new HashMap<>();
        embedTemplates = new HashMap<>();
        tabs = new ArrayList<>();
        asideLocation = Side.RIGHT;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getMainContentTemplate() {
        return mainContentTemplate;
    }

    public void setMainContentTemplate(String mainContentTemplate) {
        this.mainContentTemplate = mainContentTemplate;
    }

    public String getLeftScanTemplate() {
        return leftScanTemplate;
    }

    public void setLeftScanTemplate(String leftScanTemplate) {
        this.leftScanTemplate = leftScanTemplate;
    }

    public String getRightScanTemplate() {
        return rightScanTemplate;
    }

    public void setRightScanTemplate(String rightScanTemplate) {
        this.rightScanTemplate = rightScanTemplate;
    }

    public String getAsideTemplate() {
        return asideTemplate;
    }

    public void setAsideTemplate(String asideTemplate) {
        this.asideTemplate = asideTemplate;
    }

    public Side getAsideLocation() {
        return asideLocation;
    }

    public void setAsideLocation(Side asideLocation) {
        this.asideLocation = asideLocation;
    }

    public List<ExportView> getExportViews() {
        return exportViews;
    }

    public void setExportViews(List<ExportView> exportViews) {
        this.exportViews = exportViews;
    }

    public Map<String, String> getMetaTemplates() {
        return metaTemplates;
    }

    public void setMetaTemplates(Map<String, String> metaTemplates) {
        this.metaTemplates = metaTemplates;
    }

    public Map<String, String> getEmbedTemplates() {
        return embedTemplates;
    }

    public void setEmbedTemplates(Map<String, String> embedTemplates) {
        this.embedTemplates = embedTemplates;
    }

    public List<DisplayTabView> getTabs() {
        return tabs;
    }

    public void setTabs(List<DisplayTabView> tabs) {
        this.tabs = tabs;
    }

}
