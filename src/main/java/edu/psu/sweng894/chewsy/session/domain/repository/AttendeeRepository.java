package edu.psu.sweng894.chewsy.session.domain.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.psu.sweng894.chewsy.session.domain.Attendee;

@Repository
public interface AttendeeRepository extends CrudRepository<Attendee, String> {
    Optional<Attendee> findById(String email);
}
