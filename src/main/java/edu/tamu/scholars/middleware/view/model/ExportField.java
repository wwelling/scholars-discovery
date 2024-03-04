package edu.tamu.scholars.middleware.view.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 
 */
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
