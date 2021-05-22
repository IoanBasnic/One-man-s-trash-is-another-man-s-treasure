package OMTIAMT.serviceServer.Server.Controller;

import OMTIAMT.serviceServer.Server.Model.Client;
import OMTIAMT.serviceServer.Server.Model.EmailBuilder;
import OMTIAMT.serviceServer.Server.Model.Mail;
import OMTIAMT.serviceServer.Server.Model.Notifications.Notification;
import OMTIAMT.serviceServer.Server.Service.EmailService;
import OMTIAMT.serviceServer.Server.Service.NotificationService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.mail.HtmlEmail;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

@RestController
@RequestMapping(value = "/product")
public class NotificationController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/notif")
    public ResponseEntity sendNotification(@RequestBody Notification notification) throws IOException, MessagingException {

        String jsonInString = new Gson().toJson(notification);
        JsonObject dataJson = (JsonObject) JsonParser.parseString(jsonInString);

        JsonObject messageJson = dataJson.get("message").getAsJsonObject();

        NotificationService notificationService = new NotificationService();

        ResponseEntity responseEntity = notificationService.checkNotification(dataJson);
        if(responseEntity.getStatusCodeValue() == 404){
            return responseEntity;
        }

        responseEntity = notificationService.checkMessage(messageJson);
        if(responseEntity.getStatusCodeValue() == 404){
            return responseEntity;
        }

        String toEmail = dataJson.get("toEmail").getAsString();
        String sellerName = dataJson.get("sellerName").getAsString();

        String customerName = messageJson.get("customerName").getAsString();
        String productName = messageJson.get("name").getAsString();
        String details = messageJson.get("details").getAsString();
        String customerEmail  = messageJson.get("clientEmail").getAsString();

        Mail mail = null;
        try {
            mail = new EmailBuilder()
                        .To(toEmail)
                        .Template("notification-mail-template.html")
                        .AddContext("sellerName", sellerName)
                        .AddContext("customerName", customerName)
                        .AddContext("productName", productName)
                        .AddContext("details", details)
                        .AddContext("customerEmail", customerEmail)
                        .Subject("Notification")
                        .createMail();

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e,HttpStatus.BAD_REQUEST);
        }

        emailService.sendHTMLMail(mail);

        return new ResponseEntity(notification,HttpStatus.OK);
    }
}
