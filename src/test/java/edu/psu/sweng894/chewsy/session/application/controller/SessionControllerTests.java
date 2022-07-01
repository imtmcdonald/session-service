package edu.psu.sweng894.chewsy.session.application.controller;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

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
        Long id = Long.parseLong("34");
        String email = "test@email.com";
        Attendee attendee = new Attendee(email);
        List <Attendee> attendees = new ArrayList<Attendee>();
        AddAttendeeRequest addAttendeeRequest = new AddAttendeeRequest(email);
        Message message = new Message();
        message.setMessage("test");
        message.setRecipient(email);
        
        attendees.add(attendee);

        when(sessionService.getAttendees(anyLong())).thenReturn(attendees);
        when(messageService.createMessage(anyString(), anyString())).thenReturn(message);
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
        Attendee attendee = new Attendee(email);
        List <Attendee> attendees = new ArrayList<Attendee>();
        AddAttendeeRequest addAttendeeRequest = new AddAttendeeRequest(email);
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
}
