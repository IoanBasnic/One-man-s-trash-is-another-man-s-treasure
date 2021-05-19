package com.example.demo.ServiceControllers;

import com.example.demo.DataModels.client.Client;
import com.example.demo.Repositories.ClientRepository;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.Optional;

import com.example.demo.HttpUtils.Auth0Utils;
import org.springframework.web.client.RestTemplate;


@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/client")
public class ClientController {
    private Auth0Utils auth0Utils = new Auth0Utils();

    @Autowired
    private ClientRepository clientRepository;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity add(@RequestBody String clientToken) throws IOException, InterruptedException {
        JsonObject userInfo = auth0Utils.getUserInfo(clientToken);
        if(userInfo.has("error"))
            return ResponseEntity.status(500).body(userInfo.get("error"));

        Optional<Client> existingClient = clientRepository.findByAuthId(userInfo.get("sub").getAsString());

        if(existingClient.isEmpty()) {
            Client savedClient = new Client();

            try {
                System.out.println(savedClient.toString());
                createClient(savedClient, userInfo);
                System.out.println(savedClient.toString());
                clientRepository.save(savedClient);

                System.out.println("got saved");
                if (checkEmailValidity(savedClient.getEmail(), userInfo.get("email_verified").getAsBoolean())) {
                    final String uri = "http://localhost:6868/register/sendmail/" + savedClient.getId();

                    RestTemplate restTemplate = new RestTemplate();
                    String result = restTemplate.getForObject(uri, String.class);
                }
            } catch (DuplicateKeyException e) {
                return ResponseEntity.status(400).body("{ \"message\": \"duplicate value\"}");
            } catch (Exception e) {
                return ResponseEntity.status(500).body(e);
            }
        }
        else{
            Client existingClientGet = existingClient.get();
            updateClient(existingClientGet, userInfo);
            clientRepository.save(existingClientGet);
        }

        return ResponseEntity.status(200).body("valid token acquired");
    }

    @PutMapping
    public ResponseEntity addAdditionalValues(@RequestBody String data) throws IOException, InterruptedException {
        //data == clientToken + additionalInfo
        JsonObject dataJson = (JsonObject) JsonParser.parseString(data);
        String clientToken = dataJson.get("clientToken").getAsString();
        JsonObject additionalInfo = dataJson.get("additionalInfo").getAsJsonObject();

        JsonObject userInfo = auth0Utils.getUserInfo(clientToken);
        Optional<Client> existingClient = clientRepository.findByAuthId(userInfo.get("sub").getAsString());


        if(existingClient.isEmpty())
            return ResponseEntity.status(404).body("{ \"message\": \"client not found\"}");

        Client client = existingClient.get();
        updateClientAdditionalValues(client, additionalInfo);

        try {
            clientRepository.save(client);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e);
        }

        return ResponseEntity.status(200).body("valid token acquired");
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> getClientById(@PathVariable String id) {
        return new ResponseEntity<>(clientRepository.findById(id), HttpStatus.OK);
    }


    private Boolean checkEmailValidity(String email, Boolean emailVerified) throws IOException, InterruptedException {
        if(emailVerified)
            return true;

//            URL url = new URL("https://localhost:8080/register/sendmail?id=" + savedClient.getId());
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            InputStream responseStream = connection.getInputStream();
//            System.out.println(Arrays.toString(responseStream.readAllBytes()));

        /* This is the email validator. The reason it is commented out is because I have subscribed to
         * an API that only lets us use the endpoint 1000 times a month before paying.
         * PLEASE DO NOT comment it out unless you want to demo or test it specifically.
         */

//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create("https://mailcheck.p.rapidapi.com/?domain=" + email))
//                .header("x-rapidapi-key", "b67e8ce408mshe31dda6d0f93030p16afe7jsnca0b526c2966")
//                .header("x-rapidapi-host", "mailcheck.p.rapidapi.com")
//                .method("GET", HttpRequest.BodyPublishers.noBody())
//                .build();
//        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
//
//        org.springframework.boot.json.JsonParser parser = JsonParserFactory.getJsonParser();
//        Map<String, Object> jsonMap = parser.parseMap(response.body());
//
//        return jsonMap.get("valid").toString().equals("true");

        return true;
    }

    private void createClient(Client savedClient, JsonObject userInfo){
        savedClient.setEmail(userInfo.get("email").getAsString());
        savedClient.setAuthId(userInfo.get("sub").getAsString());

        if(userInfo.has("given_name"))
            savedClient.setGivenName(userInfo.get("given_name").getAsString());

        if(userInfo.has("family_name"))
            savedClient.setFamilyName(userInfo.get("family_name").getAsString());

        if(userInfo.has("nickname"))
            savedClient.setNickname(userInfo.get("nickname").getAsString());
    }

    private void updateClient(Client existingClient, JsonObject userInfo){
        if(userInfo.has("given_name")){
            existingClient.setGivenName(userInfo.get("given_name").getAsString());
        }
        if(userInfo.has("family_name")){
            existingClient.setFamilyName(userInfo.get("family_name").getAsString());
        }
        if(userInfo.has("nickname")){
            existingClient.setNickname(userInfo.get("nickname").getAsString());
        }
    }

    private void updateClientAdditionalValues(Client client, JsonObject additionalInfo){
        if(additionalInfo.has("phone_number")){
            client.setPhoneNumber(additionalInfo.get("phone_number").getAsString());
        }
        if(additionalInfo.has("address")){
            client.setAddress(additionalInfo.get("address").getAsString());
        }
        if(additionalInfo.has("payment_method")){
            client.mapToPaymentMethod(additionalInfo.get("payment_method").getAsString());
        }
    }
}

