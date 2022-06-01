package edu.psu.sweng894.chewsy.session.domain;

public class Attendee {
    private String email;

    public Attendee(final String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }
}
