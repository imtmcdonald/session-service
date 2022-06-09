package edu.psu.sweng894.chewsy.session.application.response;

import java.util.List;

import edu.psu.sweng894.chewsy.session.domain.Attendee;

public class GetAttendeesResponse {
    private final List<Attendee> attendees;

    public GetAttendeesResponse(final List<Attendee> attendees) {
        this.attendees = attendees;
    }

    public List<Attendee> getAttendees() {
        return attendees;
    }
}
