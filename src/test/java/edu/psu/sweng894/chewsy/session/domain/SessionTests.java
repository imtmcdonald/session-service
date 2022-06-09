package edu.psu.sweng894.chewsy.session.domain;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.json.JSONArray;

@SpringBootTest
public class SessionTests {
    private Session classUnderTest;
    String email = "test@email.com";
    String testEmail = "jon.doe@email.com";
    Long id = Long.parseLong("1234");
    Session testSession = new Session();
    List<Attendee> expected = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        classUnderTest = new Session();
    }

    @Test
    public void shouldSetSessionId_thenReturnIt() {
        final Long id = Long.parseLong("34");

        classUnderTest.setId(id);

        final Long actual = classUnderTest.getId();

        assertEquals(id, actual);
    }

    @Test
    public void shouldGetStatus_thenBeEqualToCreated() {
        final SessionStatus actual = classUnderTest.getStatus();
        final SessionStatus expected = SessionStatus.CREATED;

        assertEquals(expected, actual);
    }

    @Test
    public void shouldAddAttendee_thenVerifyIsInList() {
        final Attendee attendee = new Attendee(email);
        
        classUnderTest.addAttendee(attendee);  
        
        final List<Attendee> actual = classUnderTest.getAttendeeList();

        assertEquals(email, actual.get(0).getEmail());
    }

    @Test
    public void shouldRemoveAttendee_thenVerifyIsNotInList() {
        final Attendee attendee = new Attendee(email);

        classUnderTest.addAttendee(attendee);
        classUnderTest.removeAttendee(attendee);

        final List<Attendee> actual = classUnderTest.getAttendeeList();

        assert(actual.isEmpty());
    }

    @Test
    public void shouldSetStatusComplete_thenVerifyIt() {
        classUnderTest.setStatusComplete();

        final SessionStatus actual = classUnderTest.getStatus();
        final SessionStatus expected = SessionStatus.COMPLETED;

        assertEquals(expected, actual);
    }

    @Test
    public void shouldAddRestaurantList_thenVerifyIt() {
        JSONObject jo = new JSONObject();

        jo.put("NAME", "Burger King");
        jo.put("RATING", "3.4");
        jo.put("LOCATION", "112 Jefferson Ave, Newport News VA 23601");

        JSONArray restaurantList = new JSONArray();

        restaurantList.put(jo);
        classUnderTest.addRestaurantList(restaurantList.toString());
        
        final String actual = classUnderTest.getRestaurantList();

        assertNotNull(actual);
    }

    @Test
    public void shouldSetStatusComplete_thenThrowErrorWithStatusComplete() {
        classUnderTest.setStatusComplete();

        final DomainException thrown = assertThrows(DomainException.class, () -> 
            classUnderTest.setStatusComplete());

        final String expected = "The session is in completed state.";
        
        assertEquals(expected, thrown.getMessage());
    }

    @Test
    public void shouldGetSessionToString_thenReturnString() {
        final String actual = classUnderTest.toString();

        assertNotNull(actual);
    }
}
