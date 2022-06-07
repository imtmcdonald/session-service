package edu.psu.sweng894.chewsy.session.domain.service;

import java.io.IOException;
import java.util.List;

import edu.psu.sweng894.chewsy.session.domain.Attendee;
import edu.psu.sweng894.chewsy.session.domain.Session;
import edu.psu.sweng894.chewsy.session.domain.SessionStatus;
import edu.psu.sweng894.chewsy.session.domain.repository.AttendeeRepository;
import edu.psu.sweng894.chewsy.session.domain.repository.ConciergeRepository;
import edu.psu.sweng894.chewsy.session.domain.repository.SessionRepository;
import org.springframework.stereotype.Service;

@Service
public class DomainSessionService implements SessionService {

    private final SessionRepository sessionRepository;
    private final ConciergeRepository conciergeRepository;
    private final AttendeeRepository attendeeRepository;

    public DomainSessionService(final SessionRepository sessionRepository, final ConciergeRepository conciergeRepository, final AttendeeRepository attendeeRepository) {
        this.sessionRepository = sessionRepository;
        this.conciergeRepository = conciergeRepository;
        this.attendeeRepository = attendeeRepository;
    }

    @Override
    public String createAttendee(final String email) {
        final Attendee attendee = new Attendee(email);
        attendeeRepository.save(attendee);

        return attendee.getEmail();
    }

    @Override
    public Long createSession(final Attendee attendee) {
        final Session session = new Session(attendee.getEmail());
        sessionRepository.save(session);

        return session.getId();
    }

    public String getRestaurantList(final Long id) {
        final Session session = getSession(id);

        return session.getRestaurantList();
    }

    public List<Attendee> getAttendees(final Long id) {
        final Session session = getSession(id);

        return session.getAttendees();
    }

    public SessionStatus getStatus(final Long id) {
        final Session session = getSession(id);

        return session.getStatus();
    }

    @Override
    public void addRestaurantList(final Long id, final String location, final int radius) {
        final Session session = getSession(id);
        try {
            session.addRestaurantList(conciergeRepository.getRestaurants(location, radius).toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        sessionRepository.save(session);
    }

    @Override
    public void addAttendee(final Long id, final Attendee attendee) {
        final Session session = getSession(id);
        session.addAttendee(attendee.getEmail());

        sessionRepository.save(session);
    }

    @Override
    public void completeSession(final Long id) {
        final Session session = getSession(id);
        session.complete();

        sessionRepository.save(session);
    }

    @Override
    public void removeAttendee(final Long id, final String email) {
        final Session session = getSession(id);
        session.removeAttendee(email);

        sessionRepository.save(session);
    }

    private Session getSession(Long id) {
        return sessionRepository
          .findById(id)
          .orElseThrow(() -> new RuntimeException("Session with given id doesn't exist"));
    }
}
