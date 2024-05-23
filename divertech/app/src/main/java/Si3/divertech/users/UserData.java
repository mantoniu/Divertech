package Si3.divertech.users;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Observable;
import java.util.concurrent.CompletableFuture;

public class UserData extends Observable {
    private static UserData instance;
    private static User connectedUser;
    private final DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
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

        UserData.userId = userId;

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(userId);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                connectedUser = getUserBySnapshot(dataSnapshot, userId);
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

    public static User getUserBySnapshot(DataSnapshot dataSnapshot, String userId) {
        String userEmail = dataSnapshot.child("email").getValue(String.class);
        String firstName = dataSnapshot.child("firstName").getValue(String.class);
        String lastName = dataSnapshot.child("lastName").getValue(String.class);
        String address = dataSnapshot.child("address").getValue(String.class);
        String phoneNumber = dataSnapshot.child("phoneNumber").getValue(String.class);
        String language = dataSnapshot.child("language").getValue(String.class);
        String postalCode = dataSnapshot.child("postalCode").getValue(String.class);
        String city = dataSnapshot.child("city").getValue(String.class);
        String pictureUrl = dataSnapshot.child("pictureUrl").getValue(String.class);
        Boolean admin = dataSnapshot.child("admin").getValue(Boolean.class);

        return new User(userId, firstName, lastName, userEmail, phoneNumber, language, address, postalCode, city, pictureUrl != null ? pictureUrl : "", admin != null ? admin : false);
    }

    public void writeNewUser(String userId, String firstName, String lastName, String mail, String phoneNumber, String language, String address, String postalCode, String city) {
        DatabaseReference userRef = usersRef.child(userId);

        userRef.child("firstName").setValue(firstName);
        userRef.child("lastName").setValue(lastName);
        userRef.child("mail").setValue(mail);
        userRef.child("phoneNumber").setValue(phoneNumber);
        userRef.child("language").setValue(language);
        if(address != null)
            userRef.child("address").setValue(address);
        if(postalCode != null)
            userRef.child("postalCode").setValue(postalCode);
        if(city != null)
            userRef.child("city").setValue(city);

        userRef.child("admin").setValue(false);
    }

    private void setUserField(String path, String value) {
        usersRef.child(userId).child(path).setValue(value);
    }

    public void setPictureUrl(String pictureUrl) {
        setUserField("pictureUrl", pictureUrl);
    }

    public void setUserEmail(String email) {
        setUserField("email", email);
    }

    public void setLastName(String lastName) {
        setUserField("lastName", lastName);
    }

    public void setFirstName(String firstName) {
        setUserField("firstName", firstName);
    }

    public void setPhoneNumber(String phoneNumber) {
        setUserField("phoneNumber", phoneNumber);
    }

    public void setCity(String city) {
        setUserField("city", city);
    }

    public void setLanguage(String language) {
        setUserField("language", language);
    }

    public void setAddress(String address) {
        setUserField("address", address);
    }

    public void setPostalCode(String postalCode) {
        setUserField("postalCode", postalCode);
    }

    public CompletableFuture<Boolean> updateUserEmail(String newEmail, String password) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && user.getEmail() != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);
            user.reauthenticate(credential)
                    .addOnCompleteListener(reAuthTask -> {
                        if (reAuthTask.isSuccessful()) {
                            user.updateEmail(newEmail)
                                    .addOnCompleteListener(updateTask -> {
                                        if (updateTask.isSuccessful()) {
                                            future.complete(true);
                                        } else {
                                            future.complete(false);
                                        }
                                    });
                        } else {
                            future.complete(false);
                        }
                    });
        } else {
            future.complete(false);
        }
        return future;
    }

    public CompletableFuture<Boolean> changeUserPassword(String newPassword, String currentPassword) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && user.getEmail() != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
            user.reauthenticate(credential)
                    .addOnCompleteListener(reAuthTask -> {
                        if (reAuthTask.isSuccessful()) {
                            user.updatePassword(newPassword)
                                    .addOnCompleteListener(updateTask -> {
                                        if (updateTask.isSuccessful()) {
                                            future.complete(true);
                                        } else {
                                            future.complete(false);
                                        }
                                    });
                        } else {
                            future.complete(false);
                        }
                    });
        } else {
            future.complete(false);
        }
        return future;
    }

    public void disconnect() {
        FirebaseAuth.getInstance().signOut();
    }
}
