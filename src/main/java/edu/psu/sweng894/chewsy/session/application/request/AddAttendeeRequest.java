package edu.psu.sweng894.chewsy.session.application.request;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AddAttendeeRequest {
    @NotNull private String email;

    @JsonCreator
    public AddAttendeeRequest(@JsonProperty("email") final String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
