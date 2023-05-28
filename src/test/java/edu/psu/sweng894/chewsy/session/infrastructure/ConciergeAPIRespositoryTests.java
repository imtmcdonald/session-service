package edu.psu.sweng894.chewsy.session.infrastructure;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;

import edu.psu.sweng894.chewsy.session.infrastructure.repository.ConciergeAPI.ConciergeAPIRespository;

@WireMockTest(httpPort = 8080)
public class ConciergeAPIRespositoryTests {
    private ConciergeAPIRespository classUnderTest;

    @BeforeEach
    public void setUp() {
        classUnderTest = new ConciergeAPIRespository();
    }

    @Test
    public void shoudlGetRestaurantList_andNotBeNull() throws UnsupportedEncodingException, IOException {
        final String location = "23666";
        final int radius = 5;

        JSONObject jo = new JSONObject();
        jo.put("NAME", "Burger King");
        jo.put("RATING", "3.4");
        jo.put("LOCATION", "112 Jefferson Ave, Newport News VA 23601");
        JSONArray expected = new JSONArray();
        expected.put(jo);

        stubFor(post("/find").willReturn(okJson(expected.toString())));

        JSONArray actual = classUnderTest.getRestaurants(location, radius);

        assertEquals(expected.toString(), actual.toString());

        assertNotNull(actual);
    }
}
