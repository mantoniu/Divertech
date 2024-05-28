package Si3.divertech;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Observable;
import java.util.Observer;

import Si3.divertech.users.UserData;

@SuppressLint("CustomSplashScreen")
public class LaunchActivity extends AppCompatActivity implements Observer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lauch);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        UserData.getInstance().addObserver(this);
        UserData.getInstance().requestUserData(FirebaseAuth.getInstance().getCurrentUser());
    }

    @Override
    public void update(Observable o, Object arg) {
        if (UserData.getInstance().getConnectedUser() == null)
            return;

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        UserData.getInstance().deleteObserver(this);
        finish();
    }
}