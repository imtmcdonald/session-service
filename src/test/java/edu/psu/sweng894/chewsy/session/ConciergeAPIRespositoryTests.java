package edu.psu.sweng894.chewsy.session;

import org.json.JSONObject;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

import java.io.IOException;

import org.springframework.boot.test.context.SpringBootTest;

import edu.psu.sweng894.chewsy.session.domain.repository.ConciergeRepository;
import edu.psu.sweng894.chewsy.session.infrastructure.repository.ConciergeAPI.ConciergeAPIRespository;

@SpringBootTest
public class ConciergeAPIRespositoryTests {

    @Test
    public void testGetRestaurantsNotNull() {
        ConciergeRepository restaurants = new ConciergeAPIRespository();
        JSONArray restaurantList;
        try {
            restaurantList = restaurants.getRestaurants("23666", 5);
            assertNotNull(restaurantList);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
