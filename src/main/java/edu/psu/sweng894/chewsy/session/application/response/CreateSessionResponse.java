package edu.psu.sweng894.chewsy.session.application.response;

public class CreateSessionResponse {
    private final Long id;

    public CreateSessionResponse(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
    
}
