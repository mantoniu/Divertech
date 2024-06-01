package Si3.divertech.notifications;

import androidx.annotation.NonNull;

public class Notification {
    private String id;
    private String eventId;
    private String title;
    private String description;

    private String userCreatorId;

    public Notification() {
    }

    public Notification(String id, String eventId, String title, String description, String userCreatorId) {
        this.id = id;
        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.userCreatorId = userCreatorId;
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

    public String getNotificationCreatorUser() {
        return userCreatorId;
    }

    @NonNull
    @Override
    public String toString() {
        return "Notification{" +
                "id='" + id + '\'' +
                ", eventId='" + eventId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", user id='" + userCreatorId + '\'' +
                '}';
    }
}
