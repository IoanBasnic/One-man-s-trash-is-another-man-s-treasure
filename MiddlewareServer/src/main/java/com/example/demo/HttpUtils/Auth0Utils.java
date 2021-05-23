package com.example.demo.HttpUtils;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.http.HttpHeaders;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class Auth0Utils {

    public JsonObject getUserInfo(String clientAccessToken){
        HttpURLConnection connection = null;
        StringBuffer response = new StringBuffer();
        JsonObject json;

        try {
            URL authClientTokenUrl = new URL("https://blue-bird.eu.auth0.com/userinfo");
            connection = (HttpURLConnection) authClientTokenUrl.openConnection();
            connection.setRequestMethod("GET");

            connection.setRequestProperty("Authorization", "Bearer " + clientAccessToken);

            BufferedReader str = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String output;

            while ((output = str.readLine()) != null) {
                response.append(output);
            }

            str.close();

            json = (JsonObject) JsonParser.parseString(response.toString());
        } catch (Exception e){
            JsonObject errorJson = new JsonObject();
            errorJson.addProperty("error", e.toString());
            return errorJson;
        }

        if (json == null || json.isJsonNull()){
            JsonObject errorJson = new JsonObject();
            errorJson.addProperty("error", "error");
            return errorJson;
        }

        return json;
    }

    public boolean checkIfAuthorized(HttpHeaders headers){
        if(headers.containsKey("Authorization")){
            System.out.println("Authorization header exists");

            List<String> clientTokenList = headers.get("Authorization");
            assert clientTokenList != null;
            String clientToken = clientTokenList.get(0);
            System.out.println("client token with Bearer before: " + clientToken);

            clientToken = clientToken.replaceFirst("Bearer ", "");
            if(this.getUserInfo(clientToken).has("error")) {
                System.out.println("client token not good error: " + this.getUserInfo(clientToken).get("error"));
                return false;
            }

            System.out.println("client token: " + clientToken);
            return true;
        }
        return false;
    }

    public String getClientToken(HttpHeaders headers){
        List<String> clientTokenList = headers.get("Authorization");
        assert clientTokenList != null;
        return clientTokenList.get(0).replaceFirst("Bearer ", "");
    }


//    public boolean deleteUserByAuth0Id(String auth0Id){
//        HttpURLConnection connection = null;
//        StringBuffer response = new StringBuffer();
//        JsonObject json;
//
//        try {
//            URL authClientTokenUrl = new URL("https://blue-bird.eu.auth0.com/api/v2/clients/");
//            connection = (HttpURLConnection) authClientTokenUrl.openConnection();
//            connection.setRequestMethod("GET");
//
//            connection.setRequestProperty("Authorization", "Bearer " + clientAccessToken);
//
//            BufferedReader str = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            String output;
//
//            while ((output = str.readLine()) != null) {
//                response.append(output);
//            }
//
//            str.close();
//
//            json = (JsonObject) JsonParser.parseString(response.toString());
//        } catch (Exception e){
//            JsonObject errorJson = new JsonObject();
//            errorJson.addProperty("error", e.toString());
//            return errorJson;
//        }
//    }
}
