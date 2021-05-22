package OMTIAMT.serviceServer.Server.Model.Notifications;

import lombok.Data;

@Data
public class Message {

    private String customerName;

    private String name;

    private String clientEmail;

    private String details;
}
