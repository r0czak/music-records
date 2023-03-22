package com.ws.musicrecords.exception;

import java.io.Serial;

public class ObjectNotFoundException extends RuntimeException {
    @Serial private static final long serialVersionUID = 1L;

    public ObjectNotFoundException(ObjectNotFoundType objectType, String id) {
        super(String.format("Object of type: [%s] with and id: [%s] not found", objectType.name(), id));
    }
}
