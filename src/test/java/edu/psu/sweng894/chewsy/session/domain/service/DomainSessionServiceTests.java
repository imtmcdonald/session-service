package edu.psu.sweng894.chewsy.session.domain.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import edu.psu.sweng894.chewsy.session.domain.Attendee;
import edu.psu.sweng894.chewsy.session.domain.Message;
import edu.psu.sweng894.chewsy.session.domain.MessageStatus;
import edu.psu.sweng894.chewsy.session.domain.Session;
import edu.psu.sweng894.chewsy.session.domain.SessionProvider;
import edu.psu.sweng894.chewsy.session.domain.client.VotingClient;
import edu.psu.sweng894.chewsy.session.domain.repository.AttendeeRepository;
import edu.psu.sweng894.chewsy.session.domain.repository.ConciergeRepository;
import edu.psu.sweng894.chewsy.session.domain.repository.SessionRepository;

import org.json.JSONArray;

public class DomainSessionServiceTests {

    private SessionRepository sessionRepository;
    private ConciergeRepository conciergeRepository;
    private AttendeeRepository attendeeRepository;
    private VotingClient votingClient;
    private MessageService messageService;
    private DomainSessionService classUnderTest;

    @BeforeEach
    public void setUp() {
        sessionRepository = mock(SessionRepository.class);
        conciergeRepository = mock(ConciergeRepository.class);
        attendeeRepository = mock(AttendeeRepository.class);
        votingClient = mock(VotingClient.class);
        messageService = mock(MessageService.class);

        classUnderTest = new DomainSessionService(sessionRepository, conciergeRepository, attendeeRepository, votingClient, messageService);
    }

    @Test
    public void shouldCreateSession_thenSaveIt() {
        Session newSession = new Session();

        when(sessionRepository.save(any(Session.class))).thenReturn(newSession);

        Long actual = classUnderTest.createSession();
        System.out.println(actual);

        verify(sessionRepository).save(any(Session.class));
    }

    // @Test
    // public void shouldCreateAttendee_thenSaveIt() {
    //     String email = "test@email.com";
    //     String name = "test";
    //     Attendee attendee = new Attendee(email, name);

    //     when(attendeeRepository.save(any(Attendee.class))).thenReturn(attendee);

    //     final String actual = classUnderTest.createAttendee(email, name);
    //     System.out.println(actual);

    //     verify(attendeeRepository).save(any(Attendee.class));
    //     assertNotNull(actual);
    // }

