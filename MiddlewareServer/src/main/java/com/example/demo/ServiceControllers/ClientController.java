package com.example.demo.ServiceControllers;

import com.example.demo.DataModels.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


interface ClientRepository extends MongoRepository<Client, String> {

}

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity add(@RequestBody Client client) {
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

    @GetMapping("/{id}")
    public ResponseEntity<Object> getClientById(@PathVariable String id) {
        return new ResponseEntity<>(clientRepository.findById(id), HttpStatus.OK);
    }
}

