package edu.psu.sweng894.chewsy.session.infrastructure.repository.MockPostgreSQLDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import edu.psu.sweng894.chewsy.session.domain.Session;
import edu.psu.sweng894.chewsy.session.domain.repository.SessionRepository;

@Repository
public class MockPostgreSQLDBRepository implements SessionRepository {
    private List<Session> sessions = new ArrayList<>();

    @Override
    public Optional<Session> findById(UUID id) {
        Optional<Session> search = Optional.ofNullable(sessions.stream()
            .filter(session -> id.equals(session.getId()))
            .findAny()
            .orElse(null));
            
        return search;
    }

    @Override
    public void save(Session session) {
        sessions.add(session);
        System.out.println("Saved session: " + session);       
    }
    
}
