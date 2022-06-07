package edu.psu.sweng894.chewsy.session.application.request;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AddRestaurantListRequest {
    @NotNull String location;
    @NotNull int radius;

    @JsonCreator
    public AddRestaurantListRequest(@JsonProperty("location") @NotNull final String location, @JsonProperty("radius") @NotNull final int radius) {
        this.location = location;
        this.radius = radius;
    }

    public String getLocation() {
        return location;
    }

    public int getRadius() {
        return radius;
    }
}
