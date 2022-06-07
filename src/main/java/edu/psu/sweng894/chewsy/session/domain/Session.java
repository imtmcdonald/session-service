package edu.psu.sweng894.chewsy.session.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private SessionStatus status;
    @Column
    @ElementCollection
    private List<Attendee> attendees;
    @Lob
    private String restaurantList;

    protected Session() {}

    public Session(final String email) {
        this.status = SessionStatus.CREATED;
        this.attendees = new ArrayList<>(Collections.singletonList(new Attendee(email)));
    }

    @Override
    public String toString() {
        return String.format(
            "Session[id=%d, status='%s', attendees='%s']",
            id, status, attendees);
    }

    public void complete() {
        validateState();
        this.status = SessionStatus.COMPLETED;
    }

    public void addRestaurantList(String restaurantList) {
        validateState();
        this.restaurantList = restaurantList;
    }

    public void addAttendee(final String email) {
        validateState();
        validateEmail(email);
        attendees.add(new Attendee(email));
    }

    public void removeAttendee(final String email) {
        validateState();
        final Attendee attendee = getAttendee(email);
        attendees.remove(attendee);
    }

    private Attendee getAttendee(final String email) {
        return attendees.stream()
            .filter(attendee -> attendee.getEmail()
                .equals(email))
            .findFirst()
            .orElseThrow(() -> new DomainException("Attendee with " + email + " doesn't exist."));
    }

    private void validateState() {
        if (SessionStatus.COMPLETED.equals(status)) {
            throw new DomainException("The session is in completed state.");
        }
    }

    private void validateEmail(final String email) {
        if (email == null) {
            throw new DomainException("The email cannot be null.");
        }
    }

    public Long getId() {
        return id;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public List<Attendee> getAttendees() {
        return Collections.unmodifiableList(attendees);
    }

    public String getRestaurantList() {
        return this.restaurantList;
    }

}
