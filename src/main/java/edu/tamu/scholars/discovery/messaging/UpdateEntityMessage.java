package edu.tamu.scholars.discovery.messaging;

import static edu.tamu.scholars.discovery.messaging.EntityAction.UPDATE;

/**
 * 
 */
public class UpdateEntityMessage<E> extends SaveEntityMessage<E> {

    public UpdateEntityMessage(E entity) {
        super(entity);
    }

    public EntityAction getAction() {
        return UPDATE;
    }

}
