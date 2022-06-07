package edu.psu.sweng894.chewsy.session.application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import edu.psu.sweng894.chewsy.session.application.request.AddAttendeeRequest;
import edu.psu.sweng894.chewsy.session.application.request.AddRestaurantListRequest;
import edu.psu.sweng894.chewsy.session.application.request.CreateAttendeeRequest;
import edu.psu.sweng894.chewsy.session.application.request.CreateSessionRequest;
import edu.psu.sweng894.chewsy.session.application.request.RemoveAttendeeRequest;
import edu.psu.sweng894.chewsy.session.application.response.CreateSessionResponse;
import edu.psu.sweng894.chewsy.session.application.response.GetAttendeesResponse;
import edu.psu.sweng894.chewsy.session.application.response.GetRestaurantListResponse;
import edu.psu.sweng894.chewsy.session.domain.Attendee;
import edu.psu.sweng894.chewsy.session.domain.SessionStatus;
import edu.psu.sweng894.chewsy.session.domain.service.SessionService;

@RestController
@RequestMapping("/sessions")
public class SessionController {
    private final SessionService sessionService;
    
    @Autowired
    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping(value = "/new_user", consumes = MediaType.APPLICATION_JSON_VALUE)
    void createAttendee(@RequestBody final CreateAttendeeRequest createAttendeeRequest) {
        sessionService.createAttendee(createAttendeeRequest.getEmail()); 
    }

    @PostMapping(value = "/create_session", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    CreateSessionResponse createSession(@RequestBody final CreateSessionRequest createSessionRequest) {
        final Long id = sessionService.createSession(createSessionRequest.getAttendee());
        
        return new CreateSessionResponse(id);
    }

    @GetMapping(value = "/{id}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    String getSessionStatus(@PathVariable final Long id) {
        final SessionStatus sessionStatus = sessionService.getStatus(id);

        System.out.println(sessionStatus);

        return sessionStatus.toString();
    }

    @GetMapping(value = "/{id}/status/complete")
    void completeSession(@PathVariable final Long id) {
        sessionService.completeSession(id);
    }

    @GetMapping(value = "/{id}/attendees", produces = MediaType.APPLICATION_JSON_VALUE)
    GetAttendeesResponse getAttendee(@PathVariable final Long id) {
        final List<Attendee> attendees = sessionService.getAttendees(id);

        return new GetAttendeesResponse(attendees);
    }

    @PostMapping(value = "/{id}/attendees", consumes = MediaType.APPLICATION_JSON_VALUE)
    void addAttendee(@PathVariable final Long id, @RequestBody final AddAttendeeRequest addAttendeeRequest) {
        sessionService.addAttendee(id, addAttendeeRequest.getAttendee());
    }

    @DeleteMapping(value = "/{id}/attendees", consumes = MediaType.APPLICATION_JSON_VALUE)
    void removeAttendee(@PathVariable final Long id, @RequestBody final RemoveAttendeeRequest removeAttendeeRequest) {
        sessionService.removeAttendee(id, removeAttendeeRequest.getAttendee().getEmail());
    }

    @PostMapping(value = "/{id}/restaurants", consumes = MediaType.APPLICATION_JSON_VALUE)
    void addRestaurantList(@PathVariable final Long id, @RequestBody final AddRestaurantListRequest addRestaurantListRequest) {
        sessionService.addRestaurantList(id, addRestaurantListRequest.getLocation(), addRestaurantListRequest.getRadius());
    }

    @GetMapping(value = "/{id}/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
    public GetRestaurantListResponse getRestaurant(@PathVariable final Long id) {
        final String restaurants = sessionService.getRestaurantList(id);
        System.out.println(restaurants);
        GetRestaurantListResponse response = new GetRestaurantListResponse(restaurants);

        System.out.println(response);
        return response;
    }
}
