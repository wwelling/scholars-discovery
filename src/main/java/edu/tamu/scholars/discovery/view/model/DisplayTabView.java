package edu.tamu.scholars.discovery.view.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

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
