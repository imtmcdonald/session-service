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
    private List<Attendee> attendees = new ArrayList<Attendee>();
    @Lob
    private String restaurantList;

    public Session() {
        this.status = SessionStatus.CREATED;
    }

    @Override
    public String toString() {
        return String.format(
            "Session[id=%d, status='%s', attendees='%s', restaurants='%s']",
            id, status, attendees, restaurantList);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStatusComplete() {
        validateState();
        this.status = SessionStatus.COMPLETED;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void addRestaurantList(String restaurantList) {
        validateState();
        this.restaurantList = restaurantList;
    }

    public String getRestaurantList() {
        return this.restaurantList;
    }

    public void addAttendee(final Attendee attendee) {
        validateState();
        this.attendees.add(attendee);
    }

    public void removeAttendee(final Attendee attendee) {
        validateState();
        this.attendees.remove(attendee);
    }

    public List<Attendee> getAttendeeList() {
        return Collections.unmodifiableList(attendees);
    }

    private void validateState() {
        if (SessionStatus.COMPLETED.equals(status)) {
            throw new DomainException("The session is in completed state.");
        }
    }
}
