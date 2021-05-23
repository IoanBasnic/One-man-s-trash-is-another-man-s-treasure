package com.example.demo.ServiceControllers;

import com.example.demo.ControllerConfigurations;
import com.example.demo.DataModels.client.Client;
import com.example.demo.DataModels.client.Coordinates;
import com.example.demo.DataModels.product.Product;
import com.example.demo.Repositories.ClientRepository;
import com.example.demo.Repositories.ProductRepository;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.demo.HttpUtils.Auth0Utils;
import org.springframework.web.client.RestTemplate;

import javax.websocket.server.PathParam;


@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/client")
public class ClientController {
    ControllerConfigurations config = new ControllerConfigurations();
    private Auth0Utils auth0Utils = new Auth0Utils();

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ProductRepository productRepository;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity add(@RequestHeader HttpHeaders headers) {
        System.out.println("Called: POST /client");

        if (!auth0Utils.checkIfAuthorized(headers)){
            return ResponseEntity.status(401).body("{ \"message\": \"Unauthorized\"}");
        }

        String clientToken = auth0Utils.getClientToken(headers);

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

                if (savedClient.getEmailVerified() || checkEmailValidity(savedClient.getEmail(), userInfo.get("email_verified").getAsBoolean())) {
                    savedClient.setEmailVerified(true);
                    clientRepository.save(savedClient);

                    final String uri = config.serviceServerAPI + "/register/sendmail/" + savedClient.getId();

                    RestTemplate restTemplate = new RestTemplate();
                    restTemplate.getForObject(uri, String.class);
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

        return ResponseEntity.status(200).contentType(MediaType.TEXT_PLAIN).body("valid token acquired");
    }

    @PutMapping
    public ResponseEntity addAdditionalValues(@RequestBody String data, @RequestHeader HttpHeaders headers) throws IOException, InterruptedException {
        System.out.println("Called: PUT /client");

        //data == clientToken + additionalInfo
        JsonObject dataJson = (JsonObject) JsonParser.parseString(data);
        JsonObject additionalInfo;
        Optional<Client> existingClient;

        if (!auth0Utils.checkIfAuthorized(headers)){
            return ResponseEntity.status(401).body("{ \"message\": \"Unauthorized\"}");
        }

        String clientToken = auth0Utils.getClientToken(headers);

        try {
            additionalInfo = dataJson.get("additionalInfo").getAsJsonObject();

            JsonObject userInfo = auth0Utils.getUserInfo(clientToken);
            existingClient = clientRepository.findByAuthId(userInfo.get("sub").getAsString());
        } catch (Exception e){
            return ResponseEntity.status(401).body("{ \"message\": \"bad json data\"}");
        }

        if(existingClient.isEmpty())
            return ResponseEntity.status(404).body("{ \"message\": \"client not found\"}");

        Client client = existingClient.get();
        updateClientAdditionalValues(client, additionalInfo);

        try {
            clientRepository.save(client);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e);
        }

        return ResponseEntity.status(200).body("{ \"message\": \"valid token acquired\"}");
    }

    @DeleteMapping
    @ResponseBody
    public ResponseEntity delete(@RequestHeader HttpHeaders headers) {
        System.out.println("Called: DELETE /client");
        Optional<Client> existingClient;
        String clientId;

        if (!auth0Utils.checkIfAuthorized(headers)){
            return ResponseEntity.status(401).body("{ \"message\": \"Unauthorized\"}");
        }
        String clientToken = auth0Utils.getClientToken(headers);

        try {
            JsonObject userInfo = auth0Utils.getUserInfo(clientToken);
            clientId = userInfo.get("sub").getAsString();
            existingClient = clientRepository.findByAuthId(clientId);
        } catch (Exception e){
            return ResponseEntity.status(401).body("{ \"message\": \"bad json data\"}");
        }

        if(existingClient.isPresent()){
            List<Product> clientProducts = productRepository.findByClientId(clientId);

            for (Product product : clientProducts) {
                try {
                    productRepository.delete(product);
                } catch (Exception e) {
                    return ResponseEntity.status(500).contentType(MediaType.TEXT_PLAIN).body("client with id " + clientId + " couldn't be deleted due to error " + e + " caused by failed product deletion with id " + product.getId());
                }
            }

            try {
                clientRepository.delete(existingClient.get());
            } catch (Exception e){
                return ResponseEntity.status(500).contentType(MediaType.TEXT_PLAIN).body( "client with id " + clientId + " couldn't be deleted ERROR: " + e);
            }
            return ResponseEntity.status(200).body("{ \"message\": \"client with id: " + clientId + " successfully deleted\"}");
        }

        return ResponseEntity.status(404).body("{ \"message\": \"client with id: " + clientId + " not found\"}");
    }



    @GetMapping
    public ResponseEntity<Object> getClientByToken(@RequestHeader HttpHeaders headers) {
        System.out.println("Called: GET /client");

        if (!auth0Utils.checkIfAuthorized(headers)){
            return ResponseEntity.status(401).body("{ \"message\": \"Unauthorized\"}");
        }
        String clientToken = auth0Utils.getClientToken(headers);

        JsonObject userInfo = auth0Utils.getUserInfo(clientToken);
        Optional<Client> client = clientRepository.findByAuthId(userInfo.get("sub").getAsString());

        if (client.isPresent())
            return new ResponseEntity<>(client, HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    private Boolean checkEmailValidity(String email, Boolean emailVerified) throws IOException, InterruptedException {
        if(emailVerified)
            return true;

        /* This is the email validator. The reason it is commented out is because I have subscribed to
         * an API that only lets us use the endpoint 1000 times a month before paying.
         * PLEASE DO NOT comment it out unless you want to demo or test it specifically.
         */

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://mailcheck.p.rapidapi.com/?domain=" + email))
                .header("x-rapidapi-key", "b67e8ce408mshe31dda6d0f93030p16afe7jsnca0b526c2966")
                .header("x-rapidapi-host", "mailcheck.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        org.springframework.boot.json.JsonParser parser = JsonParserFactory.getJsonParser();
        Map<String, Object> jsonMap = parser.parseMap(response.body());

        return jsonMap.get("valid").toString().equals("true");
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
        if(additionalInfo.has("phoneNumber")){
            client.setPhoneNumber(additionalInfo.get("phoneNumber").getAsString());
        }
        if(additionalInfo.has("address")){
            client.setAddress(additionalInfo.get("address").getAsString());
        }
        if(additionalInfo.has("paymentMethod")){
            client.mapToPaymentMethod(additionalInfo.get("paymentMethod").getAsString());
        }

        if(additionalInfo.has("coordinates")){
            JsonObject coordinates = additionalInfo.get("coordinates").getAsJsonObject();
            if(coordinates.has("lat") && coordinates.has("lng"))
                client.setCoordinates(new Coordinates(coordinates.get("lat").getAsDouble(), coordinates.get("lng").getAsDouble()));
        }
    }
}

