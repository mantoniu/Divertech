package Si3.divertech;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class SignalementIncident extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signalement_incident);
        View b = findViewById(R.id.return_arrow);
        b.setOnClickListener(click-> finish());
    }

}
