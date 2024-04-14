package Si3.divertech;

import java.util.HashMap;
import java.util.Map;

public class NotificationList {
    private static final String default_url = "https://static.vecteezy.com/system/resources/previews/010/366/202/original/bell-icon-transparent-notification-free-png.png";
    private static final Notification notification1 = new Notification("0", "0", default_url, "Incident1", "Un incident à eu lieu");
    private static final Notification notification2 = new Notification("1", "1", default_url, "Incident2", "Un incident à eu lieu");
    private static final Notification notification3 = new Notification("2", "2", default_url, "Incident3", "Un incident à eu lieu");
    private static final Notification notification4 = new Notification("3", "3", default_url, "Incident4", "Un incident à eu lieu");

    private static final Map<String, Notification> notificationMap = new HashMap<String, Notification>() {{
        put(notification1.getId(), notification1);
        put(notification2.getId(), notification2);
        put(notification3.getId(), notification3);
        put(notification4.getId(), notification4);
    }};

    public static Map<String, Notification> getNotificationMap() {
        return notificationMap;
    }

    public static void addNotification(Notification notification) {
        notificationMap.putIfAbsent(notification.getId(), notification);
    }

    public static void addNotification(String id, String eventId, String pictureUrl, String title, String description) {
        addNotification(new Notification(id, eventId, pictureUrl, title, description));
    }

    public static void deleteNotification(Notification notification) {
        notificationMap.remove(notification.getId());
    }

    public static void deleteNotification(String id) {
        notificationMap.remove(id);
    }
}
