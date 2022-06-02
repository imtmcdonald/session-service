package edu.psu.sweng894.chewsy.session.application.request;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.psu.sweng894.chewsy.session.domain.Attendee;

public class AddAttendeeRequest {
    @NotNull private Attendee attendee;

    @JsonCreator
    public AddAttendeeRequest(@JsonProperty("attendee") final Attendee attendee) {
        this.attendee = attendee;
    }

    public Attendee getAttendee() {
        return attendee;
    }
}
