package edu.tamu.scholars.discovery.view.model;

import jakarta.persistence.MappedSuperclass;

import edu.tamu.scholars.discovery.model.Named;

/**
 * Abstract mapped superclass `View` to differentiate between persistent views.
 */
@MappedSuperclass
public abstract class View extends Named {

}
