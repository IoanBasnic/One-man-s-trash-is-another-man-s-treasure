package com.example.demo.Repositories;

import com.example.demo.DataModels.product.Product;
import com.example.demo.Repositories.CustomRepositoryMethods.CustomProductRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String>, CustomProductRepository {
}
