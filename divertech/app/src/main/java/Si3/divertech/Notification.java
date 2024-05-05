package Si3.divertech;

import androidx.annotation.NonNull;

public class Notification {
    private String id;
    private String eventId;
    private String title;
    private String description;

    public Notification() {
    }

    public Notification(String id, String eventId, int type, String description) {
        this.id = id;
        this.eventId = eventId;
        this.title = NotificationTypes.values()[type].getTitle();
        this.description = description;
    }

    public String getDescription() {
        return description;
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

    @NonNull
    @Override
    public String toString() {
        return "Notification{" +
                "id='" + id + '\'' +
                ", eventId='" + eventId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
