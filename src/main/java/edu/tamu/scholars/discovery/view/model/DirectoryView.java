package edu.tamu.scholars.discovery.view.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A persistent representation of how a UI should render a directory view and its result set.
 * 
 * <p>See `src/main/resources/defaults/directoryView.yml`</p>
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "directory_views")
//@ValidIndexField(message = "{DirectoryView.validIndexField}")
public class DirectoryView extends CollectionView {

    private static final long serialVersionUID = 112233445566778899L;

    @Embedded
    private Index index;

}
