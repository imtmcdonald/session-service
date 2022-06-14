package edu.psu.sweng894.chewsy.session.infrastructure;

import org.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.springframework.boot.test.context.SpringBootTest;

import edu.psu.sweng894.chewsy.session.infrastructure.repository.ConciergeAPI.ConciergeAPIRespository;

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

        final JSONArray actual = classUnderTest.getRestaurants(location, radius);

        assertNotNull(actual);
    }
}
