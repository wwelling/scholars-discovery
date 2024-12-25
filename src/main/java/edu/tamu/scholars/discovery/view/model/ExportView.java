package edu.tamu.scholars.discovery.view.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "display_export_views")
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
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ExportFieldView> lazyReferences = new ArrayList<>();

}
