package com.example.demo.Repositories;

import com.example.demo.DataModels.client.Client;
import com.example.demo.Repositories.CustomRepositoryMethods.CustomClientRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClientRepository extends MongoRepository<Client, String>, CustomClientRepository {}
