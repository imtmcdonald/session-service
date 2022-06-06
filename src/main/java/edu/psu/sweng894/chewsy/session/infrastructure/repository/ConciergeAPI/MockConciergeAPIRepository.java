package edu.psu.sweng894.chewsy.session.infrastructure.repository.ConciergeAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.psu.sweng894.chewsy.session.domain.repository.ConciergeRepository;

public class MockConciergeAPIRepository implements ConciergeRepository {
    public JSONArray getRestaurants(String location, int radius) {
        JSONObject jo = new JSONObject();
        jo.put("NAME", "Burger King");
        jo.put("RATING", "3.4");
        jo.put("LOCATION", "112 Jefferson Ave, Newport News VA 23601");

        JSONArray restaurantList = new JSONArray();
        restaurantList.put(jo);

        return restaurantList;
    }
    
}
