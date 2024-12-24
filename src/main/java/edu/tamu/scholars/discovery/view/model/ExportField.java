package edu.tamu.scholars.discovery.view.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ExportField {

    @Column(nullable = false)
    private String columnHeader;

    @Column(nullable = false)
    private String valuePath;

    public ExportField() {

    }

    public String getColumnHeader() {
        return columnHeader;
    }

    public void setColumnHeader(String columnHeader) {
        this.columnHeader = columnHeader;
    }

    public String getValuePath() {
        return valuePath;
    }

    public void setValuePath(String valuePath) {
        this.valuePath = valuePath;
    }

}
