package edu.psu.sweng894.chewsy.session.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import edu.psu.sweng894.chewsy.session.domain.Session;

@Repository
public interface SessionRepository {
    Optional<Session> findById(UUID id);

    void save(Session session);    
}
