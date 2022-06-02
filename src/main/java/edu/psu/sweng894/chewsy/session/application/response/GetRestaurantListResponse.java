package edu.psu.sweng894.chewsy.session.application.response;

import org.json.JSONArray;

public class GetRestaurantListResponse {
    private final String restaurants;

    public GetRestaurantListResponse(final String restaurants) {
        this.restaurants = restaurants;
    }

    public String getRestaurantList() {
        return restaurants;
    }
}
