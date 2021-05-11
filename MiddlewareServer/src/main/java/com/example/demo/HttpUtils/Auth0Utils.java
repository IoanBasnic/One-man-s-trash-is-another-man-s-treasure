package com.example.demo.HttpUtils;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

        //json.addProperty("done", true);
        return json;
    }
}
