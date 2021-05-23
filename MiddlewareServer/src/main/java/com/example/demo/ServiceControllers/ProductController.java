package com.example.demo.ServiceControllers;

import com.example.demo.ControllerConfigurations;
import com.example.demo.DataModels.product.Product;
import com.example.demo.DataModels.client.Client;
import com.example.demo.HttpUtils.Auth0Utils;
import com.example.demo.Repositories.ClientRepository;
import com.example.demo.Repositories.ProductRepository;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Optional;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/product")
public class ProductController {
    ControllerConfigurations config = new ControllerConfigurations();

    private Auth0Utils auth0Utils = new Auth0Utils();

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ClientRepository clientRepository;
    
    @PostMapping("/add")
    @ResponseStatus(code = HttpStatus.CREATED)
        public ResponseEntity add(@RequestBody String data, @RequestHeader HttpHeaders headers) {
        System.out.println("Called: POST /product/add");

        if (!auth0Utils.checkIfAuthorized(headers)){
            return ResponseEntity.status(401).body("{ \"message\": \"Unauthorized\"}");
        }

        String clientToken = auth0Utils.getClientToken(headers);

        JsonObject productJson = (JsonObject) JsonParser.parseString(data);
        JsonObject userInfo = auth0Utils.getUserInfo(clientToken);
        productJson = productJson.getAsJsonObject("product");
        productJson.addProperty("clientId", userInfo.get("sub").getAsString());

        Product product = new Product();
        product.jsonToProduct(productJson);

        Optional<Client> existingClient;
        try {
            existingClient = clientRepository.findByAuthId(userInfo.get("sub").getAsString());
        } catch (NullPointerException e){
            return ResponseEntity.status(404).body("{ \"message\": \"invalid client token\"}");
        }

        if(existingClient.isEmpty())
            return ResponseEntity.status(404).body("{ \"message\": \"client not found\"}");

        Product savedProduct;

        try {
            savedProduct = productRepository.save(product);
            return ResponseEntity.status(200).body(savedProduct);
        } catch (Exception e){
            return ResponseEntity.status(500).body(e);
        }
    }

    @DeleteMapping
    @ResponseBody
    public ResponseEntity delete(@RequestParam String productid, @RequestHeader HttpHeaders headers) {
        System.out.println("Called: DELETE /product");

        if (!auth0Utils.checkIfAuthorized(headers)){
            return ResponseEntity.status(401).body("{ \"message\": \"Unauthorized\"}");
        }

        Optional<Product> productToDelete = productRepository.findById(productid);

        if(productToDelete.isPresent()) {
            try {
                productRepository.delete(productToDelete.get());
            } catch (Exception e) {
                return ResponseEntity.status(500).body(e);
            }
            return ResponseEntity.status(200).body("{ \"message\": \"product with id: " + productid + " successfully deleted\"}");
        }

        return ResponseEntity.status(404).body("{ \"message\": \"product with id: " + productid + " not found\"}");
    }

    @GetMapping
    public ResponseEntity<Object> getProducts() {
        System.out.println("Called: GET /product");

        return new ResponseEntity<>(productRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable String id) {
        System.out.println("Called: GET /product/{id}");

        return new ResponseEntity<>(productRepository.findById(id), HttpStatus.OK);
    }

    @GetMapping(value = "/client/", params = "productid")
    public ResponseEntity getClientByProductId(@PathParam("productid") String productid) {
        System.out.println("Called: GET /product/client/{id}");

        Optional<Product> product = productRepository.findById(productid);
        Optional<Client> client;
        Product gotProduct;

        if (product.isPresent()) {
            gotProduct = product.get();
            client = clientRepository.findByAuthId(gotProduct.getClientId());

            if (client.isPresent()) {
                return new ResponseEntity(client, HttpStatus.OK);
            } else
                return ResponseEntity.status(404).body("{ \"message\": \"client with id: " + gotProduct.getClientId() + " not found\"}");

        }
        return ResponseEntity.status(404).body("{ \"message\": \"product with id: " + productid + " not found\"}");
    }

    @GetMapping("/client")
    public ResponseEntity<Object> getProductsByClientToken(@RequestHeader HttpHeaders headers) {
        System.out.println("Called: GET /product/client");

        if (!auth0Utils.checkIfAuthorized(headers)){
            return ResponseEntity.status(401).body("{ \"message\": \"Unauthorized\"}");
        }

        String clientToken = auth0Utils.getClientToken(headers);

        JsonObject userInfo = auth0Utils.getUserInfo(clientToken);
        List<Product> products;

        try {
            products = productRepository.findByClientId(userInfo.get("sub").getAsString());
        } catch (Exception e){
            return ResponseEntity.status(500).body(e);
        }

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    //checks if product with id productId exists for client with clientToken
    @GetMapping(params = {"productId"})
    public ResponseEntity<Object> productExistByTokenAndId(@PathParam("productId") String productId, @RequestHeader HttpHeaders headers) {
        System.out.println("Called: GET product{productId}");

        if (!auth0Utils.checkIfAuthorized(headers)){
            return ResponseEntity.status(401).body("{ \"message\": \"Unauthorized\"}");
        }

        String clientToken = auth0Utils.getClientToken(headers);

        JsonObject userInfo;
        Optional<Client> client;
        try {
            userInfo = auth0Utils.getUserInfo(clientToken);
            client = clientRepository.findByAuthId(userInfo.get("sub").getAsString());
        } catch (NullPointerException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<Product> product = productRepository.findById(productId);

        if(client.isPresent() && product.isPresent()){
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(params = {"productId", "image"})
    public ResponseEntity<Object> addImageToProduct(@PathParam("productId") String productId, String image) {
        System.out.println("Called: POST product{productId}&{image}");

        Optional<Product> product = productRepository.findById(productId);

        if(product.isPresent()){
            Product gotProduct = product.get();
            gotProduct.setImage(image);
            productRepository.save(gotProduct);

            try {
                gotProduct = productRepository.save(gotProduct);
                return ResponseEntity.status(200).body(gotProduct);
            } catch (Exception e){
                return ResponseEntity.status(500).body(e);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}