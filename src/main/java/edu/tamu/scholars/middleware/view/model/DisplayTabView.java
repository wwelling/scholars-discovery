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
 * Represents a display tab view in the application.
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

    /**
     * Default constructor for DisplayTabView.
     */
    public DisplayTabView() {
        super();
        hidden = false;
        sections = new ArrayList<>();
    }

    /**
     * Get the name of the display tab.
     *
     * @return The name of the display tab.
     */
    public String getName() {
        return name;
    }

    /**
     * Check if the display tab is hidden.
     *
     * @return True if hidden, false otherwise.
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * Set the display tab's hidden status.
     *
     * @param hidden True to hide the display tab, false otherwise.
     */
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    /**
     * Get the list of display sections associated with this tab.
     *
     * @return List of display sections.
     */
    public List<DisplaySectionView> getSections() {
        return sections;
    }

    /**
     * Set the list of display sections for this tab.
     *
     * @param sections List of display sections.
     */
    public void setSections(List<DisplaySectionView> sections) {
        this.sections = sections;
    }
}
