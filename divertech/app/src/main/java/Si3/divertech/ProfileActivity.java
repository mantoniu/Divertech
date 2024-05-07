package Si3.divertech;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

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

        User user = UserData.getConnectedUser();

        ((TextView) findViewById(R.id.user_names)).setText(String.format("%s %s", user.getName(), user.getLastName()));
        ((TextView) findViewById(R.id.address)).setText(user.getAddress());
        ((TextView) findViewById(R.id.phone_number)).setText(user.getPhoneNumber());
        ((TextView) findViewById(R.id.email)).setText(user.getEmail());
        ((TextView) findViewById(R.id.language)).setText(user.getLanguage());

        if (!user.getPictureUrl().isEmpty())
            Picasso.get().load(user.getPictureUrl()).into((ImageView) findViewById(R.id.profile_image));

        findViewById(R.id.modify_button).setOnClickListener(click -> {
            Intent updateIntent = new Intent(getApplicationContext(), EditUserActivity.class);
            startActivity(updateIntent);
            finish();
        });

        findViewById(R.id.return_arrow).setOnClickListener(click -> finish());
    }
}