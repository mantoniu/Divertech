package Si3.divertech;

import java.util.HashMap;
import java.util.Map;

public class NotificationList {
    private static final Notification notification1 = new Notification("0", "0", ListEvent.getEvent("0").getPictureUrl(), NotificationTypes.INCIDENT, "Laoreet sit amet cursus sit amet dictum sit amet justo donec enim diam vulputate ut pharetra sit amet aliquam id diam maecenas ultricies mi eget mauris pharetra et ultrices neque ornare aenean euismod elementum nisi quis eleifend quam adipiscing vitae proin sagittis nisl rhoncus mattis rhoncus urna neque viverra justo nec ultrices dui sapien eget mi proin sed libero enim sed faucibus turpis in eu mi bibendum neque egestas congue quisque egestas diam in arcu cursus euismod quis viverra nibh cras pulvinar mattis nunc sed blandit libero volutpat sed cras ornare arcu dui vivamus arcu felis bibendum ut tristique et egestas quis ipsum suspendisse ultrices gravida dictum fusce ut placerat orci nulla pellentesque dignissim enim sit amet venenatis urna cursus eget nunc scelerisque viverra mauris in aliquam sem fringilla ut morbi tincidunt augue interdum velit euismod in pellentesque massa placerat duis ultricies lacus sed turpis tincidunt id aliquet risus feugiat in ante metus dictum at tempor commodo ullamcorper a lacus vestibulum sed arcu non odio euismod lacinia at quis risus sed vulputate odio ut enim blandit volutpat maecenas volutpat blandit aliquam etiam erat velit scelerisque in dictum non consectetur a erat nam at lectus urna duis convallis convallis tellus id.");
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
