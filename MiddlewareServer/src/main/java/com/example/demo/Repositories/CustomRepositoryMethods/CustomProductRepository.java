package com.example.demo.Repositories.CustomRepositoryMethods;

import com.example.demo.DataModels.product.Product;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface CustomProductRepository {
    @Query("{clientId:'?0'}")
    List<Product> findByClientId(String clientId);
}
