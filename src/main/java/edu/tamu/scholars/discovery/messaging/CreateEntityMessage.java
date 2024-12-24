package edu.tamu.scholars.discovery.messaging;

import static edu.tamu.scholars.discovery.messaging.EntityAction.CREATE;

public class CreateEntityMessage<E> extends SaveEntityMessage<E> {

    public CreateEntityMessage(E entity) {
        super(entity);
    }

    public EntityAction getAction() {
        return CREATE;
    }

}
