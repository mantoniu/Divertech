package Si3.divertech;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class UserData {
    private static User connectedUser;
    private static FirebaseUser firebaseUser;
    private static String userId;

    public static String getUserId() {
        return userId;
    }

    public static void requestUserData(FirebaseUser user) {
        if (user == null)
            return;

        String userId = user.getUid();
        String userEmail = user.getEmail();

        UserData.firebaseUser = user;
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
                connectedUser.setEmail(userEmail);

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

    public static void writeNewUser(String userId, String name, String lastName, String address, String phoneNumber, String language) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        usersRef.child("address").setValue(address);
        usersRef.child("lastName").setValue(lastName);
        usersRef.child("name").setValue(name);
        usersRef.child("phoneNumber").setValue(phoneNumber);
        usersRef.child("language").setValue(language);
    }

    public static void updateUser(String name, String lastName, String address, String phoneNumber, String language, String email, String password) {
        if (firebaseUser == null)
            return;

        writeNewUser(userId, name, lastName, address, phoneNumber, language);

        AuthCredential credential = EmailAuthProvider
                .getCredential(Objects.requireNonNull(firebaseUser.getEmail()), password);

        firebaseUser.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        firebaseUser.updateEmail(email)
                                .addOnCompleteListener(task2 -> {
                                    if (task2.isSuccessful()) {
                                        UserData.requestUserData(firebaseUser);
                                    } else {
                                        Exception exception = task2.getException();
                                        if (exception != null) {
                                            Log.e("UPDATE EMAIL ERROR", "Erreur lors de la mise à jour de l'email", exception);
                                        }
                                    }
                                });
                    } else {
                        Exception exception = task.getException();
                        Log.d("RE-AUTHENTICATION ERROR", "", exception);
                    }
                });


    }
}
