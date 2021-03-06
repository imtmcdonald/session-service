package edu.psu.sweng894.chewsy.session.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

@Entity
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.ORDINAL)
    private SessionStatus status;
    private LocalDate startDate;
    @Column
    private int duration;
    @Column
    @OneToMany(mappedBy = "session", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Attendee> attendees = new ArrayList<Attendee>();
    @Lob
    private String restaurantList;
    @Lob
    private String consensus;

    public Session() {
        this.status = SessionStatus.CREATED;
        this.startDate = LocalDate.now();
        this.duration = 7;

    }

    @Override
    public String toString() {
        return String.format(
            "Session[id=%d, status='%s', restaurants='%s', startDate='%s', duration=%d, consensus='%s']",
            id, status, restaurantList, startDate, duration, consensus);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDuration(int duration) {
        validateState();
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public LocalDate getStartDate() {
        return startDate;
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

    public void setConsensus(String consensus) {
        validateState();
        this.consensus = consensus;
    }

    public String getConsensus() {
        return this.consensus;
    }

    public void setStatusExpired() {
        validateState();
        this.status = SessionStatus.EXPIRED;
    }

    private void validateState() {
        if (SessionStatus.COMPLETED.equals(status)) {
            throw new DomainException("The session is in completed state.");
        }
    }
}
