package edu.tamu.scholars.discovery.view.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "directory_views", indexes = {
        @Index(name = "idx_directory_view_name", columnList = "name")
    }
)
// TODO: validate in handler before create and save
public class DirectoryView extends CollectionView {

    private static final long serialVersionUID = 112233445566778899L;

    @Embedded
    private Grouping grouping;

    public DirectoryView() {
        super();
    }

}
