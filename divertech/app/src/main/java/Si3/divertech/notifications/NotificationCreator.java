package Si3.divertech.notifications;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Observable;

import Si3.divertech.users.User;
import Si3.divertech.users.UserData;

public class NotificationCreator extends Observable {
    private final static NotificationCreator instance = new NotificationCreator();
    private static User notificationCreatorUser;

    private NotificationCreator() {
    }

    public void getUser(String userId) {
        if (userId == null)
            return;

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(userId);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notificationCreatorUser = UserData.getUserBySnapshot(dataSnapshot, userId);
                setChanged();
                notifyObservers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TAG", databaseError.getMessage());
            }
        });
    }

    public static NotificationCreator getInstance() {
        return instance;
    }

    public User getNotificationCreatorUser() {
        return notificationCreatorUser;
    }
}
