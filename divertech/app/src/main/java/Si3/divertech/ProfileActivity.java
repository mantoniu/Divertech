package Si3.divertech;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Observable;
import java.util.Observer;

import Si3.divertech.users.UserData;

public class ProfileActivity extends AppCompatActivity implements Observer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setValues();
        UserData.getInstance().addObserver(this);

        findViewById(R.id.modify_button).setOnClickListener(click -> {
            Intent updateIntent = new Intent(getApplicationContext(), RegisterActivity.class);
            updateIntent.putExtra("update", "");
            startActivity(updateIntent);
            finish();
        });

        findViewById(R.id.return_arrow).setOnClickListener(click -> finish());
    }

    @Override
    public void update(Observable o, Object arg) {
        setValues();
    }

    private void setValues() {
        ((TextView) findViewById(R.id.user_names)).setText(String.format("%s %s", UserData.getInstance().getConnectedUser().getName(), UserData.getInstance().getConnectedUser().getLastName()));
        ((TextView) findViewById(R.id.address)).setText(UserData.getInstance().getConnectedUser().getAddress());
        ((TextView) findViewById(R.id.phone_number)).setText(UserData.getInstance().getConnectedUser().getPhoneNumber());
        ((TextView) findViewById(R.id.email)).setText(UserData.getInstance().getConnectedUser().getEmail());
        ((TextView) findViewById(R.id.language)).setText(UserData.getInstance().getConnectedUser().getLanguage());
    }
}