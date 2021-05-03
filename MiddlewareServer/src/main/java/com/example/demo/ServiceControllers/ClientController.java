package com.example.demo.ServiceControllers;

import com.example.demo.DataModels.client.Client;
import com.example.demo.Repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity add(@RequestBody Client client) {
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

        Client savedClient;

        try {
            savedClient = clientRepository.save(client);
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
}

