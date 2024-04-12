package Si3.divertech;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EventActivity extends AppCompatActivity {

    public static final int REPORTING = 0;
    public static final int CONTACT = 1;
    public static final int OBJET = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Event event = getIntent().getParcelableExtra("event");

        if(event==null) {
            finish();
            return;
        }

        Log.d("test", event.getTitle());

        TextView titre = findViewById(R.id.nameEvent);
        titre.setText(event.getTitle());

        TextView place = findViewById(R.id.localisation);
        place.setText(String.format("Lieu : %s", event.getPlace()));

        ImageView map = findViewById(R.id.logo_map);
        map.setOnClickListener(click->{
            Intent intent = new Intent(getApplicationContext(),MapActivity.class);
            intent.putExtra("pos",event.getId());
            startActivity(intent);
            finish();
        });

        TextView description = findViewById(R.id.description_event);
        description.setText(event.getDescription());
        Log.d("Event description",event.getDescription());

        Intent curIntent = getIntent();
        String qrData = curIntent.getStringExtra("qr_data");
        if (qrData != null) {
            Toast.makeText(getApplicationContext(), qrData, Toast.LENGTH_LONG).show();
        }

        Intent modification = new Intent(getApplicationContext(), MultiPagesActivity.class);
        View report = findViewById(R.id.bloc_reporting);
        report.setOnClickListener(click-> {
            modification.putExtra("type", REPORTING);
            startActivity(modification);
        });
        View contact = findViewById(R.id.bloc_contact);
        contact.setOnClickListener(click-> {
            modification.putExtra("type", CONTACT);
            startActivity(modification);
        });
        View objets = findViewById(R.id.bloc_lost_object);
        objets.setOnClickListener(click-> {
            modification.putExtra("type", OBJET);
            startActivity(modification);
        });

        View b = findViewById(R.id.return_arrow);
        b.setOnClickListener(click-> finish());


        ConstraintLayout parkingLayout = findViewById(R.id.parking);
        parkingLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ParkingActivity.class);
            startActivity(intent);
        });
    }
}