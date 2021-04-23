package com.example.demo.ServiceControllers;

import com.example.demo.DataModels.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

interface ProductRepository extends MongoRepository<Product, String>{ }


@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Product add(@RequestBody Product product) {
        return productRepository.save(product);
    }

    @GetMapping
    public ResponseEntity<Object> getProducts() {
        return new ResponseEntity<>(productRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable String id) {
        return new ResponseEntity<>(productRepository.findById(id), HttpStatus.OK);
    }

    @GetMapping(value = "/", params = "clientid")
    public ResponseEntity<Object> getProductsByClientId(@RequestParam("clientid") String clientId) {
        List<Product> products = productRepository.findAll();
        List<Product> filteredProducts = products.stream().filter(product -> product.getClientId().equals(clientId)).collect(Collectors.toList());
        return new ResponseEntity<>(filteredProducts, HttpStatus.OK);
    }
}