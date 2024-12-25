package edu.tamu.scholars.discovery.view.model;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import edu.tamu.scholars.discovery.model.Named;

/**
 * Abstract mapped superclass `View` to differentiate between persistent views.
 */
@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class View extends Named {

}
