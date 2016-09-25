package org.cruk.automation.framework;

//**** Created by turner34 on 25/08/2016. The Api Utils gives the following methods: ****//
//**** 1) getToken: will get the OAuth token so the api can be used                  ****//
//**** 2) set_a_REST_GET_response: will connect to the api address and get a response               ****//

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
//import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

//import java.io.File;
//import java.io.IOException;
import java.lang.String;


public class ApiUtils {
    //Variables
    //Hold access token
    private String accessToken;
    //Holds the response of the
    private ClientResponse response;


    //to put the acess token in to
    public void setAccessToken(String token_URL) {

        try {
            //Get access token to access JSON API
            Client client = Client.create();

            WebResource webResource = client.resource(token_URL);
            ClientResponse responseAuth = webResource.accept("application/json").get(ClientResponse.class);
            String outputAuth = responseAuth.getEntity(String.class);
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(outputAuth);
            accessToken = (String) jsonObject.get("access_token");  //jsonObject
            System.out.println("Access token is: " + accessToken);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void set_a_REST_GET_response(String URL_prefix, String URL_params) {
            //String to hold the token and the access_token=
            String URL_token_with_prefix;
            //Add parameter identifier to token
            URL_token_with_prefix = "access_token=" + accessToken;
            Client client = Client.create();
            //Use input to create apiURL
            String URLforTest = URL_prefix + URL_params + URL_token_with_prefix;
            //System.out.println(URLforTest);
            //Put URL into client
            WebResource webResource = client.resource(URLforTest);
            //Store api response
            response = webResource.accept("application/json").get(ClientResponse.class);
    }

    public ClientResponse get_REST_GET_Response() {
        return response;
    }
}