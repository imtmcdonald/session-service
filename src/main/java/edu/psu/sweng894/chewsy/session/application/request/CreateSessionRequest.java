package edu.psu.sweng894.chewsy.session.application.request;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.psu.sweng894.chewsy.session.domain.Attendee;

public class CreateSessionRequest {
    @NotNull private Attendee attendee;

    @JsonCreator
    public CreateSessionRequest(@JsonProperty("email") @NotNull final Attendee attendee ) {
        this.attendee = attendee;
    }

    public Attendee getAttendee() {
        return attendee;
    }
}
