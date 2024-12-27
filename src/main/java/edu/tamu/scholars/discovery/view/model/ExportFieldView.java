package edu.tamu.scholars.discovery.view.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
    name = "export_fields",
    indexes = {
        @Index(name = "idx_export_field_name", columnList = "name"),
        @Index(name = "idx_export_field_order", columnList = "\"order\"")
    }
)
@AttributeOverride(name = "name", column = @Column(nullable = false))
public class ExportFieldView extends FieldView {

    private static final long serialVersionUID = 334455667788990011L;

    @Column(name = "\"limit\"", nullable = false)
    private int limit;

    public ExportFieldView() {
        super();
        this.limit = Integer.MAX_VALUE;
    }

}
