package edu.psu.sweng894.chewsy.session.domain.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.json.JSONObject;
import org.springframework.stereotype.Repository;

@Repository
public interface VotingClient {
    public JSONObject getConsensus(Long id, int voters) throws UnsupportedEncodingException, IOException;    
}
