package com.example.demo.ServiceControllers;

import com.example.demo.DataModels.client.Client;
import com.example.demo.Repositories.ClientRepository;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;


@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity add(@RequestBody Client client) throws IOException, InterruptedException {

        if(client.getId() != null) {
            Optional<Client> existingClient = clientRepository.findById(client.getId());

            if(existingClient.isEmpty())
                return ResponseEntity.status(404).body("{ \"message\": \"the client with id: " + client.getId() + " doesn't exist; it can't be updated. If you are trying to create a new client, send an entity with id set to null\"}");
            existingClient.get().update(client);

            Client updatedClient;
            try {
                clientRepository.deleteById(client.getId());
                updatedClient = clientRepository.save(existingClient.get());
                return ResponseEntity.status(200).body(updatedClient);
            } catch (Exception e){
                return ResponseEntity.status(500).body(e);
            }
        }

        if(!checkEmailValidity(client.getEmail()))
            return ResponseEntity.status(400).body("{ \"message\": \"invalid email\"}");

        Client savedClient;
        try {
            savedClient = clientRepository.save(client);
            URL url = new URL("https://localhost:8080/register/sendmail?id=" + savedClient.getId());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream responseStream = connection.getInputStream();
            System.out.println(Arrays.toString(responseStream.readAllBytes()));
        } catch (DuplicateKeyException e){
            return ResponseEntity.status(400).body("{ \"message\": \"duplicate value\"}");
        } catch (Exception e){
            return ResponseEntity.status(500).body(e);
        }


        return ResponseEntity.status(200).body(savedClient);
    }

    @PostMapping(value = "/login")
    @ResponseBody
    public ResponseEntity loginByEmail(@RequestParam String email, @RequestParam String password) {
        Optional<Client> client = clientRepository.findByEmail(email);

        if(client.isEmpty())
            return ResponseEntity.status(404).body("{ \"message\": \"client not found\"}");

        if(client.get().getPassword().equals(password)){
            return ResponseEntity.status(200).body(client);
        }

        return ResponseEntity.status(400).body("{ \"message\": \"invalid credentials\"}");
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> getClientById(@PathVariable String id) {
        return new ResponseEntity<>(clientRepository.findById(id), HttpStatus.OK);
    }


    private Boolean checkEmailValidity(String email) throws IOException, InterruptedException {
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
}

