package com.example.demo.ServiceControllers;

import com.example.demo.DataModels.product.Product;
import com.example.demo.DataModels.client.Client;
import com.example.demo.HttpUtils.Auth0Utils;
import com.example.demo.Repositories.ClientRepository;
import com.example.demo.Repositories.ProductRepository;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Null;
import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Optional;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/product")
public class ProductController {

    private Auth0Utils auth0Utils = new Auth0Utils();

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ClientRepository clientRepository;
    
    @PostMapping("/add")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity add(@RequestBody String data) {
        JsonObject dataJson = (JsonObject) JsonParser.parseString(data);
        String clientToken = dataJson.get("clientToken").getAsString();
        JsonObject productJson = dataJson.get("product").getAsJsonObject();

        JsonObject userInfo = auth0Utils.getUserInfo(clientToken);
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
    public ResponseEntity delete(@RequestParam String productid) {
        Optional<Product> productToDelete = productRepository.findById(productid);

        if(productToDelete.isPresent()) {
            try {
                productRepository.delete(productToDelete.get());
                return ResponseEntity.status(200).body("{ \"message\": \"product with id: " + productid + " successfully deleted\"}");
            } catch (Exception e) {
                return ResponseEntity.status(500).body(e);
            }
        }

        return ResponseEntity.status(404).body("{ \"message\": \"product with id: " + productid + " not found\"}");
    }

    @GetMapping
    public ResponseEntity<Object> getProducts() {
        return new ResponseEntity<>(productRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable String id) {
        return new ResponseEntity<>(productRepository.findById(id), HttpStatus.OK);
    }

    @GetMapping("/clientid/{id}")
    public ResponseEntity getClientByProductId(@PathVariable String id) {
        Optional<Product> product = productRepository.findById(id);
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
        return ResponseEntity.status(404).body("{ \"message\": \"product with id: " + id + " not found\"}");
    }

    //TODO: implement
    @GetMapping(params = "clientToken")
    public ResponseEntity<Object> getProductsByClientToken(@RequestParam("clientid") String clientId) {
        List<Product> products = productRepository.findByClientId(clientId);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    //checks if product with id productId exists for client with clientToken
    @GetMapping(params = {"productId", "clientToken"})
    public ResponseEntity<Object> productExistByTokenAndId(@PathParam("productId") String productId, String clientToken) {
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
        System.out.println(productId);
        System.out.println(image);

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