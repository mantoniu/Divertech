package Si3.divertech;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public class NotificationList extends Observable {
    private static NotificationList instance;
    private static final Map<String, Notification> notificationMap = new HashMap<>();

    private NotificationList() {
        requestData();
    }

    public static NotificationList getInstance() {
        if (instance == null)
            instance = new NotificationList();
        return instance;
    }

    public Notification getNotification(String notificationId) {
        return notificationMap.get(notificationId);
    }

    public List<Notification> getNotifications() {
        return new ArrayList<>(notificationMap.values());
    }

    private void addNotification(Notification notification) {
        notificationMap.put(notification.getId(), notification);
    }

    public void deleteNotification(String id) {
        notificationMap.remove(id);

        String userId = UserData.getInstance().getUserId();
        if (userId == null)
            return;

        FirebaseDatabase.getInstance().getReference()
                .child("Users").child(userId).child("notifications").child(id).removeValue();
    }

    public void requestData() {
        String userId = UserData.getInstance().getUserId();
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
                            String userCreatorId = snapshot.child("userCreatorId").getValue(String.class);

                            if (type != null) {
                                Notification notification = new Notification(notificationId, eventId, type, description, userCreatorId);
                                Log.d("NOTIFICATION", notification.toString());
                                addNotification(notification);
                                setChanged();
                                notifyObservers();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d("TAG", error.getMessage());
                        }
                    });
                }
                setChanged();
                notifyObservers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", error.getMessage());
            }
        });

    }
}
