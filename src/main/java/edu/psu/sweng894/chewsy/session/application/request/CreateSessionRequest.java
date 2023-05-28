package edu.psu.sweng894.chewsy.session.application.request;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateSessionRequest {
    @NotNull String location;
    @NotNull int radius;
    @NotNull int duration;

    @JsonCreator
    public CreateSessionRequest(@JsonProperty("location") @NotNull final String location, @JsonProperty("radius") @NotNull final int radius, @JsonProperty("duration") @NotNull final int duration) {
        this.duration = duration;
        this.location = location;
        this.radius = radius;
    }

    public String getLocation() {
        return location;
    }

    public int getRadius() {
        return radius;
    }

    public int getDuration() {
        return duration;
    }    
    
}
