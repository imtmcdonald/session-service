package edu.psu.sweng894.chewsy.session.application.request;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.psu.sweng894.chewsy.session.domain.Attendee;

public class RemoveAttendeeRequest {
    @NotNull private Attendee attendee;

    @JsonCreator
    public RemoveAttendeeRequest(@JsonProperty("attendee") final Attendee attendee) {
        this.attendee = attendee;
    }

    public Attendee getAttendee() {
        return attendee;
    }    
}
