package edu.tamu.scholars.discovery.messaging;

import static edu.tamu.scholars.discovery.messaging.EntityAction.DELETE;

public class DeleteEntityMessage<I> {

    private final I id;

    public DeleteEntityMessage(I id) {
        this.id = id;
    }

    public I getIdentifier() {
        return id;
    }

    public EntityAction getAction() {
        return DELETE;
    }

}
