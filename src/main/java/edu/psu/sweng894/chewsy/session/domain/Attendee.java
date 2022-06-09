package edu.psu.sweng894.chewsy.session.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Attendee {
    @Id
    private String email;

    private Attendee(){}

    public Attendee(final String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return String.format(
            "Session[email='%s']",
            email);
    }

    public String getEmail() {
        return this.email;
    }
}
