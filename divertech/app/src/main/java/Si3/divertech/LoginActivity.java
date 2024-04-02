package Si3.divertech;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ImageView exit = (ImageView)findViewById(R.id.exit);
        exit.setOnClickListener(click -> finish());

        TextView register = (TextView)findViewById(R.id.register);
        register.setOnClickListener(click -> {
            Intent i = new Intent(this, RegisterActivity.class);
            startActivity(i);
        });

        Button login = (Button)findViewById(R.id.login);
        login.setOnClickListener(click -> {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        });
    }

    
}