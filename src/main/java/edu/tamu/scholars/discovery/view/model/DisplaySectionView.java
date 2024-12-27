package edu.tamu.scholars.discovery.view.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
    name = "display_sections",
    indexes = {
        @Index(name = "idx_display_section_name", columnList = "name"),
        @Index(name = "idx_display_section_order", columnList = "\"order\"")
    }
)
@AttributeOverride(name = "name", column = @Column(nullable = false))
public class DisplaySectionView extends FieldView {

    private static final long serialVersionUID = -192837465564738291L;

    @Column(nullable = false)
    private boolean hidden;

    @Column(nullable = false)
    private boolean shared;

    @Column(nullable = false)
    private boolean paginated;

    @Column(nullable = false)
    private int pageSize = 5;

    @Column(nullable = false, columnDefinition = "TEXT")
    public String template;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "discovery_section_required_fields")
    private List<String> requiredFields;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "discovery_section_lazy_references")
    private List<String> lazyReferences;

    @OrderBy("order")
    @JoinColumn(name = "display_tab_section_id")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<DisplaySubsectionView> subsections;

    public DisplaySectionView() {
        super();
        this.hidden = false;
        this.shared = false;
        this.paginated = false;
        this.pageSize = 5;
        this.requiredFields = new ArrayList<>();
        this.lazyReferences = new ArrayList<>();
        this.subsections = new ArrayList<>();
    }

}
