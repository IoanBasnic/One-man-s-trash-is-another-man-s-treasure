package OMTIAMT.serviceServer.Server.Service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public ResponseEntity checkNotification(JsonObject notifJSON) {

        if (notifJSON.isJsonNull() || notifJSON == null){
            return ResponseEntity.status(404)
                                 .body("{ \"message\": \"JSON can't be null\"}");
        }

        if(!notifJSON.has("toEmail") || notifJSON.get("toEmail").getAsString().isEmpty()){
            return ResponseEntity.status(404)
                                 .body("{ \"message\": \"Email destination is missing\"}");
        }

        if(!notifJSON.has("sellerName") ||  notifJSON.get("sellerName").getAsString().isEmpty()){

            return ResponseEntity.status(404)
                                 .body("{ \"message\": \"Seller name is missing\"}");
        }

        return ResponseEntity.status(200).body("Success");
    }

    public ResponseEntity checkMessage(JsonObject messageJSON){

        if (messageJSON.isJsonNull() || messageJSON == null){

            return ResponseEntity.status(404)
                                 .body("{ \"message\": \"Message JSON can't be null\"}");
        }

        if(!messageJSON.has("customerName") || messageJSON.get("customerName").getAsString().isEmpty()){

            return ResponseEntity.status(404)
                    .body("{ \"message\": \"Customer name is missing\"}");
        }

        if(!messageJSON.has("name") || messageJSON.get("name").getAsString().isEmpty()){

            return ResponseEntity.status(404)
                                 .body("{ \"message\": \"Product name is missing\"}");
        }

        if(!messageJSON.has("clientEmail") || messageJSON.get("clientEmail").getAsString().isEmpty()){

            return ResponseEntity.status(404)
                    .body("{ \"message\": \"Client email is missing\"}");
        }

        if(!messageJSON.has("details") || messageJSON.get("details").getAsString().isEmpty()){

            return ResponseEntity.status(404)
                                 .body("{ \"message\": \"Details is missing\"}");
        }

        return ResponseEntity.status(200).body("Success");
    }
}
