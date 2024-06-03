package Si3.divertech.parking;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Observable;
import java.util.Optional;

import Si3.divertech.users.User;
import Si3.divertech.users.UserData;

public class ParkingCreator extends Observable {
    private final static ParkingCreator instance = new ParkingCreator();
    private static List<User> ParkingCreatorUser;

    private ParkingCreator() {
        ParkingCreatorUser = new ArrayList<>();
    }

    public void getUser(String userId) {
        if (userId == null)
            return;

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(userId);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User created = UserData.getUserBySnapshot(dataSnapshot, userId);
                ParkingCreatorUser.forEach(user -> {
                    if (user.getId().equals(created.getId())) user = created;
                });
                if (!getParkingCreatorUser().contains(created)) ParkingCreatorUser.add(created);
                setChanged();
                notifyObservers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TAG", databaseError.getMessage());
            }
        });
    }

    public static ParkingCreator getInstance() {
        return instance;
    }

    public static Optional<User> getUserInList(String id) {
        return ParkingCreatorUser.stream().filter(user -> Objects.equals(user.getId(), id)).findFirst();
    }

    public List<User> getParkingCreatorUser() {
        return ParkingCreatorUser;
    }
}
