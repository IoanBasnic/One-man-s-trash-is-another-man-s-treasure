package OMTIAMT.serviceServer.Server.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/")
public class HealthController {
    @GetMapping
    public ResponseEntity<Object> getHealthCheck() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
