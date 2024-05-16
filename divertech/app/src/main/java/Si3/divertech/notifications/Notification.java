package Si3.divertech.notifications;

import androidx.annotation.NonNull;

import Si3.divertech.NotificationTypes;

public class Notification {
    private String id;
    private String eventId;
    private NotificationTypes type;
    private String description;

    private String userCreatorId;

    public Notification() {
    }

    public Notification(String id, String eventId, int type, String description, String userCreatorId) {
        this.id = id;
        this.eventId = eventId;
        this.type = NotificationTypes.values()[type];
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

    public String getType() {
        return type.getTitle();
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
                ", title='" + type + '\'' +
                ", description='" + description + '\'' +
                ", user id='" + userCreatorId + '\'' +
                '}';
    }
}
