package com.example.demo.ServiceControllers;

import com.example.demo.DataModels.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

interface ClientRepository extends MongoRepository<Client, String> {}

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Client add(@RequestBody Client client) {
        return clientRepository.save(client);
    }
}
