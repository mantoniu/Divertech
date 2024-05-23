package Si3.divertech.events;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Observable;

import Si3.divertech.users.User;
import Si3.divertech.users.UserData;

public class EventOrganizer extends Observable {
    private final static EventOrganizer instance = new EventOrganizer();
    private static User eventOrganizer;

    private EventOrganizer() {
    }

    public void getUser(String userId) {
        if (userId == null)
            return;
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(userId);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventOrganizer = UserData.getUserBySnapshot(dataSnapshot, userId);
                setChanged();
                notifyObservers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public static EventOrganizer getInstance() {
        return instance;
    }

    public User getEventOrganizer() {
        return eventOrganizer;
    }
}
