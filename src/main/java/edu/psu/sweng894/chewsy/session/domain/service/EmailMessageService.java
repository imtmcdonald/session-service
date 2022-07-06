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
    public Message createMessage(String recipient, String message) {
        Message newMessage = new Message();

        newMessage.setRecipient(recipient);
        newMessage.setMessage(message);
        
        return newMessage;
    }

    @Override
    public void sendMessage(Message message) {
        MailjetRequest request;
        MailjetResponse response;
        String url = System.getenv("WEB_CLIENT_URL") + "/" + message.getMessage();

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
                    .put(Emailv31.Message.SUBJECT, "You are invited to a Chewsy session!")
                    .put(Emailv31.Message.TEXTPART, "Someone invited you to join them for a meal. Click the link to help them decide where to eat. " + url)
                    .put(Emailv31.Message.HTMLPART, "<h1>Someone invited you to join them for a meal.</h1><br /><h2>Help decide where to eat.</h2><br /><p><a href=\"" + url +"\">Click Here to Choose!</a></p>")));
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
