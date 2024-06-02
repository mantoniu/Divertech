package Si3.divertech.notificationservice;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import Si3.divertech.users.UserData;

/**
 * This class is used to store the FCM token of the user in the database
 * It is a singleton class
 */
public class DBToken {

    private static DBToken instance;
    private static String token;

    private DBToken() {
    }

    /**
     * Set the token of the user in the database
     * @param token the token to set
     */
    public void setTokenToDb(String token) {
        if(UserData.getInstance().getUserId() == null)
            return;
        DBToken.token = token;
        FirebaseDatabase.getInstance().getReference().child("Users").child(UserData.getInstance().getUserId()).child("FCMtoken").setValue(token);

    }

    /**
     * Get the instance of the class
     * @return the instance of the class
     */
    public static DBToken getInstance() {
        if (instance == null)
            instance = new DBToken();
        return instance;
    }

    /**
     * Get the token of the user
     * @return the token of the user
     */
    public String getToken() {
        return token;
    }

}
