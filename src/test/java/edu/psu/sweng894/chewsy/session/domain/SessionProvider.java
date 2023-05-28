package edu.psu.sweng894.chewsy.session.domain;

public class SessionProvider {
    public static Session getCreatedSession() {
        return new Session();
    }

    public static Session getCompletedSession() {
        final Session session = getCreatedSession();
        session.setStatusComplete();

        return session;
    }    
}
