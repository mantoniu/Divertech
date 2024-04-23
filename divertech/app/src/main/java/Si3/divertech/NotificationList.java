package Si3.divertech;

import java.util.HashMap;
import java.util.Map;

public class NotificationList {
    private static final Notification notification1 = new Notification("0", "0", ListEvent.getEvent("0").getPictureUrl(), NotificationTypes.INCIDENT, "Un incident à eu lieu Iff eziohgio zogi ezrjg er gjozr kgj jker gkjer kjgenropgj eripjg aer ngkmja erkjg jerngiovreoing jnaekjg kea roibgerioahbio nake nrklgaek rijeraiphj ipearjio hklr  realn giernaiph khrn, klhnaerjl nhineri nhlkenlknlmenrioag jiera igjiera ngkndjkl nglarneing opaerohg pag piarnegn aekrn gkearngkl ernih iernklg nfnklg nerkln hklrnemh arnehl aerkhnaekl nhklnrekl anh");
    private static final Notification notification2 = new Notification("1", "1", ListEvent.getEvent("1").getPictureUrl(), NotificationTypes.INCIDENT, "Un incident à eu lieu");
    private static final Notification notification3 = new Notification("2", "2", ListEvent.getEvent("2").getPictureUrl(), NotificationTypes.INCIDENT, "Un incident à eu lieu");
    private static final Notification notification4 = new Notification("3", "3", ListEvent.getEvent("3").getPictureUrl(), NotificationTypes.INCIDENT, "Un incident à eu lieu");

    private static final Map<String, Notification> notificationMap = new HashMap<String, Notification>() {{
        put(notification1.getId(), notification1);
        put(notification2.getId(), notification2);
        put(notification3.getId(), notification3);
        put(notification4.getId(), notification4);
    }};

    public static Notification getNotification(String notificationId) {
        return notificationMap.get(notificationId);
    }

    public static Map<String, Notification> getNotificationMap() {
        return notificationMap;
    }

    public static void addNotification(Notification notification) {
        notificationMap.putIfAbsent(notification.getId(), notification);
    }

    public static void addNotification(String id, String eventId, String pictureUrl, NotificationTypes type, String description) {
        addNotification(new Notification(id, eventId, pictureUrl, type, description));
    }

    public static void deleteNotification(Notification notification) {
        notificationMap.remove(notification.getId());
    }

    public static void deleteNotification(String id) {
        notificationMap.remove(id);
    }
}
