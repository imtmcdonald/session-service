package edu.psu.sweng894.chewsy.session.infrastructure.repository.ConciergeAPI;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.psu.sweng894.chewsy.session.domain.repository.ConciergeRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ConciergeAPIRespository implements ConciergeRepository {
    private String conciergeEndpoint = System.getenv("CONCIERGE_ENDPOINT");
    
    public JSONArray getRestaurants(String location, int radius) {
        JSONArray restaurantList = new JSONArray();

        JSONObject jsonInput = new JSONObject();
        jsonInput.put("location", location);
        jsonInput.put("radius", radius);        

        String jsonInputString = jsonInput.toString();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(conciergeEndpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonInputString))
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode()==200) {
                restaurantList = new JSONArray(response.body().toString());
            }

        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return restaurantList;
    }   
}
