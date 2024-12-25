package edu.tamu.scholars.discovery.view.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "export_fields")
@AttributeOverride(name = "name", column = @Column(nullable = false))
public class ExportFieldView extends FieldView {

    private static final long serialVersionUID = 334455667788990011L;

    @Column(name = "\"limit\"", nullable = false)
    private int limit = Integer.MAX_VALUE;

}
