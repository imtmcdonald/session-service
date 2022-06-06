package edu.psu.sweng894.chewsy.session.application.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import edu.psu.sweng894.chewsy.session.application.request.AddAttendeeRequest;
import edu.psu.sweng894.chewsy.session.application.request.CreateSessionRequest;
import edu.psu.sweng894.chewsy.session.application.request.RemoveAttendeeRequest;
import edu.psu.sweng894.chewsy.session.application.response.CreateSessionResponse;
import edu.psu.sweng894.chewsy.session.application.response.GetAttendeesResponse;
import edu.psu.sweng894.chewsy.session.domain.Attendee;
import edu.psu.sweng894.chewsy.session.domain.repository.ConciergeRepository;
import edu.psu.sweng894.chewsy.session.domain.repository.SessionRepository;
import edu.psu.sweng894.chewsy.session.domain.service.DomainSessionService;
import edu.psu.sweng894.chewsy.session.domain.service.SessionService;
import edu.psu.sweng894.chewsy.session.infrastructure.repository.ConciergeAPI.MockConciergeAPIRepository;
import edu.psu.sweng894.chewsy.session.infrastructure.repository.PostgreSQLDB.MockPostgreSQLDBRepository;

@SpringBootTest
public class SessionControllerTests {
    public final String email = "tam6190@psu.edu";
    public final String newEmail = "jon.doe@psu.edu";
    public final Attendee attendee = new Attendee(email);
    public final Attendee newAttendee = new Attendee(newEmail);
    public final SessionRepository sessionRepository = new MockPostgreSQLDBRepository();
    public final ConciergeRepository conciergeRepository = new MockConciergeAPIRepository();
    public final SessionService sessionService = new DomainSessionService(sessionRepository, conciergeRepository);
    public final SessionController sessionController = new SessionController(sessionService);
    public final CreateSessionRequest createSessionRequest = new CreateSessionRequest(attendee);
    public final CreateSessionResponse session = sessionController.createSession(createSessionRequest);

    @Test
    public void testSessionController_CreateSession() {
        CreateSessionResponse actual = sessionController.createSession(createSessionRequest);
        assertNotNull(actual.getId());
    }

    @Test
    public void testSessionController_getRestaurant() {
        String actual = sessionController.getSessionStatus(session.getId());
        assertEquals("CREATED", actual);
    }

    @Test
    public void testSessionController_GetAttendees() {
        GetAttendeesResponse actual = sessionController.getAttendee(session.getId());
        assertEquals(email, actual.getAttendees().get(0).getEmail());
    }

    @Test
    public void testAddAttendee() {
        AddAttendeeRequest request = new AddAttendeeRequest(newAttendee);
        sessionController.addAttendee(session.getId(), request);
        GetAttendeesResponse actual = sessionController.getAttendee(session.getId());
        assertEquals(newEmail, actual.getAttendees().get(1).getEmail());
    }

    @Test
    public void testSessionController_RemoveAttendee() {
        AddAttendeeRequest addAttendeeRequest = new AddAttendeeRequest(newAttendee);
        RemoveAttendeeRequest request = new RemoveAttendeeRequest(attendee);
        sessionController.addAttendee(session.getId(), addAttendeeRequest);
        sessionController.removeAttendee(session.getId(), request);
        GetAttendeesResponse actual = sessionController.getAttendee(session.getId());
        assertEquals(newEmail, actual.getAttendees().get(0).getEmail());
    }

    @Test
    public void testCompleteSession() {
        sessionController.completeSession(session.getId());
        String actual = sessionController.getSessionStatus(session.getId()).toString();
        assertEquals("COMPLETED", actual);
    }

    // @Test
    // public void testRestaurantList() {
    //     sessionService.addRestaurantList(id, "23666", 5);
    //     assertNotNull(sessionService.getRestaurantList(id));
    //     System.out.println(sessionService.getRestaurantList(id));
    // }

}
