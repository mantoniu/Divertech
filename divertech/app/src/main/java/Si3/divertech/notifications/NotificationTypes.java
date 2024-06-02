package Si3.divertech.notifications;

import androidx.annotation.NonNull;

public enum NotificationTypes {
    INCIDENT("Incident signalé", "Signaler un incident","Incident"), LOST_OBJECT("Objet perdu", "Objet perdu","Objet perdu"), CONTACT("Demande d'informations", "Demande d'informations","Information");

    private final String title;
    private final String content;
    private final String notificationHeader;

    NotificationTypes(String title, String content,String notificationHeader) {
        this.title = title;
        this.content = content;
        this.notificationHeader = notificationHeader;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getNotificationHeader() {
        return notificationHeader;
    }

    @NonNull
    @Override
    public String toString() {
        return content;
    }

    public static NotificationTypes fromInt(int value) {
        for (NotificationTypes type : NotificationTypes.values()) {
            if (type.ordinal() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid notification type value: " + value);
    }
}
