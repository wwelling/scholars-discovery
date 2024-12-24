package edu.tamu.scholars.discovery.messaging;

public abstract class SaveEntityMessage<E> implements EntityMessage {

    private final E entity;

    protected SaveEntityMessage(E entity) {
        this.entity = entity;
    }

    public E getEntity() {
        return entity;
    }

}
