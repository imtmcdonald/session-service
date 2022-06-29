package edu.psu.sweng894.chewsy.session.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        String message = "test";

        Message expected = new Message();
        expected.setRecipient(recipient);
        expected.setMessage(message);

        Message actual = classUnderTest.createMessage(recipient, message);

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void shouldGetStatus_thenReturnIt() {
        String recipient = "test@email.com";
        String message = "test";

        Message newMessage = classUnderTest.createMessage(recipient, message);
        MessageStatus actual = classUnderTest.getStatus(newMessage);

        MessageStatus expected = MessageStatus.CREATED;
        
        assertEquals(expected, actual);
    }
}
