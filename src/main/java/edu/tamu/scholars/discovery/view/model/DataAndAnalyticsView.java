package edu.tamu.scholars.discovery.view.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * A persistent representation of how a UI should render a data and analytics view.
 * 
 * <p>See `src/main/resources/defaults/dataAndAnalyticViews.yml`</p>
 */
@Getter
@Setter
@Entity
@Table(
    name = "data_and_analytics_views",
    indexes = {
        @Index(name = "idx_data_and_analytics_view_name", columnList = "name")
})
@SuppressWarnings("java:S2160") // the inherited equals is of id
public class DataAndAnalyticsView extends CollectionView {

    private static final long serialVersionUID = -234567890123456789L;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ContainerType type;

    public DataAndAnalyticsView() {
        super();
    }

}
