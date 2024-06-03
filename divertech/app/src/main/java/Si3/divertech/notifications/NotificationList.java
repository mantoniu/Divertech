package Si3.divertech.notifications;

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
import java.util.Objects;
import java.util.Observable;

import Si3.divertech.events.EventList;
import Si3.divertech.users.UserData;

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
        String userId = UserData.getInstance().getUserId();
        if (userId == null)
            return;

        FirebaseDatabase.getInstance().getReference()
                .child("Users").child(userId).child("notifications").child(id).removeValue()
                .addOnSuccessListener(aVoid -> {
                    setChanged();
                    notifyObservers();
                    notificationMap.remove(id);
                })
                .addOnFailureListener(e -> Log.d("NOTIFICATION DELETE ERROR", ""));
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
                if (!snapshot.exists()) {
                    setChanged();
                    notifyObservers();
                }
                for (DataSnapshot registrationSnapshot : snapshot.getChildren()) {
                    String notificationId = registrationSnapshot.getKey();
                    if (notificationId == null)
                        return;

                    DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference()
                            .child("Notifications").child(notificationId);

                    notificationRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String eventId = snapshot.child("eventId").getValue(String.class);
                            int type = snapshot.child("type").getValue(int.class);
                            String description = snapshot.child("description").getValue(String.class);
                            String descriptionEn = snapshot.child("descriptionEn").getValue(String.class);
                            String userCreatorId = snapshot.child("userCreatorId").getValue(String.class);

                            Notification notification = new Notification(notificationId, eventId, "title", type, description, descriptionEn, userCreatorId);
                            Log.d("NOTIFICATION", notification.toString());
                            addNotification(notification);
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", error.getMessage());
            }
        });

    }

    private void retrieveUserIds(String eventId, String notificationId, String creatorId) {
        String userId = UserData.getInstance().getUserId();
        if (userId == null)
            return;

        DatabaseReference userRegistrationsRef = FirebaseDatabase.getInstance().getReference().child("Registrations");

        userRegistrationsRef.orderByChild("eventId").equalTo(eventId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot registrationSnapshot : dataSnapshot.getChildren()) {
                    String userId = registrationSnapshot.child("userId").getValue(String.class);
                    if (userId != null && !userId.equals(creatorId)) {
                        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("notifications").child(notificationId).setValue(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("NOTIFICATIONS REQUEST ERROR : ", databaseError.getMessage());
            }
        });
    }

    public void sendNotification(String eventId, NotificationTypes types, String description, String descriptionEn, String userCreatorId) {
        DatabaseReference notificationsRef = FirebaseDatabase.getInstance().getReference().child("Notifications");
        DatabaseReference newNotificationRef = notificationsRef.push();
        newNotificationRef.child("eventId").setValue(eventId);
        newNotificationRef.child("type").setValue(types.ordinal());
        newNotificationRef.child("description").setValue(description);
        newNotificationRef.child("descriptionEn").setValue(descriptionEn);
        newNotificationRef.child("userCreatorId").setValue(userCreatorId);
        retrieveUserIds(eventId, newNotificationRef.getKey(), userCreatorId);
    }

    public void sendUserNotification(String eventId, NotificationTypes types, String description) {
        DatabaseReference notificationsRef = FirebaseDatabase.getInstance().getReference().child("Notifications");
        DatabaseReference newNotificationRef = notificationsRef.push();
        newNotificationRef.child("eventId").setValue(eventId);
        newNotificationRef.child("type").setValue(types.ordinal());
        newNotificationRef.child("description").setValue(description);
        newNotificationRef.child("userCreatorId").setValue(UserData.getInstance().getUserId());
        newNotificationRef.child("descriptionEn").setValue(description);
        FirebaseDatabase.getInstance().getReference().child("Users").child(EventList.getInstance().getEvent(eventId).getOrganizer()).child("notifications").child(Objects.requireNonNull(newNotificationRef.getKey())).setValue(true);
    }

    public void reset() {
        instance = null;
    }
}