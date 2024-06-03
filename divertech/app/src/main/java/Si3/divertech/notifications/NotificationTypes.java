package Si3.divertech.notifications;

import androidx.annotation.NonNull;

public enum NotificationTypes {
    INCIDENT("Incident signalé", "Incident reported", "Signaler un incident", "Report an incident", "Incident", "Incident"), LOST_OBJECT("Objet perdu", "Lost item", "Objet perdu", "Lost property", "Objet perdu", "Lost property"), CONTACT("Demande d'informations", "Request for information", "Demande d'informations", "Information request", "Information", "Information");

    private final String titleFr;
    private final String titleEn;
    private final String contentFr;
    private final String contentEn;
    private final String notificationHeaderFr;
    private final String notificationHeaderEn;

    NotificationTypes(String titleFr, String titleEn, String contentFr, String contentEn, String notificationHeaderFr, String notificationHeaderEn) {
        this.titleFr = titleFr;
        this.contentFr = contentFr;
        this.notificationHeaderFr = notificationHeaderFr;
        this.titleEn = titleEn;
        this.contentEn = contentEn;
        this.notificationHeaderEn = notificationHeaderEn;
    }

    public String getTitleFr() {
        return titleFr;
    }

    public String getContentFr() {
        return contentFr;
    }

    public String getNotificationHeaderFr() {
        return notificationHeaderFr;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public String getContentEn() {
        return contentEn;
    }

    public String getNotificationHeaderEn() {
        return notificationHeaderEn;
    }

    public static NotificationTypes fromInt(int value) {
        for (NotificationTypes type : NotificationTypes.values()) {
            if (type.ordinal() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid notification type value: " + value);
    }

    @NonNull
    @Override
    public String toString() {
        return getContentFr();
    }
}
