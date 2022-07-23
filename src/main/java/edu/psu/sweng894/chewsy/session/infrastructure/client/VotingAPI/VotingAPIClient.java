package edu.psu.sweng894.chewsy.session.infrastructure.client.VotingAPI;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import edu.psu.sweng894.chewsy.session.domain.client.VotingClient;
import org.springframework.stereotype.Repository;

@Repository
public class VotingAPIClient implements VotingClient {
    private String votingEndpoint =  StringUtils.defaultIfEmpty(System.getenv("VOTING_ENDPOINT"), "http://localhost:8091/vote/count");

    @Override
    public JSONObject getConsensus(Long id, int voters) throws UnsupportedEncodingException, IOException {
        // JSONArray consensus = new JSONArray();
        JSONObject consensus = new JSONObject();

        JSONObject jsonInput = new JSONObject();
        jsonInput.put("session", id);
        jsonInput.put("voters", voters);        

        String jsonInputString = jsonInput.toString();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(votingEndpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonInputString))
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode()==200) {
                System.out.println(response.body());
                consensus = new JSONObject(response.body());
            }

        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return consensus;
    }
    
}
