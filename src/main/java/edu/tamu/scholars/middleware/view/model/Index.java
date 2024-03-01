package edu.tamu.scholars.middleware.view.model;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import java.util.List;

import edu.tamu.scholars.middleware.model.OpKey;

/**
 * {@link DirectoryView} embedded class `Index` to describe collection result
 * navigation.
 */
@Embeddable
public class Index {

    @Column(nullable = false)
    private String field;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OpKey opKey;

    @ElementCollection
    private List<String> options;

    public Index() {
        opKey = OpKey.STARTS_WITH;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public OpKey getOpKey() {
        return opKey;
    }

    public void setOpKey(OpKey opKey) {
        this.opKey = opKey;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

}
