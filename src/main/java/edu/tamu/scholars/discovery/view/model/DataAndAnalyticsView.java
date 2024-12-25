package edu.tamu.scholars.discovery.view.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A persistent representation of how a UI should render a data and analytics view.
 * 
 * <p>See `src/main/resources/defaults/dataAndAnalyticViews.yml`</p>
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "data_and_analytics_views")
public class DataAndAnalyticsView extends CollectionView {

    private static final long serialVersionUID = -234567890123456789L;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ContainerType type;

}
