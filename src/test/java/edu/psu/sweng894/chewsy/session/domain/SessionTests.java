package edu.psu.sweng894.chewsy.session.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SessionTests {
    String email = "test@email.com";
    String testEmail = "jon.doe@email.com";
    Long id = Long.parseLong("1234");
    Session testSession = new Session(email);
    List<Attendee> expected = new ArrayList<>();

    @Test
    public void TestStatusCreated() {
        final Session session = SessionProvider.getCreatedSession();
        assertEquals(SessionStatus.CREATED, session.getStatus());
    }

    @Test
    public void TestGetId() {
        Long actual = testSession.getId();
        Long expected = id;
        assertEquals(expected, actual);
    }

    @Test
    public void TestAddAttendee() {      
        testSession.addAttendee(testEmail);
        List<Attendee> actual = testSession.getAttendees();

        assertEquals(testEmail, actual.get(1).getEmail());
    }

    @Test
    public void TestRemoveAttendee() {
        testSession.addAttendee(testEmail);
        testSession.removeAttendee(email);
        List<Attendee> actual = testSession.getAttendees();

        assertNotEquals(email, actual.get(0).getEmail());
    }

    @Test
    public void TestStatusComplete() {
        testSession.complete();
        SessionStatus actual = testSession.getStatus();
        SessionStatus expected = SessionStatus.COMPLETED;
        assertEquals(expected, actual);
    }
}
