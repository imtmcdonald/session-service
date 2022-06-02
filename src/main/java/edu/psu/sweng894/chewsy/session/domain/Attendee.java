package edu.psu.sweng894.chewsy.session.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Attendee {
    private final String email;

    public Attendee(final String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }
}
