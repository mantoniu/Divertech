package Si3.divertech;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.CompletableFuture;

public class User {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String postalCode;
    private String city;
    private String phoneNumber;
    private String language;
    private boolean isAdmin;
    private DatabaseReference userRef;

    public User() {
    }

    public User(String userId, String firstName, String lastName, String mail, String phoneNumber, String language, String address,String postalCode, String city,boolean isAdmin) {
        this.id = userId;
        this.email = mail;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.language = language;
        this.isAdmin = isAdmin;
        this.userRef= FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getLanguage() {
        return language;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", city='" + city + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", language='" + language + '\'' +
                ", admin='" + isAdmin + '\'' +
                '}';
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        userRef.child("email").setValue(email);
        this.email = email;
    }

    public void setFirstName(String firstName) {
        userRef.child("firstName").setValue(firstName);
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        userRef.child("lastName").setValue(lastName);
        this.lastName = lastName;
    }

    public void setAddress(String address) {
        userRef.child("address").setValue(address);
        this.address = address;
    }

    public void setPostalCode(String postalCode) {
        userRef.child("postalCode").setValue(postalCode);
        this.postalCode = postalCode;
    }

    public void setCity(String city) {
        userRef.child("city").setValue(city);
        this.city = city;
    }

    public void setPhoneNumber(String phoneNumber) {
        userRef.child("phoneNumber").setValue(phoneNumber);
        this.phoneNumber = phoneNumber;
    }

    public void setLanguage(String language) {
        userRef.child("language").setValue(language);
        this.language = language;
    }

    public CompletableFuture<Boolean> updateUserEmail(String newEmail, String password) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
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
        if (user != null) {
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
}
