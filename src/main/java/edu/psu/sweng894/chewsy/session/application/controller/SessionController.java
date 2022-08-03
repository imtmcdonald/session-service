package edu.psu.sweng894.chewsy.session.application.controller;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import edu.psu.sweng894.chewsy.session.application.request.AddAttendeeRequest;
import edu.psu.sweng894.chewsy.session.application.request.CreateSessionRequest;
import edu.psu.sweng894.chewsy.session.application.request.RemoveAttendeeRequest;
import edu.psu.sweng894.chewsy.session.application.response.CreateSessionResponse;
import edu.psu.sweng894.chewsy.session.application.response.GetAttendeesResponse;
import edu.psu.sweng894.chewsy.session.application.response.GetRestaurantListResponse;
import edu.psu.sweng894.chewsy.session.domain.Attendee;
import edu.psu.sweng894.chewsy.session.domain.Session;
import edu.psu.sweng894.chewsy.session.domain.SessionStatus;
import edu.psu.sweng894.chewsy.session.domain.service.MessageService;
import edu.psu.sweng894.chewsy.session.domain.service.SessionService;

@RestController
@RequestMapping("/sessions")
public class SessionController {
    private final SessionService sessionService;
    private final MessageService messageService;
    
    @Autowired
    public SessionController(SessionService sessionService, MessageService messageService) {
        this.sessionService = sessionService;
        this.messageService = messageService;
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/create_session", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    CreateSessionResponse createSession(@RequestBody final CreateSessionRequest createSessionRequest) {
        final Long id = sessionService.createSession();
        sessionService.setDuration(id, createSessionRequest.getDuration());
        sessionService.addRestaurantList(id, createSessionRequest.getLocation(), createSessionRequest.getRadius());

        return new CreateSessionResponse(id);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/{id}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    String getSessionStatus(@PathVariable final Long id) {
        final SessionStatus sessionStatus = sessionService.getStatus(id);

        System.out.println(sessionStatus);

        return sessionStatus.toString();
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/{id}/status/complete")
    void completeSession(@PathVariable final Long id) {
        sessionService.completeSession(id);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/{id}/attendees", produces = MediaType.APPLICATION_JSON_VALUE)
    GetAttendeesResponse getAttendeeList(@PathVariable final Long id) {
        final List<Attendee> attendees = sessionService.getAttendees(id);

        return new GetAttendeesResponse(attendees);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/{id}/attendees", consumes = MediaType.APPLICATION_JSON_VALUE)
    void addAttendee(@PathVariable final Long id, @RequestBody final AddAttendeeRequest addAttendeeRequest) {
        String url = System.getenv("WEB_CLIENT_URL") + "/joingroup/" + id + "/" + addAttendeeRequest.getEmail();
        JSONObject message = new JSONObject();
        String subject = "You are invited to a Chewsy session!";
        String textpart = "Someone invited you to join them for a meal. Click the link to help them decide where to eat. " + url;
        String htmlpart = "<h1>Someone invited you to join them for a meal.</h1><br /><h2>Help decide where to eat.</h2><br /><p><a href=\"" + url +"\">Click Here to Choose!</a></p>";

        message.put("subject", subject);
        message.put("textpart", textpart);
        message.put("htmlpart", htmlpart);

        sessionService.addAttendee(id, addAttendeeRequest.getEmail(), addAttendeeRequest.getName());
        messageService.sendMessage(messageService.createMessage(addAttendeeRequest.getEmail(), message));
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping(value = "/{id}/attendees", consumes = MediaType.APPLICATION_JSON_VALUE)
    void removeAttendee(@PathVariable final Long id, @RequestBody final RemoveAttendeeRequest removeAttendeeRequest) {
        sessionService.removeAttendee(id, removeAttendeeRequest.getEmail());
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/{id}/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
    public GetRestaurantListResponse getRestaurantList(@PathVariable final Long id) {
        final String restaurants = sessionService.getRestaurantList(id);
        System.out.println(restaurants);
        GetRestaurantListResponse response = new GetRestaurantListResponse(restaurants);

        System.out.println(response);
        return response;
    }

    @Scheduled(cron="0 0 0 * * *")
    void checkSessionExpirations() {     
        List<Session> sessions = sessionService.getSessions();

        for (int i = 0; i < sessions.size(); i++) {
            if (! sessionService.getStatus(sessions.get(i).getId()).equals(SessionStatus.COMPLETED)) {
                System.out.println(sessions.get(i).getId());
                System.out.println(sessionService.getStatus(sessions.get(i).getId()));
                sessionService.checkExpiration(sessions.get(i).getId());
                System.out.println(sessionService.getStatus(sessions.get(i).getId()));
            }

            if (sessionService.getStatus(sessions.get(i).getId()).equals(SessionStatus.EXPIRED)) {
                System.out.println(sessions.get(i).getId());
                sessionService.completeSession(sessions.get(i).getId());
            }
        }
    }
}
