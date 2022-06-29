package edu.psu.sweng894.chewsy.session.application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import edu.psu.sweng894.chewsy.session.application.request.AddAttendeeRequest;
import edu.psu.sweng894.chewsy.session.application.request.AddRestaurantListRequest;
import edu.psu.sweng894.chewsy.session.application.request.CreateAttendeeRequest;
import edu.psu.sweng894.chewsy.session.application.request.RemoveAttendeeRequest;
import edu.psu.sweng894.chewsy.session.application.request.SetDurationRequest;
import edu.psu.sweng894.chewsy.session.application.response.CreateSessionResponse;
import edu.psu.sweng894.chewsy.session.application.response.GetAttendeesResponse;
import edu.psu.sweng894.chewsy.session.application.response.GetRestaurantListResponse;
import edu.psu.sweng894.chewsy.session.domain.Attendee;
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
    @PostMapping(value = "/new_user", consumes = MediaType.APPLICATION_JSON_VALUE)
    void createAttendee(@RequestBody final CreateAttendeeRequest createAttendeeRequest) {
        sessionService.createAttendee(createAttendeeRequest.getEmail()); 
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/create_session", produces = MediaType.APPLICATION_JSON_VALUE)
    CreateSessionResponse createSession() {
        final Long id = sessionService.createSession();
        
        return new CreateSessionResponse(id);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/{id}/set_duration", consumes = MediaType.APPLICATION_JSON_VALUE)
    void setDuration(@PathVariable final Long id, @RequestBody final SetDurationRequest setDurationRequest) {
        sessionService.setDuration(id, setDurationRequest.getDuration());
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
        sessionService.addAttendee(id, addAttendeeRequest.getEmail());
        messageService.sendMessage(messageService.createMessage(addAttendeeRequest.getEmail(), Long.toString(id)));
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping(value = "/{id}/attendees", consumes = MediaType.APPLICATION_JSON_VALUE)
    void removeAttendee(@PathVariable final Long id, @RequestBody final RemoveAttendeeRequest removeAttendeeRequest) {
        sessionService.removeAttendee(id, removeAttendeeRequest.getEmail());
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/{id}/restaurants", consumes = MediaType.APPLICATION_JSON_VALUE)
    void addRestaurantList(@PathVariable final Long id, @RequestBody final AddRestaurantListRequest addRestaurantListRequest) {
        sessionService.addRestaurantList(id, addRestaurantListRequest.getLocation(), addRestaurantListRequest.getRadius());
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
}
