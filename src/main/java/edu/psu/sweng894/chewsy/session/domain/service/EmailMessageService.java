package edu.psu.sweng894.chewsy.session.domain.service;

import org.springframework.stereotype.Service;

import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.resource.Emailv31;

import edu.psu.sweng894.chewsy.session.domain.Message;
import edu.psu.sweng894.chewsy.session.domain.MessageStatus;

import org.json.JSONArray;
import org.json.JSONObject;

@Service
public class EmailMessageService implements MessageService {
    private final MailjetClient client;

    public EmailMessageService() {
        ClientOptions clientOptions = ClientOptions.builder()
            .apiKey(System.getenv("MJ_APIKEY_PUBLIC"))
            .apiSecretKey(System.getenv("MJ_APIKEY_PRIVATE"))
            .build();
        this.client = new MailjetClient(clientOptions);
    }

    @Override
    public Message createMessage(String recipient, JSONObject message) {
        Message newMessage = new Message();

        newMessage.setRecipient(recipient);
        newMessage.setMessage(message);
        
        return newMessage;
    }

    @Override
    public void sendMessage(Message message) {
        MailjetRequest request;
        MailjetResponse response;

        request = new MailjetRequest(Emailv31.resource)
        .property(Emailv31.MESSAGES, new JSONArray()
                .put(new JSONObject()
                    .put(Emailv31.Message.FROM, new JSONObject()
                        .put("Email", "timmc9104@gmail.com")
                        .put("Name", "Chewsy Eater"))
                    .put(Emailv31.Message.TO, new JSONArray()
                        .put(new JSONObject()
                            .put("Email", message.getRecipient())
                            .put("Name", "Friend")))
                    .put(Emailv31.Message.SUBJECT, message.getMessage().get("subject"))
                    .put(Emailv31.Message.TEXTPART, message.getMessage().get("textpart"))
                    .put(Emailv31.Message.HTMLPART, message.getMessage().get("htmlpart"))));
        try {
            response = client.post(request);
            
            System.out.println(response.getStatus());
            System.out.println(response.getData());

            message.setStatusSent();
        } catch (MailjetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            message.setStatusFailed();
        }
    }

    @Override
    public MessageStatus getStatus(Message message) {
        return message.getStatus();
    }
}
