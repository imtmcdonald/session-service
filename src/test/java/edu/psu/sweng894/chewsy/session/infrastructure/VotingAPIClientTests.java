package edu.psu.sweng894.chewsy.session.infrastructure;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;

import edu.psu.sweng894.chewsy.session.infrastructure.client.VotingAPI.VotingAPIClient;

@WireMockTest(httpPort = 8091)
public class VotingAPIClientTests {
    private VotingAPIClient classUnderTest;

    @BeforeEach
    public void setUp() {
        classUnderTest = new VotingAPIClient();
    }

    @Test
    public void shoudlGetConsensus_andNotBeNull() throws UnsupportedEncodingException, IOException {
        final long id = Long.parseLong("3");
        final int voters = 5;
        final ArrayList<String> consensus = new ArrayList();

        consensus.add("test");

        JSONObject expected = new JSONObject();
        expected.put("count", consensus);        

        stubFor(post("/vote/count").willReturn(okJson(expected.toString())));

        JSONObject actual = classUnderTest.getConsensus(id, voters);

        assertEquals(expected.toString(), actual.toString());

        assertNotNull(actual);
    }
}
