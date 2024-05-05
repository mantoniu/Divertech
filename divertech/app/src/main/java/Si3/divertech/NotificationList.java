package Si3.divertech;

import android.util.Log;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class NotificationList {

    private static final Map<String, Notification> notificationMap = new HashMap<>();
    private static BaseAdapter adapter;

    public static Notification getNotification(String notificationId) {
        return notificationMap.get(notificationId);
    }

    public static Map<String, Notification> getNotificationMap() {
        return notificationMap;
    }

    public static void addNotification(Notification notification) {
        notificationMap.put(notification.getId(), notification);
    }

    public static void clear() {
        notificationMap.clear();
    }


    public static void addNotification(String id, String eventId, int type, String description) {
        addNotification(new Notification(id, eventId, type, description));
    }

    public static void deleteNotification(Notification notification) {
        notificationMap.remove(notification.getId());
    }

    public static void deleteNotification(String id) {
        notificationMap.remove(id);
    }

    public static void setAdapter(BaseAdapter adapter) {
        NotificationList.adapter = adapter;
    }

    public static void requestData() {
        Log.d("NOTIF REQUEST", "");
        String userId = UserData.getUserId();
        if (userId == null)
            return;

        DatabaseReference userNotificationsRef = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(userId).child("notifications");

        userNotificationsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notificationMap.clear();
                for (DataSnapshot registrationSnapshot : snapshot.getChildren()) {
                    String notificationId = registrationSnapshot.getValue(String.class);
                    if (notificationId == null)
                        return;

                    DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference()
                            .child("Notifications").child(notificationId);

                    notificationRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String eventId = snapshot.child("eventId").getValue(String.class);
                            String description = snapshot.child("description").getValue(String.class);
                            Integer type = snapshot.child("type").getValue(Integer.class);

                            if (type != null) {
                                Notification notification = new Notification(notificationId, eventId, type, description);
                                Log.d("NOTIFICATION", notification.toString());
                                NotificationList.addNotification(notification);
                            }
                            if (adapter != null)
                                adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d("TAG", error.getMessage());
                        }
                    });
                }
                if (adapter != null)
                    adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", error.getMessage());
            }
        });

    }
}
