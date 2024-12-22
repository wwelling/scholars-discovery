package edu.tamu.scholars.discovery.view.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * 
 */
@Entity
@Table(name = "display_export_views")
@AttributeOverride(name = "name", column = @Column(nullable = false))
public class ExportView extends View {

    private static final long serialVersionUID = 8352195631003934922L;

    @Column(columnDefinition = "TEXT")
    private String contentTemplate;

    @Column(columnDefinition = "TEXT")
    private String headerTemplate;

    @OneToOne(cascade = CascadeType.ALL, optional = true)
    private ExportFieldView multipleReference;

    @OrderBy("order")
    @JoinColumn(name = "export_field_id")
    @OneToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<ExportFieldView> lazyReferences;

    public ExportView() {
        super();
        lazyReferences = new ArrayList<ExportFieldView>();
    }

    public String getContentTemplate() {
        return contentTemplate;
    }

    public void setContentTemplate(String contentTemplate) {
        this.contentTemplate = contentTemplate;
    }

    public String getHeaderTemplate() {
        return headerTemplate;
    }

    public void setHeaderTemplate(String headerTemplate) {
        this.headerTemplate = headerTemplate;
    }

    public ExportFieldView getMultipleReference() {
        return multipleReference;
    }

    public void setMultipleReference(ExportFieldView multipleReference) {
        this.multipleReference = multipleReference;
    }

    public List<ExportFieldView> getLazyReferences() {
        return lazyReferences;
    }

    public void setLazyReferences(List<ExportFieldView> lazyReferences) {
        this.lazyReferences = lazyReferences;
    }

}
