package edu.psu.sweng894.chewsy.session.application.response;

import edu.psu.sweng894.chewsy.session.domain.SessionStatus;

public class GetSessionStatusResponse {
    private final SessionStatus sessionStatus;

    public GetSessionStatusResponse(final SessionStatus sessionStatus) {
        this.sessionStatus = sessionStatus;
    }

    public SessionStatus sessionStatus() {
        return sessionStatus;
    }
}
