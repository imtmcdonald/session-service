package edu.psu.sweng894.chewsy.session.domain.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import edu.psu.sweng894.chewsy.session.domain.Attendee;
import edu.psu.sweng894.chewsy.session.domain.Session;
import edu.psu.sweng894.chewsy.session.domain.SessionProvider;
import edu.psu.sweng894.chewsy.session.domain.repository.AttendeeRepository;
import edu.psu.sweng894.chewsy.session.domain.repository.ConciergeRepository;
import edu.psu.sweng894.chewsy.session.domain.repository.SessionRepository;
import org.json.JSONArray;

public class DomainSessionServiceTests {

    private SessionRepository sessionRepository;
    private ConciergeRepository conciergeRepository;
    private AttendeeRepository attendeeRepository;
    private DomainSessionService classUnderTest;

    @BeforeEach
    public void setUp() {
        sessionRepository = mock(SessionRepository.class);
        conciergeRepository = mock(ConciergeRepository.class);
        attendeeRepository = mock(AttendeeRepository.class);
        classUnderTest = new DomainSessionService(sessionRepository, conciergeRepository, attendeeRepository);
    }

    @Test
    public void shouldCreateSession_thenSaveIt() {
        Session newSession = new Session();

        when(sessionRepository.save(any(Session.class))).thenReturn(newSession);

        Long actual = classUnderTest.createSession();
        System.out.println(actual);

        verify(sessionRepository).save(any(Session.class));
    }

    @Test
    public void shouldCreateAttendee_thenSaveIt() {
        String email = "test@email.com";
        String name = "test";
        Attendee attendee = new Attendee(email, name);

        when(attendeeRepository.save(any(Attendee.class))).thenReturn(attendee);

        final String actual = classUnderTest.createAttendee(email, name);
        System.out.println(actual);

        verify(attendeeRepository).save(any(Attendee.class));
        assertNotNull(actual);
    }

    @Test
    public void shouldFindSession_thenShowAttendees() {
        Long id = Long.parseLong("3");
        String email = "test@email.com";
        String name = "test";
        Session session = new Session();
        Attendee attendee = new Attendee(email, name);
        session.addAttendee(attendee);

        when(sessionRepository.findById(any())).thenReturn(Optional.of(session));

        List<Attendee> actual = classUnderTest.getAttendees(id);

        System.out.println(actual);
        assertNotNull(actual);
    }

    @Test
    public void shouldAddAttendee_thenSaveIt() {
        Session session = spy(SessionProvider.getCreatedSession());
        String email = "test@email.com";
        String name = "test";
        Attendee attendee = new Attendee(email, name);

        when(attendeeRepository.findById(anyString())).thenReturn(Optional.of(attendee));
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));

        classUnderTest.addAttendee(session.getId(), email);

        verify(sessionRepository).save(session);
        verify(session).addAttendee(attendee);
    }

    @Test
    public void shouldRemoveAttendee_thenSaveIt() {
        Session session = spy(SessionProvider.getCreatedSession());
        String email = "test@email.com";
        String name = "test";
        Attendee attendee = new Attendee(email, name);

        when(attendeeRepository.findById(anyString())).thenReturn(Optional.of(attendee));
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));

        classUnderTest.removeAttendee(session.getId(), email);

        verify(sessionRepository).save(session);
        verify(session).removeAttendee(attendee);
    }

    @Test
    public void shouldCompleteSession_thenSaveIt() {
        Session session = spy(SessionProvider.getCreatedSession());
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));

        classUnderTest.completeSession(session.getId());

        verify(sessionRepository).save(any(Session.class));
        verify(session).setStatusComplete();
    }

    @Test
    public void shouldGetRestaurantList_thenSaveIt() throws UnsupportedEncodingException, IOException {
        Session session = spy(SessionProvider.getCreatedSession());
        String location = "23666";
        int radius = 5;

        JSONObject jo = new JSONObject();
        jo.put("NAME", "Burger King");
        jo.put("RATING", "3.4");
        jo.put("LOCATION", "112 Jefferson Ave, Newport News VA 23601");

        JSONArray restaurantList = new JSONArray();
        restaurantList.put(jo);

        when(conciergeRepository.getRestaurants(anyString(), anyInt())).thenReturn(restaurantList);
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));

        classUnderTest.addRestaurantList(session.getId(), location, radius);

        verify(sessionRepository).save(any(Session.class));
        verify(conciergeRepository).getRestaurants(anyString(), anyInt());
    }

    @Test
    public void shouldSetDuration_thenSaveIt() {
        int duration = 7;
        Session session = spy(SessionProvider.getCreatedSession());
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));

        classUnderTest.setDuration(session.getId(), duration);

        verify(sessionRepository).save(any(Session.class));
        verify(session).setDuration(anyInt());
    }
}