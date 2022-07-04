package edu.psu.sweng894.chewsy.session.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Attendee {
    @Id
    private String email;
    private String name;

    private Attendee(){}

    public Attendee(final String email, final String name) {
        this.email = email;
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format(
            "Session[email='%s', name='%s']",
            email, name);
    }

    public String getEmail() {
        return this.email;
    }

    public String getName() {
        return this.name;
    }
}
