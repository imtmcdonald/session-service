package edu.psu.sweng894.chewsy.session.application.response;

public class GetRestaurantListResponse {
    private final String restaurants;

    public GetRestaurantListResponse(final String restaurants) {
        this.restaurants = restaurants;
    }

    public String getRestaurantList() {
        return restaurants;
    }
}
