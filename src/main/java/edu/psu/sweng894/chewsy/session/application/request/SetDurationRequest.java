package edu.psu.sweng894.chewsy.session.application.request;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SetDurationRequest {
    @NotNull int duration;

    @JsonCreator
    public SetDurationRequest(@JsonProperty("duration") @NotNull final int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }    
    
}
