package edu.tamu.scholars.discovery.view.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
    name = "display_export_views",
    indexes = {
        @Index(name = "idx_display_export_view_name", columnList = "name")
    }
)
@AttributeOverride(name = "name", column = @Column(nullable = false))
public class ExportView extends View {

    private static final long serialVersionUID = -654321098765432109L;

    @Column(columnDefinition = "TEXT")
    private String contentTemplate;

    @Column(columnDefinition = "TEXT")
    private String headerTemplate;

    @OneToOne(cascade = CascadeType.ALL, optional = true)
    private ExportFieldView multipleReference;

    @OrderBy("order")
    @JoinColumn(name = "export_field_id")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ExportFieldView> lazyReferences;

    public ExportView() {
        super();
        this.lazyReferences = new HashSet<>();
    }

}
