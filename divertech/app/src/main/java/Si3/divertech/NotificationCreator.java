package Si3.divertech;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Observable;

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
                String userEmail = dataSnapshot.child("email").getValue(String.class);
                String firstName = dataSnapshot.child("firstName").getValue(String.class);
                String lastName = dataSnapshot.child("lastName").getValue(String.class);
                String address = dataSnapshot.child("address").getValue(String.class);
                String phoneNumber = dataSnapshot.child("phoneNumber").getValue(String.class);
                String language = dataSnapshot.child("language").getValue(String.class);
                Boolean admin = dataSnapshot.child("admin").getValue(Boolean.class);
                notificationCreatorUser = new User(userId, userEmail, firstName, lastName, address, phoneNumber, language, admin != null ? admin : false);
                getInstance().setChanged();
                Log.d("TAG", instance.hasChanged() + " ");
                getInstance().notifyObservers();

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
