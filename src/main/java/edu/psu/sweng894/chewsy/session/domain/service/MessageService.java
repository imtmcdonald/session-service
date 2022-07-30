package edu.psu.sweng894.chewsy.session.domain.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import edu.psu.sweng894.chewsy.session.domain.Message;
import edu.psu.sweng894.chewsy.session.domain.MessageStatus;

@Service
public interface MessageService {
    public Message createMessage(String recipient, JSONObject message);
    
    void sendMessage(Message message);    

    MessageStatus getStatus(Message message);
}
