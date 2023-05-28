package edu.psu.sweng894.chewsy.session.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.psu.sweng894.chewsy.session.domain.Message;
import edu.psu.sweng894.chewsy.session.domain.MessageStatus;


public class EmailMessageServiceTests {
    private EmailMessageService classUnderTest;

    @BeforeEach
    public void setUp() {
        classUnderTest = new EmailMessageService();
    }

    @Test
    public void shouldCreateMessage_thenReturnIt() {
        String recipient = "test@email.com";
        String url = "test.com";
        JSONObject message = new JSONObject();
        String subject = "You are invited to a Chewsy session!";
        String textpart = "Someone invited you to join them for a meal. Click the link to help them decide where to eat. " + url;
        String htmlpart = "<h1>Someone invited you to join them for a meal.</h1><br /><h2>Help decide where to eat.</h2><br /><p><a href=\"" + url +"\">Click Here to Choose!</a></p>";
        message.put("subject", subject);
        message.put("textpart", textpart);
        message.put("htmlpart", htmlpart);

        Message expected = new Message();
        expected.setRecipient(recipient);
        expected.setMessage(message);

        Message actual = classUnderTest.createMessage(recipient, message);

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void shouldGetStatus_thenReturnIt() {
        String recipient = "test@email.com";
        String url = "test.com";
        JSONObject message = new JSONObject();
        String subject = "You are invited to a Chewsy session!";
        String textpart = "Someone invited you to join them for a meal. Click the link to help them decide where to eat. " + url;
        String htmlpart = "<h1>Someone invited you to join them for a meal.</h1><br /><h2>Help decide where to eat.</h2><br /><p><a href=\"" + url +"\">Click Here to Choose!</a></p>";
        message.put("subject", subject);
        message.put("textpart", textpart);
        message.put("htmlpart", htmlpart);

        Message newMessage = classUnderTest.createMessage(recipient, message);
        MessageStatus actual = classUnderTest.getStatus(newMessage);

        MessageStatus expected = MessageStatus.CREATED;
        
        assertEquals(expected, actual);
    }
}
