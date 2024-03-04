package edu.tamu.scholars.middleware.view.model;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * 
 */
@Entity
@Table(name = "display_tabs")
@AttributeOverride(name = "name", column = @Column(nullable = false))
public class DisplayTabView extends View {

    private static final long serialVersionUID = -6232439954200363571L;

    @Column(nullable = false)
    private boolean hidden;

    @OrderBy("order")
    @JoinColumn(name = "display_tab_view_id")
    @OneToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    public List<DisplaySectionView> sections;

    public DisplayTabView() {
        super();
        hidden = false;
        sections = new ArrayList<DisplaySectionView>();
    }

    public String getName() {
        return name;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public List<DisplaySectionView> getSections() {
        return sections;
    }

    public void setSections(List<DisplaySectionView> sections) {
        this.sections = sections;
    }

}
