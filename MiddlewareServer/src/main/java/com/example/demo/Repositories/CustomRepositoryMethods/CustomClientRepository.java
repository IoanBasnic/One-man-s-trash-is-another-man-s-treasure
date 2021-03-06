package com.example.demo.Repositories.CustomRepositoryMethods;

import com.example.demo.DataModels.client.Client;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface CustomClientRepository {
    @Query("{email:'?0'}")
    Optional<Client> findByEmail(String email);

    @Query("{authId:'?0'}")
    Optional<Client> findByAuthId(String authId);
}