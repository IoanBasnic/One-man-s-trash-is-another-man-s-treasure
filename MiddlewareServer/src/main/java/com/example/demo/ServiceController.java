package com.example.demo;

import com.example.demo.DataModels.Client;
import com.example.demo.DataModels.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ServiceController {
    private static Map<String, Product> productRepo = new HashMap<>();
    private static Map<String, Client> clientRepo = new HashMap<>();

//    static {
//        Product honey = new Product();
//        honey.setId(String.fromString("00000000-0000-0000-0000-000000000000"));
//        honey.setClientId(String.fromString("00000000-0000-0000-0000-000000000000"));
//        honey.setTitle("Honey");
//        productRepo.put(honey.getId(), honey);
//
//        Product almond = new Product();
//        almond.setId(String.randomString());
//        almond.setTitle("Almond");
//        productRepo.put(almond.getId(), almond);
//
//        Client client = new Client();
//
//        client.setId(String.randomString());
//        client.setUsername("Username");
//        clientRepo.put(client.getId(), client);
//
//    }

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
        return new ResponseEntity<>(productRepo.values().stream().filter(product -> product.getClientId().equals(clientId)), HttpStatus.OK);
    }

    //GET products by different filters
}

interface ProductRepository extends MongoRepository<Product, String> {}

@RestController
@RequestMapping("/product")
class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Product add(@RequestBody Product product) {
        return productRepository.save(product);
    }
}

interface ClientRepository extends MongoRepository<Client, String> {}

@RestController
@RequestMapping("/client")
class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Client add(@RequestBody Client client) {
        return clientRepository.save(client);
    }
}