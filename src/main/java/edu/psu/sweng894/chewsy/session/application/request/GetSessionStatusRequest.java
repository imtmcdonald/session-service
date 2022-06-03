package edu.psu.sweng894.chewsy.session.application.request;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GetSessionStatusRequest {
    @NotNull private UUID id;

    @JsonCreator
    public GetSessionStatusRequest(@JsonProperty("id") @NotNull final UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }   
}
