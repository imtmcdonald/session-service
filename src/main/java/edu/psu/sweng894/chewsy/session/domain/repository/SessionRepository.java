package edu.psu.sweng894.chewsy.session.domain.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.psu.sweng894.chewsy.session.domain.Session;

@Repository
public interface SessionRepository extends CrudRepository<Session, Long> {
    Optional<Session> findById(Long id);   
}
