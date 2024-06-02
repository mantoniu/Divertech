package Si3.divertech.notificationservice;

import android.content.Context;
import android.util.Log;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import Si3.divertech.R;

/**
 * OAuth class to get the access token from the service account credentials
 * The service account credentials are stored in the resources folder
 * The credentials are used to authenticate the app to the Firebase Cloud Messaging service
 * /!\ The credentials are stored in a JSON file and should not be shared
 */
public class OAuth {
    public static String getAccessToken(Context context) throws IOException {
        //not safe at all
        //should be stored in a secure place but for the sake of the project it is stored in the resources folder
        InputStream inputStream = context.getResources().openRawResource(R.raw.adminsdk);
        GoogleCredentials credentials = GoogleCredentials
                .fromStream(inputStream)
                .createScoped("https://www.googleapis.com/auth/firebase.messaging");

        credentials.refresh();
        return credentials.getAccessToken().getTokenValue();
    }
}
