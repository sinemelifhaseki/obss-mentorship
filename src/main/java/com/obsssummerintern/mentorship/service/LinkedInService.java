package com.obsssummerintern.mentorship.service;


import com.unboundid.util.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

@Service
public class LinkedInService {

    private AllowedUserService allowedUserService;
    private UserService userService;

    public LinkedInService(UserService userService, AllowedUserService allowedUserService) {
        this.userService = userService;
        this.allowedUserService = allowedUserService;
    }

    private static final String EMAIL_ENDPOINT = "https://api.linkedin.com/v2/emailAddress?q=members&projection=(elements*(handle~))";

    private static final String CLIENT_ID = "77668qj90qju5b";
    private static final String CLIENT_SECRET = "xJm98zvHL3HAhROC";
    private static final String REDIRECT_URI = "http://localhost:8080";

    private String getAccessToken(String accessTokenUri) throws JSONException, org.json.JSONException {
        RestTemplate restTemplate = new RestTemplate();

        String accessTokenRequest = restTemplate.getForObject(accessTokenUri, String.class);
        JSONObject jsonObjOfAccessToken = new JSONObject(accessTokenRequest);

        return jsonObjOfAccessToken.get("access_token").toString();

    }


    public String exchangeToken(String authorizationCode) throws Exception {

        String accessTokenUri = "https://www.linkedin.com/oauth/v2/accessToken?grant_type=authorization_code&code="
                + authorizationCode + "&redirect_uri=" + REDIRECT_URI + "&client_id="
                + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "";

        String accessToken = getAccessToken(accessTokenUri);

        String email = getEmail(accessToken);


        return userService.findByUid(allowedUserService.findByEmail(email).getUid()).getId();
    }

    private String getEmail(String accessToken) throws Exception {
        String response = sendGetWithAuthorizationHeader(EMAIL_ENDPOINT, accessToken);

        JSONObject responseAsJSON = new JSONObject(response);

        return parseEmailApiResponse(responseAsJSON);
    }

    private String parseEmailApiResponse(JSONObject responseAsJSON) throws JSONException, org.json.JSONException {

        return responseAsJSON.getJSONArray("elements")
                .getJSONObject(0)
                .getJSONObject("handle~")
                .getString("emailAddress");
    }

    private String sendGetWithAuthorizationHeader(String resourceUrl, String accessToken) throws IOException {
        HttpsURLConnection con = openAuthorizedConnectionTo(resourceUrl, accessToken);

        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder jsonString = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            jsonString.append(line);
        }

        br.close();

        return jsonString.toString();
    }
    private HttpsURLConnection openAuthorizedConnectionTo(String resourceUrl, String accessToken) throws IOException {
        URL url = new URL(resourceUrl);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer " + accessToken);
        con.setRequestProperty("cache-control", "no-cache");
        con.setRequestProperty("X-Restli-Protocol-Version", "2.0.0");

        return con;
    }
}
