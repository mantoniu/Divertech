package Si3.divertech;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class EventActivity extends AppCompatActivity {


    public static final int REPORTING = 0;
    public static final int CONTACT = 1;
    public static final int OBJET = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Event event = getIntent().getParcelableExtra("event");
        Log.d("test", event.getTitle());

        TextView titre = findViewById(R.id.nameEvent);
        titre.setText(event.getTitle());

        TextView place = findViewById(R.id.localisation);
        place.setText("Lieu : "+event.getPlace());

        TextView description = findViewById(R.id.descriptionEvent);
        description.setText(event.getDescription());
        Log.d("test",event.getDescription());

    }
}