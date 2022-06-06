package edu.psu.sweng894.chewsy.session.domain;

import java.util.UUID;

public class SessionProvider {
    public static Session getCreatedSession() {
        return new Session(UUID.randomUUID(), "test@email.com");
    }

    public static Session getCompletedSession() {
        final Session session = getCreatedSession();
        session.complete();

        return session;
    }    
}
