package edu.psu.sweng894.chewsy.session.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MessageTests {
    private Message classUnderTest;
    String recipient = "test@email.com";
    String message = "test";

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
        classUnderTest.setMessage(message);
        String actual = classUnderTest.getMessage();

        assertEquals(message, actual);
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
