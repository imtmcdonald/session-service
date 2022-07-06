package edu.psu.sweng894.chewsy.session.application.request;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateAttendeeRequest {
    @NotNull private String email;
    @NotNull private String name;

    @JsonCreator
    public CreateAttendeeRequest(@JsonProperty("email") @NotNull final String email, @JsonProperty("name") final String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