    @Test
    public void shouldFindSession_thenShowAttendees() {
        Long id = Long.parseLong("3");
        String email = "test@email.com";
        String name = "test";
        Session session = new Session();
        Attendee attendee = new Attendee(email, name, session);
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

        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));

        classUnderTest.addAttendee(session.getId(), email, name);

        verify(attendeeRepository).save(any());
    }

    @Test
    public void shouldRemoveAttendee_thenSaveIt() {
        Session session = spy(SessionProvider.getCreatedSession());
        String email = "test@email.com";
        String name = "test";
        Attendee attendee = new Attendee(email, name, session);

        when(attendeeRepository.findById(anyString())).thenReturn(Optional.of(attendee));
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));

        classUnderTest.removeAttendee(session.getId(), email);

        verify(sessionRepository).save(session);
        verify(session).removeAttendee(attendee);
    }

    @Test
    public void shouldCompleteSession_thenSetConsensus_thenSendConsensus_thenSaveIt() throws UnsupportedEncodingException, IOException {
        String location = "23666";
        int radius = 5;
        JSONObject consensusMessage = new JSONObject();
        String testConsensus = "test";
        String subject = "It's Time to Eat!";
        String textpart = "Voting is complete! Your group chose to eat at: " + testConsensus;
        String htmlpart = "<h1>Voting is complete!</h1><br /><h2>Your group chose to eat at: </h2><br /><p> " + testConsensus + "</p>";
        String recipient = "test@email.com";
        int voters = 1;
        Session session = spy(SessionProvider.getCreatedSession());
        Message message = new Message();
        String name = "testUser";
        Attendee attendee = new Attendee(recipient, name, session);
        final ArrayList<String> consensus = new ArrayList();

        JSONObject jo = new JSONObject();
        jo.put("NAME", "Burger King");
        jo.put("RATING", 3.4);
        jo.put("ADDRESS", "112 Jefferson Ave, Newport News VA 23601");

        JSONObject joNew = new JSONObject();
        joNew.put("NAME", "McDonalds");
        joNew.put("RATING", 2.8);
        joNew.put("ADDRESS", "123 Mercury Ave, Hampton VA 23666");

        JSONArray restaurantList = new JSONArray();
        restaurantList.put(jo);
        restaurantList.put(joNew);

        consensus.add("Burger King");

        JSONObject expected = new JSONObject();
        expected.put("count", consensus);       

        consensusMessage.put("subject", subject);
        consensusMessage.put("textpart", textpart);
        consensusMessage.put("htmlpart", htmlpart);

        message.setMessage(consensusMessage);
        message.setRecipient(recipient);

        when(conciergeRepository.getRestaurants(anyString(), anyInt())).thenReturn(restaurantList);
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        when(attendeeRepository.findById(anyString())).thenReturn(Optional.of(attendee));
        when(votingClient.getConsensus(session.getId(), voters)).thenReturn(expected);
        when(messageService.createMessage(recipient, consensusMessage)).thenReturn(message);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable{
                Message message = (Message) invocation.getArguments()[0];
                message.setStatusSent();

                assertEquals(MessageStatus.SENT, message.getStatus());
                return message;
            }
        }).when(messageService).sendMessage(message);
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));

        classUnderTest.addRestaurantList(session.getId(), location, radius);
        classUnderTest.addAttendee(session.getId(), recipient, name);
        classUnderTest.completeSession(session.getId());

        verify(sessionRepository, times(4)).save(any(Session.class));
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

    @Test
    public void shouldSetConsensus_thenSaveIt() throws UnsupportedEncodingException, IOException {
        int voters = 7;
        Session session = spy(SessionProvider.getCreatedSession());
        final ArrayList<String> consensus = new ArrayList();

        consensus.add("test");

        JSONObject expected = new JSONObject();
        expected.put("count", consensus);
        
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        when(votingClient.getConsensus(session.getId(), voters)).thenReturn(expected);

        classUnderTest.setConsensus(session.getId(), voters);

        verify(sessionRepository).save(any(Session.class));
        verify(session).setConsensus(anyString());
    }

    @Test
    public void shouldGetConsensus_thenReturnIt() throws UnsupportedEncodingException, IOException {
        String location = "23666";
        int radius = 5;
        int voters = 7;
        Session session = spy(SessionProvider.getCreatedSession());
        final ArrayList<String> consensus = new ArrayList();
        JSONObject jo = new JSONObject();
        jo.put("NAME", "Burger King");
        jo.put("RATING", "3.4");
        jo.put("LOCATION", "112 Jefferson Ave, Newport News VA 23601");

        JSONObject joNew = new JSONObject();
        joNew.put("NAME", "McDonalds");
        joNew.put("RATING", "2.8");
        joNew.put("LOCATION", "123 Mercury Ave, Hampton VA 23666");

        JSONArray restaurantList = new JSONArray();
        restaurantList.put(jo);
        restaurantList.put(joNew);

        consensus.add("Burger King");

        JSONObject expected = new JSONObject();
        expected.put("count", consensus);
        
        when(conciergeRepository.getRestaurants(anyString(), anyInt())).thenReturn(restaurantList);
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        when(votingClient.getConsensus(session.getId(), voters)).thenReturn(expected);

        classUnderTest.addRestaurantList(session.getId(), location, radius);
        classUnderTest.setConsensus(session.getId(), voters);

        JSONObject actual = classUnderTest.getConsensus(session.getId());

        verify(session).getConsensus();

        assertEquals(jo.toString(), actual.toString());
    }

    @Test
    public void shouldSendConsensus_thenVerifyIt() {
        JSONObject consensusMessage = new JSONObject();
        JSONObject consensus = new JSONObject();
        consensus.put("NAME", "Burger King");
        consensus.put("RATING", 3.4);
        consensus.put("ADDRESS", "112 Jefferson Ave, Newport News VA 23601");
        String subject = "It's Time to Eat!";
        String textpart = "Voting is complete! Your group chose to eat at " + consensus.getString("NAME") + 
            ".  It has a rating of " + consensus.getInt("RATING") + 
            ". The address is" + consensus.getString("ADDRESS") + ".";
        String htmlpart = "<h1>Voting is complete!</h1><br /><h2>Your group chose to eat at: </h2><br /><h3> " + consensus.getString("NAME") + 
            "</h3><br /><h4>The rating is: " + consensus.getInt("RATING") + 
            "</h4><br /><h4>The address is: " + consensus.getString("ADDRESS");
        String recipient = "test@email.com";
        Session session = spy(SessionProvider.getCreatedSession());
        Message message = new Message();
        String name = "testUser";
        Attendee attendee = new Attendee(recipient, name, session);

        consensusMessage.put("subject", subject);
        consensusMessage.put("textpart", textpart);
        consensusMessage.put("htmlpart", htmlpart);

        message.setMessage(consensusMessage);
        message.setRecipient(recipient);

        when(attendeeRepository.findById(anyString())).thenReturn(Optional.of(attendee));
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        when(messageService.createMessage(recipient, consensusMessage)).thenReturn(message);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable{
                Message message = (Message) invocation.getArguments()[0];
                message.setStatusSent();

                assertEquals(MessageStatus.SENT, message.getStatus());
                return message;
            }
        }).when(messageService).sendMessage(message);

        classUnderTest.addAttendee(session.getId(), recipient, name);
        classUnderTest.sendConsensus(session.getId(), consensus);

        assertEquals(consensusMessage, message.getMessage());
    }
}