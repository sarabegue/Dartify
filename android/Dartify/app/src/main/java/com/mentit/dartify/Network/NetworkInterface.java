package com.mentit.dartify.Network;

import org.json.JSONObject;

import java.util.List;

public interface NetworkInterface {
    /**
     * Get string by username password
     *
     * @param endpoint rest url
     * @param username username
     * @param password password
     * @return String
     */
    String getString(String endpoint, String username, String password);


    /**
     * Get string by bear token
     *
     * @param endpoint rest url
     * @param token    bearer token
     * @return String
     */
    String getString(String endpoint, String token);

    /**
     * Get call
     *
     * @param query search query
     * @return String
     */
    NetworkResponse get(String query, List<String> credentials);

    /**
     * Put call
     *
     * @param query search query
     * @return String
     */
    NetworkResponse put(String query, JSONObject body, List<String> credentials);
}
