package edu.psu.sweng894.chewsy.session.domain.repository;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.springframework.stereotype.Repository;

@Repository
public interface ConciergeRepository {
    public JSONArray getRestaurants(String location, int radius) throws UnsupportedEncodingException, IOException;    
}