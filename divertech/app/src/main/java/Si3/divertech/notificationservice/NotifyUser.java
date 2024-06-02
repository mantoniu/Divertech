package Si3.divertech.notificationservice;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

import Si3.divertech.users.UserData;

public class NotifyUser {

    private NotifyUser() {
    }

    /**
     * Notify a user with a specific id
     * @param userId the id of the user
     * @param context the context of the application
     */
    public static void notifyUserWithId(String userId,NotificationContent notification, Context context) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    String token = snapshot.child("FCMtoken").getValue(String.class);
                    String language = snapshot.child("language").getValue(String.class);

                    if (token != null && language != null) {
                        notifyUserWithToken(token, notification,language, context);
                    }
                } else {
                    Log.e("DBToken", "User does not exist");
                }
            } else {
                Log.e("DBToken", "Error getting data", task.getException());
            }
        });
    }

    /**
     * Notify a user with a specific token
     * @param token the token of the user
     * @param context the context of the application
     */
    public static void notifyUserWithToken(String token,NotificationContent notification,String lang, Context context) {
        //wont notify the sender of the notification
        if(token.equals(DBToken.getInstance().getToken()))
            return;
        FCMSender sender = new FCMSender();
        try {
            sender.sendNotification(context,token,lang,notification);
        } catch (IOException e) {
            Log.d("FCM", e.getMessage());
        }
    }

    /**
     * Notify all users that are registered to an event
     * @param eventId the id of the event
     * @param context the context of the application
     */
    public static void notifyUserWithEventId(String eventId,NotificationContent notification, Context context) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Registrations");
        ref.orderByChild("eventId").equalTo(eventId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    String userId = child.child("userId").getValue(String.class);
                    notifyUserWithId(userId,notification, context);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DBToken", "Error getting data", error.toException());
            }
        });
    }

}
