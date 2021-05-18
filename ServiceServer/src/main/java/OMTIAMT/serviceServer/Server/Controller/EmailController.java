package OMTIAMT.serviceServer.Server.Controller;

import OMTIAMT.serviceServer.Server.Model.Client;
import OMTIAMT.serviceServer.Server.Model.EmailBuilder;
import OMTIAMT.serviceServer.Server.Model.Mail;
import OMTIAMT.serviceServer.Server.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Optional;

interface ClientRepository extends MongoRepository<Client, String> {

}

@RestController
@RequestMapping("/register")
public class EmailController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping("/sendmail/{id}")
    public ResponseEntity<Object> sendmail(@PathVariable String id) throws Exception {
        Optional<Client> cl = clientRepository.findById(id);

        if (cl.isPresent()){
            System.out.println("client exists");
            Mail mail = new EmailBuilder()
                    .To(cl.get().getEmail())
                    .Template("mail-template.html")
                    .AddContext("subject", "Test Email")
                    .AddContext("content", "Hello World!")
                    .Subject("Registration")
                    .createMail();
            emailService.sendHTMLMail(mail);
        }

        return new ResponseEntity<>(clientRepository.findById(id), HttpStatus.OK);

        // TESTING
//            Mail mail = new EmailBuilder()
//                    .To("sebi.atletico99@yahoo.com")
//                    .Template("mail-template.html")
//                    .AddContext("subject", "Test Email")
//                    .AddContext("content", "Hello World!")
//                    .Subject("Registration")
//                    .createMail();
//            emailService.sendHTMLMail(mail);
//
//        return new ResponseEntity<>("email sent", HttpStatus.OK);
    }

}

