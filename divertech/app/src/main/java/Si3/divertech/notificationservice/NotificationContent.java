package Si3.divertech.notificationservice;

import Si3.divertech.notifications.NotificationTypes;

public class NotificationContent {

    private String title;
    private String body;
    private String imageUrl;
    private String channel;

    public NotificationContent(String title,String body, String channel){
        this.title = title;
        this.body = body;
        this.channel = channel;
    }

    public NotificationContent(String title, String body, String imageUrl, String channel) {
        this.title = title;
        this.body = body;
        this.imageUrl = imageUrl;
        this.channel = channel;
    }


    public String getTitle() {
        return title;
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
