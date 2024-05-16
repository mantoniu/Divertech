package Si3.divertech.users;

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
import java.util.Observable;

public class UserData extends Observable {
    private static UserData instance;
    private static User connectedUser;
    private static FirebaseUser firebaseUser;
    private static String userId;

    private UserData() {
    }

    public static UserData getInstance() {
        if (instance == null)
            instance = new UserData();
        return instance;
    }

    public String getUserId() {
        return userId;
    }

    public void requestUserData(FirebaseUser user) {
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
                String name = dataSnapshot.child("firstName").getValue(String.class);
                String lastName = dataSnapshot.child("lastName").getValue(String.class);
                String address = dataSnapshot.child("address").getValue(String.class);
                String phoneNumber = dataSnapshot.child("phoneNumber").getValue(String.class);
                String language = dataSnapshot.child("language").getValue(String.class);
                Boolean admin = dataSnapshot.child("admin").getValue(Boolean.class);

                connectedUser = new User(userId, userEmail, name, lastName, address, phoneNumber, language, admin != null ? admin : false);
                Log.d("CONNECTED USER", connectedUser.toString());

                setChanged();
                notifyObservers();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("USER REQUEST ERROR", databaseError.getMessage());
            }
        });
    }

    public User getConnectedUser() {
        return connectedUser;
    }

    public void writeNewUser(String userId, String firstName, String lastName, String mail, String phoneNumber, String language, String address, String postalCode, String city) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        usersRef.child("firstName").setValue(firstName);
        usersRef.child("lastName").setValue(lastName);
        usersRef.child("mail").setValue(mail);
        usersRef.child("phoneNumber").setValue(phoneNumber);
        usersRef.child("language").setValue(language);
        if(address != null)
            usersRef.child("address").setValue(address);
        if(postalCode != null)
            usersRef.child("postalCode").setValue(postalCode);
        if(city != null)
            usersRef.child("city").setValue(city);

        usersRef.child("admin").setValue(false);
    }

    public void updateUser(String userId, String firstName, String lastName, String email, String phoneNumber, String language, String address, String postalCode, String city, String password) {
        if (firebaseUser == null)
            return;

        writeNewUser(userId, firstName, lastName, email, phoneNumber, language, address, postalCode, city);

        AuthCredential credential = EmailAuthProvider
                .getCredential(Objects.requireNonNull(firebaseUser.getEmail()), password);

        firebaseUser.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        firebaseUser.updateEmail(email)
                                .addOnCompleteListener(task2 -> {
                                    if (task2.isSuccessful()) {
                                        requestUserData(firebaseUser);
                                    } else {
                                        Exception exception = task2.getException();
                                        if (exception != null)
                                            Log.e("UPDATE EMAIL ERROR", "", exception);
                                    }
                                });
                    } else {
                        Log.d("RE-AUTHENTICATION ERROR", "", task.getException());
                    }
                });
    }
}
