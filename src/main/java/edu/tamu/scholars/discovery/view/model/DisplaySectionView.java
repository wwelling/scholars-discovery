package edu.tamu.scholars.discovery.view.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "display_sections")
@AttributeOverride(name = "name", column = @Column(nullable = false))
public class DisplaySectionView extends FieldView {

    private static final long serialVersionUID = -192837465564738291L;

    @Column(nullable = false)
    private boolean hidden = false;

    @Column(nullable = false)
    private boolean shared = false;

    @Column(nullable = false)
    private boolean paginated = false;

    @Column(nullable = false)
    private int pageSize = 5;

    @Column(nullable = false, columnDefinition = "TEXT")
    public String template;

    @ElementCollection
    private List<String> requiredFields = new ArrayList<>();

    @ElementCollection
    private List<String> lazyReferences = new ArrayList<>();

    @OrderBy("order")
    @JoinColumn(name = "display_tab_section_id")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<DisplaySubsectionView> subsections = new ArrayList<>();

}
