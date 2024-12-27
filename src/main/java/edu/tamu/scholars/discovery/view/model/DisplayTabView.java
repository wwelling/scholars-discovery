package edu.tamu.scholars.discovery.view.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a display tab view in the application.
 */
@Getter
@Setter
@Entity
@Table(
    name = "display_tabs",
    indexes = {
        @Index(name = "idx_display_tab_name", columnList = "name")
    }
)
@AttributeOverride(name = "name", column = @Column(nullable = false))
public class DisplayTabView extends View {

    private static final long serialVersionUID = -987654321098765432L;

    @Column(nullable = false)
    private boolean hidden;

    @OrderBy("order")
    @JoinColumn(name = "display_tab_view_id")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public List<DisplaySectionView> sections; // NOSONAR

    public DisplayTabView() {
        super();
        this.hidden = false;
        this.sections = new ArrayList<>();
    }

}
