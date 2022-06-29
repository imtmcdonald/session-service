package edu.psu.sweng894.chewsy.session.domain;

public class Message {
    private String recipient;
    private String message;
    private MessageStatus status;

    public Message() {
        this.status = MessageStatus.CREATED;
    }

    @Override
    public String toString() {
        return String.format(
            "Session[recipient=%s, status='%s', message='%s']",
            recipient, status, message);
    }

    public void setRecipient(String recipient) {
        validateState();
        this.recipient = recipient;
    }

    public void setMessage(String message) {
        validateState();
        this.message = message;
    }

    public void setStatusSent() {
        validateState();
        this.status = MessageStatus.SENT;
    }

    public void setStatusFailed() {
        validateState();
        this.status = MessageStatus.FAILED;
    }

    public String getRecipient() {
        return this.recipient;
    }

    public String getMessage() {
        return this.message;
    }

    public MessageStatus getStatus() {
        return this.status;
    }

    private void validateState() {
        if (MessageStatus.SENT.equals(status)) {
            throw new DomainException("The message is already sent.");
        }
    }
}
