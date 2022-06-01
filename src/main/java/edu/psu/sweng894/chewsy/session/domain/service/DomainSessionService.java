package edu.psu.sweng894.chewsy.session.domain.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;

import edu.psu.sweng894.chewsy.session.domain.Attendee;
import edu.psu.sweng894.chewsy.session.domain.Session;
import edu.psu.sweng894.chewsy.session.domain.SessionStatus;
import edu.psu.sweng894.chewsy.session.domain.repository.ConciergeRepository;
import edu.psu.sweng894.chewsy.session.domain.repository.SessionRepository;

public class DomainSessionService implements SessionService {

    private final SessionRepository sessionRepository;
    private final ConciergeRepository conciergeRepository;

    public DomainSessionService(final SessionRepository sessionRepository, final ConciergeRepository conciergeRepository) {
        this.sessionRepository = sessionRepository;
        this.conciergeRepository = conciergeRepository;
    }

    @Override
    public UUID createSession(final Attendee attendee) {
        final Session session = new Session(UUID.randomUUID(), attendee.getEmail());
        sessionRepository.save(session);

        return session.getId();
    }

    public JSONArray getRestaurantList(final UUID id) {
        final Session session = getSession(id);

        return session.getRestaurantList();
    }

    public List<Attendee> getAttendees(final UUID id) {
        final Session session = getSession(id);

        return session.getAttendees();
    }

    public SessionStatus getStatus(final UUID id) {
        final Session session = getSession(id);

        return session.getStatus();
    }

    @Override
    public void addRestaurantList(final UUID id, final String location, final int radius) {
        final Session session = getSession(id);
        try {
            session.addRestaurantList(conciergeRepository.getRestaurants(location, radius));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        sessionRepository.save(session);
    }

    @Override
    public void addAttendee(final UUID id, final Attendee attendee) {
        final Session session = getSession(id);
        session.addAttendee(attendee.getEmail());

        sessionRepository.save(session);
    }

    @Override
    public void completeSession(final UUID id) {
        final Session session = getSession(id);
        session.complete();

        sessionRepository.save(session);
    }

    @Override
    public void removeAttendee(final UUID id, final String email) {
        final Session session = getSession(id);
        session.removeAttendee(email);

        sessionRepository.save(session);
    }

    private Session getSession(UUID id) {
        return sessionRepository
          .findById(id)
          .orElseThrow(() -> new RuntimeException("Session with given id doesn't exist"));
    }
}
