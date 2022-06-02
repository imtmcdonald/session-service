package edu.psu.sweng894.chewsy.session.domain.service;

import java.util.List;
import java.util.UUID;

import org.json.JSONArray;

import edu.psu.sweng894.chewsy.session.domain.Attendee;
import edu.psu.sweng894.chewsy.session.domain.SessionStatus;
import org.springframework.stereotype.Service;

@Service
public interface SessionService {
    UUID createSession(Attendee attendee);

    void addRestaurantList(final UUID id, final String location, final int radius);

    void addAttendee(UUID id, Attendee attendee);

    void completeSession(UUID id);

    void removeAttendee(UUID id, String email);

    String getRestaurantList(UUID id);
    
    List<Attendee> getAttendees(UUID id);

    SessionStatus getStatus(UUID id);
}
