package edu.psu.sweng894.chewsy.session.application.controller;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import edu.psu.sweng894.chewsy.session.application.request.AddAttendeeRequest;
import edu.psu.sweng894.chewsy.session.application.request.CreateSessionRequest;
import edu.psu.sweng894.chewsy.session.application.request.RemoveAttendeeRequest;
import edu.psu.sweng894.chewsy.session.application.response.CreateSessionResponse;
import edu.psu.sweng894.chewsy.session.application.response.GetAttendeesResponse;
import edu.psu.sweng894.chewsy.session.domain.Attendee;
import edu.psu.sweng894.chewsy.session.domain.Message;
import edu.psu.sweng894.chewsy.session.domain.Session;
import edu.psu.sweng894.chewsy.session.domain.SessionProvider;
import edu.psu.sweng894.chewsy.session.domain.SessionStatus;
import edu.psu.sweng894.chewsy.session.domain.service.MessageService;
import edu.psu.sweng894.chewsy.session.domain.service.SessionService;

public class SessionControllerTests {
    private SessionController classUnderTest;
    private SessionService sessionService;
    private MessageService messageService;

    @BeforeEach
    public void setUp() {
        sessionService = mock(SessionService.class);
        messageService = mock(MessageService.class);
        classUnderTest = new SessionController(sessionService, messageService);
    }

    @Test
    public void shouldCreateSession_thenReturnIt() {
        int duration = 3;
        String location = "23666";
        int radius = 5;
        CreateSessionRequest createSessionRequest = new CreateSessionRequest(location, radius, duration);
        CreateSessionResponse actual = classUnderTest.createSession(createSessionRequest);

        assertNotNull(actual);
    }

    @Test
    public void shouldGetSessionStatus_thenReturnStatusCreated() {
        when(sessionService.getStatus(anyLong())).thenReturn(SessionStatus.CREATED);

        Long id = Long.parseLong("34");
        String actual = classUnderTest.getSessionStatus(id);
        String expected = "CREATED";

        assertEquals(expected, actual);
    }

    @Test
    public void shouldRequestAttendeeList_thenReturnAttendeeList() {
        Long id = Long.parseLong("34");
        List <Attendee> attendees = new ArrayList<Attendee>();

        when(sessionService.getAttendees(anyLong())).thenReturn(attendees);

        GetAttendeesResponse actual = classUnderTest.getAttendeeList(id);

        assertEquals(attendees, actual.getAttendees());
    }

    @Test
    public void shouldAddAttendee_thenValidateInAttendeeList() {
        JSONObject consensusMessage = new JSONObject();
        String consensus = "test";
        String subject = "It's Time to Eat!";
        String textpart = "Voting is complete! Your group chose to eat at: " + consensus;
        String htmlpart = "<h1>Voting is complete!</h1><br /><h2>Your group chose to eat at: </h2><br /><p> " + consensus + "</p>";
        Long id = Long.parseLong("34");
        String email = "test@email.com";
        String name = "test";
        Attendee attendee = new Attendee(email, name);
        List <Attendee> attendees = new ArrayList<Attendee>();
        AddAttendeeRequest addAttendeeRequest = new AddAttendeeRequest(email, name);
        Message message = new Message();

        consensusMessage.put("subject", subject);
        consensusMessage.put("textpart", textpart);
        consensusMessage.put("htmlpart", htmlpart);
        message.setMessage(consensusMessage);
        message.setRecipient(email);
        
        attendees.add(attendee);

        when(sessionService.getAttendees(anyLong())).thenReturn(attendees);
        when(messageService.createMessage(email, consensusMessage)).thenReturn(message);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable{
                Message message = (Message) invocation.getArguments()[0];
                message.setStatusSent();
                return null;
            }
        }).when(messageService).sendMessage(message);
     
        classUnderTest.addAttendee(id, addAttendeeRequest);

        GetAttendeesResponse actual = classUnderTest.getAttendeeList(id);

        assertEquals(email, actual.getAttendees().get(0).getEmail());
    }

    @Test
    public void shouldRemoveAttendee_thenValidateNotInAttendeeList() {
        Long id = Long.parseLong("34");
        String email = "test@email.com";
        String name = "test";
        Attendee attendee = new Attendee(email, name);
        List <Attendee> attendees = new ArrayList<Attendee>();
        AddAttendeeRequest addAttendeeRequest = new AddAttendeeRequest(email, name);
        RemoveAttendeeRequest removeAttendeeRequest = new RemoveAttendeeRequest(email);
        
        attendees.add(attendee);

        when(sessionService.getAttendees(anyLong())).thenReturn(attendees);

        classUnderTest.addAttendee(id, addAttendeeRequest);

        GetAttendeesResponse attendeeList = classUnderTest.getAttendeeList(id);

        assertEquals(email, attendeeList.getAttendees().get(0).getEmail());

        attendees.remove(attendee);
        classUnderTest.removeAttendee(id, removeAttendeeRequest);

        GetAttendeesResponse actual = classUnderTest.getAttendeeList(id);

        assertArrayEquals(attendees.toArray(), actual.getAttendees().toArray());
    }

    @Test
    public void shouldCompleteSession_theVerifyIt() {
        when(sessionService.getStatus(anyLong())).thenReturn(SessionStatus.COMPLETED);

        Long id = Long.parseLong("34");
        classUnderTest.completeSession(id);

        String actual = classUnderTest.getSessionStatus(id);
        String expected = "COMPLETED";

        assertEquals(expected, actual);
    }

    @Test
    public void shouldCheckSessionExpirations_whenExpired_thenCompleteSession() {
        Session session = spy(SessionProvider.getCreatedSession());

        List<Session> sessions = new ArrayList<>();
        sessions.add(session);

        when(sessionService.getSessions()).thenReturn(sessions);
        when(sessionService.getStatus(session.getId())).thenReturn(SessionStatus.EXPIRED);
        
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable{
                Session session = (Session) invocation.getArguments()[0];
                session.setStatusExpired();

                assertEquals(SessionStatus.EXPIRED, session.getStatus());

                return session;
            }
        }).when(sessionService).checkExpiration(anyLong());

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable{
                Session session = (Session) invocation.getArguments()[0];
                session.setStatusComplete();

                assertEquals(SessionStatus.COMPLETED, session.getStatus());

                return session;
            }
        }).when(sessionService).completeSession(anyLong());
        
        classUnderTest.checkSessionExpirations();        
    }
}
