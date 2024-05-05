package Si3.divertech;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserData {
    private static User connectedUser;
    private static String userId;

    public static String getUserId() {
        return userId;
    }

    public static void requestUserData(String userId) {
        if (userId == null)
            return;
        UserData.userId = userId;

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(userId);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                connectedUser = dataSnapshot.getValue(User.class);
                if (connectedUser == null)
                    return;

                connectedUser.setId(userId);

                Log.d("CONNECTED USER", connectedUser.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TAG", databaseError.getMessage());
            }
        });
    }

    public static User getConnectedUser() {
        return connectedUser;
    }

    public static void writeNewUser(String userId, String name, String lastName, String email, String address, String phoneNumber, String language) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        usersRef.child("address").setValue(address);
        usersRef.child("email").setValue(email);
        usersRef.child("lastName").setValue(lastName);
        usersRef.child("name").setValue(name);
        usersRef.child("phoneNumber").setValue(phoneNumber);
        usersRef.child("language").setValue(language);
    }
}
