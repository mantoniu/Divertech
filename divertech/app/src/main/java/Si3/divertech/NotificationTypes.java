package Si3.divertech;

import androidx.annotation.NonNull;

public enum NotificationTypes {
    INCIDENT("Incident signalé", "Signaler un incident"), LOST_OBJECT("Objet perdu", "Objet perdu"), CONTACT("Demande d'informations", "Demande d'informations");

    private final String title;
    private final String content;

    NotificationTypes(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    @NonNull
    @Override
    public String toString() {
        return content;
    }
}
