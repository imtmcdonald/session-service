package edu.psu.sweng894.chewsy.session.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



public class MessageTests {
    private Message classUnderTest;
    String recipient = "test@email.com";

    @BeforeEach
    public void setUp() {
        classUnderTest = new Message();
    }

    @Test
    public void shouldSetRecipient_thenReturnIt() {
        classUnderTest.setRecipient(recipient);
        String actual = classUnderTest.getRecipient();

        assertEquals(recipient, actual);
    }
    
    @Test
    public void shouldSetMessage_thenReturnIt() {
        JSONObject expected = new JSONObject();
        String url = "test.com";
        String subject = "You are invited to a Chewsy session!";
        String textpart = "Someone invited you to join them for a meal. Click the link to help them decide where to eat. " + url;
        String htmlpart = "<h1>Someone invited you to join them for a meal.</h1><br /><h2>Help decide where to eat.</h2><br /><p><a href=\"" + url +"\">Click Here to Choose!</a></p>";
        expected.put("subject", subject);
        expected.put("textpart", textpart);
        expected.put("htmlpart", htmlpart);
        classUnderTest.setMessage(expected);

        JSONObject actual = classUnderTest.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnStatusCreated_whenCreated() {
        MessageStatus actual = classUnderTest.getStatus();
        MessageStatus status = MessageStatus.CREATED;

        assertEquals(status, actual);
    }

    @Test
    public void shouldSetStatusSent_thenReturnIt() {
        classUnderTest.setStatusSent();
        MessageStatus actual = classUnderTest.getStatus();
        MessageStatus status = MessageStatus.SENT;

        assertEquals(status, actual);
    }

    @Test
    public void shouldSetStatusFailed_thenReturnIt() {
        classUnderTest.setStatusFailed();
        MessageStatus actual = classUnderTest.getStatus();
        MessageStatus status = MessageStatus.FAILED;

        assertEquals(status, actual);
    }
}
