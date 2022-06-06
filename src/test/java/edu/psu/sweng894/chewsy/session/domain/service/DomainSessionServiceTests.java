package edu.psu.sweng894.chewsy.session.domain.service;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;
import org.springframework.boot.test.context.SpringBootTest;

import edu.psu.sweng894.chewsy.session.domain.Attendee;
import edu.psu.sweng894.chewsy.session.domain.repository.ConciergeRepository;
import edu.psu.sweng894.chewsy.session.domain.repository.SessionRepository;
import edu.psu.sweng894.chewsy.session.infrastructure.repository.ConciergeAPI.MockConciergeAPIRepository;
import edu.psu.sweng894.chewsy.session.infrastructure.repository.PostgreSQLDB.MockPostgreSQLDBRepository;

@SpringBootTest
public class DomainSessionServiceTests {
    public final String email = "tam6190@psu.edu";
    public final String newEmail = "jon.doe@psu.edu";
    public final Attendee attendee = new Attendee(email);
    public final Attendee newAttendee = new Attendee(newEmail);
    public final SessionRepository sessionRepository = new MockPostgreSQLDBRepository();
    public final ConciergeRepository conciergeRepository = new MockConciergeAPIRepository();
    public final SessionService sessionService = new DomainSessionService(sessionRepository, conciergeRepository);
    public final UUID id = sessionService.createSession(attendee);
    public final List<Attendee> attendees = sessionService.getAttendees(id);

    @Test
    public void testCreateAttendee() {
        String actual = attendee.getEmail();
        assertEquals(email, actual);
    }
    
    @Test
    public void testCreateSession() {
        String actual = sessionService.getStatus(id).toString();
        assertEquals("CREATED", actual);
    }

    @Test
    public void testGetAttendees() {
        String actual = attendees.get(0).getEmail();
        assertEquals(email, actual);
    }

    @Test
    public void testAddAttendee() {
        sessionService.addAttendee(id, newAttendee);
        String actual = attendees.get(1).getEmail();
        assertEquals(newEmail, actual);
        System.out.println(attendees);
    }

    @Test
    public void testRemoveAttendee() {
        sessionService.addAttendee(id, newAttendee);
        sessionService.removeAttendee(id, email);
        String actual = attendees.get(0).getEmail();
        assertNotEquals(email, actual);
    }

    @Test
    public void testCompleteSession() {
        sessionService.completeSession(id);
        String actual = sessionService.getStatus(id).toString();
        assertEquals("COMPLETED", actual);
    }

    @Test
    public void testRestaurantList() {
        sessionService.addRestaurantList(id, "23666", 5);
        assertNotNull(sessionService.getRestaurantList(id));
        System.out.println(sessionService.getRestaurantList(id));
    }
}
