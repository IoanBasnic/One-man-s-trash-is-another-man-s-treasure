package OMTIAMT.serviceServer.Server.Model.Notifications;

import lombok.Data;

@Data
public class Notification {

    private String sellerName;

    private String toEmail;

    private Message message;
}
