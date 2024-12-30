package edu.tamu.scholars.discovery.view.model;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import edu.tamu.scholars.discovery.model.Named;

@Getter
@Setter
@MappedSuperclass
public abstract class View extends Named {

}
