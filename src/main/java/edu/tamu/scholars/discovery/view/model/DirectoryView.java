package edu.tamu.scholars.discovery.view.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * A persistent representation of how a UI should render a directory view and
 * its result set.
 * 
 * <p>
 * See `src/main/resources/defaults/directoryView.yml`
 * </p>
 */
@Getter
@Setter
@Entity
@Table(name = "directory_views", indexes = {
        @Index(name = "idx_directory_view_name", columnList = "name")
})
// @ValidIndexField(message = "{DirectoryView.validIndexField}")
// TODO: validate in handler before create and save
@SuppressWarnings("java:S2160") // the inherited equals is of id
public class DirectoryView extends CollectionView {

    private static final long serialVersionUID = 112233445566778899L;

    @Embedded
    private Grouping grouping;

    public DirectoryView() {
        super();
    }

}
