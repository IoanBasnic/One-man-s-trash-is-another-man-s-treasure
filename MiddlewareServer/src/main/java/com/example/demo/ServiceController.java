package com.example.demo;

import com.example.demo.DataModels.Client;
import com.example.demo.DataModels.Product;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ServiceController {
    private static Map<UUID, Product> productRepo = new HashMap<>();
    private static Map<UUID, Client> clientRepo = new HashMap<>();

    static {
        Product honey = new Product();
        honey.setId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        honey.setClientId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        honey.setTitle("Honey");
        productRepo.put(honey.getId(), honey);

        Product almond = new Product();
        almond.setId(UUID.randomUUID());
        almond.setTitle("Almond");
        productRepo.put(almond.getId(), almond);

        Client client = new Client();

        client.setId(UUID.randomUUID());
        client.setUsername("Username");
        clientRepo.put(client.getId(), client);

    }

    //GET all products
    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public ResponseEntity<Object> getProduct() {
        return new ResponseEntity<>(productRepo.values(), HttpStatus.OK);
    }

    //POST client (register)
    //GET client data by logging in
    @RequestMapping(value = "/client", method = RequestMethod.GET)
    public ResponseEntity<Object> getClient() {
        return new ResponseEntity<>(clientRepo.values(), HttpStatus.OK);
    }

    //GET all products of client with id
    @RequestMapping(value = "/products/{clientId}", method = RequestMethod.GET)
    public ResponseEntity<Object> getProductByClientId(@PathVariable("clientId") String clientId) {
        return new ResponseEntity<>(productRepo.values().stream().filter(product -> product.getClientId().equals(UUID.fromString(clientId))), HttpStatus.OK);
    }

    //GET products by different filters
}
