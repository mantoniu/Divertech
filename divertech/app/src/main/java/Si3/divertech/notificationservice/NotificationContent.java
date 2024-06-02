package Si3.divertech.notificationservice;

import Si3.divertech.notifications.NotificationTypes;

public class NotificationContent {

    private NotificationTypes type;
    private String body;
    private String imageUrl;
    private String channel;

    public NotificationContent(NotificationTypes types,String body, String channel){
        this.type = types;
        this.body = body;
        this.channel = channel;
    }

    public NotificationContent(NotificationTypes types, String body, String imageUrl, String channel) {
        this.type = types;
        this.body = body;
        this.imageUrl = imageUrl;
        this.channel = channel;
    }


    public String getTitle() {
        return type.toString();
    }

    public NotificationTypes getType() {
        return type;
    }

    public String getBody() {
        return body;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getChannel() {
        return channel;
    }


}
