package Si3.divertech;

public class Notification {
    private final String id;
    private final String eventId;
    private final String pictureUrl;
    private final String title;
    private final String description;

    public Notification(String id, String eventId, String pictureUrl, NotificationTypes title, String description) {
        this.id = id;
        this.eventId = eventId;
        this.pictureUrl = pictureUrl;
        this.title = title.getTitle();
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getEventId() {
        return eventId;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
