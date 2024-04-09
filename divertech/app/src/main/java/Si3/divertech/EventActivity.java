package Si3.divertech;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;



public class EventActivity extends AppCompatActivity {

    public static final int REPORTING = 0;
    public static final int CONTACT = 1;
    public static final int OBJET = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Intent changement = new Intent(getApplicationContext(), MultiPagesActivity.class);
        View signalement = findViewById(R.id.bloc_reporting);
        signalement.setOnClickListener(click-> {
            changement.putExtra("type", REPORTING);
            startActivity(changement);
        });
        View contact = findViewById(R.id.bloc_contact);
        contact.setOnClickListener(click-> {
            changement.putExtra("type", CONTACT);
            startActivity(changement);
        });
        View objets = findViewById(R.id.bloc_lost_object);
        objets.setOnClickListener(click-> {
            changement.putExtra("type", OBJET);
            startActivity(changement);
        });

        View b = findViewById(R.id.return_arrow);
        b.setOnClickListener(click-> finish());


        ConstraintLayout parkingLayout = findViewById(R.id.parking);
        parkingLayout.setOnClickListener(v -> {
            Intent intent = new Intent(this, ParkingActivity.class);
            startActivity(intent);
        });
    }
}