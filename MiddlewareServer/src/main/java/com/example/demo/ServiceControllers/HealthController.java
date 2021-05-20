package com.example.demo.ServiceControllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/")
public class HealthController {
    @GetMapping
    public ResponseEntity<Object> getHealthCheck() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
