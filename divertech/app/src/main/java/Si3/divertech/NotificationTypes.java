package Si3.divertech;

public enum NotificationTypes {
    INCIDENT("Incident signalé"), LOST_OBJECT("Objet perdu"), CONTACT("Demande d'informations");

    private final String title;

    NotificationTypes(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
