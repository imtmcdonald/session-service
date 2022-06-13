package edu.psu.sweng894.chewsy.session.domain.service;

import java.util.List;

import edu.psu.sweng894.chewsy.session.domain.Attendee;
import edu.psu.sweng894.chewsy.session.domain.SessionStatus;
import org.springframework.stereotype.Service;

@Service
public interface SessionService {
    String createAttendee(String email);

    Long createSession();

    void addRestaurantList(final Long id, final String location, final int radius);

    void addAttendee(Long id, String email);

    void completeSession(Long id);

    void removeAttendee(Long id, String email);

    String getRestaurantList(Long id);
    
    List<Attendee> getAttendees(Long id);

    SessionStatus getStatus(Long id);

    void setDuration(final Long id, final int duration);
}
