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

        Optional<Client> existingClient = clientRepository.findById(userInfo.get("sub").getAsString());

        if(existingClient.isEmpty())
            return ResponseEntity.status(404).body("{ \"message\": \"client not found\"}");


        Optional<Client> foundClient = clientRepository.findById(product.getClientId());
        Product savedProduct;

        if(foundClient.isPresent()){
            try {
                savedProduct = productRepository.save(product);
                return ResponseEntity.status(200).body(savedProduct);
            } catch (Exception e){
                return ResponseEntity.status(500).body(e);
            }
        }
        return ResponseEntity.status(404).body("{ \"message\": \"client id not found\"}");
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

    @GetMapping(params = "clientid")
    public ResponseEntity<Object> getProductsByClientId(@RequestParam("clientid") String clientId) {
        List<Product> products = productRepository.findByClientId(clientId);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}