package edu.psu.sweng894.chewsy.session.domain;

import static org.junit.Assert.fail;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Attendee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;    

    private Attendee(){}

    public Attendee(final String email, final String name, final Session session) {
        this.email = email;
        this.name = name;
        this.session = session;
    }

    @Override
    public String toString() {
        return String.format(
            "Attendee[id=%d, email='%s', name='%s', session='%s']",
            id, email, name, session);
    }

    public String getEmail() {
        return this.email;
    }

    public String getName() {
        return this.name;
    }

    public Session getSession() {
        return this.session;
    }
}
