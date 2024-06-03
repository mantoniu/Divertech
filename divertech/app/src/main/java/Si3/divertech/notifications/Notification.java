package Si3.divertech.notifications;

import androidx.annotation.NonNull;

public class Notification {
    private String id;
    private String eventId;
    private String title;

    private NotificationTypes type;
    private String description;
    private String descriptionEn;

    private String userCreatorId;

    public Notification() {
    }

    public Notification(String id, String eventId, String title, int type, String description, String descriptionEn, String userCreatorId) {
        this.id = id;
        this.eventId = eventId;
        this.title = title;
        this.type = NotificationTypes.fromInt(type);
        this.description = description;
        this.descriptionEn = descriptionEn;
        this.userCreatorId = userCreatorId;
    }

    public String getDescription() {
        return description;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public String getEventId() {
        return eventId;
    }

    public String getId() {
        return id;
    }

    public NotificationTypes getType() {
        return type;
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
                ", type='" + type.toString() + '\'' +
                ", description='" + description + '\'' +
                ", descriptionEn='" + descriptionEn + '\'' +
                ", user id='" + userCreatorId + '\'' +
                '}';
    }
}
