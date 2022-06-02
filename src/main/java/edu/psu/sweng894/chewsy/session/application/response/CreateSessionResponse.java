package edu.psu.sweng894.chewsy.session.application.response;

import java.util.UUID;

public class CreateSessionResponse {
    private final UUID id;

    public CreateSessionResponse(final UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
    
}
