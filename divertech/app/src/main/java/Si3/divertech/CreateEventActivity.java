package Si3.divertech;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class CreateEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_event);

        ImageView back = findViewById(R.id.return_arrow);
        back.setOnClickListener(click -> finish());
        View cancel = findViewById(R.id.bloc_cancel);
        cancel.setOnClickListener(click -> finish());

        Event event = getIntent().getParcelableExtra("event");
        if (event != null) {
            EditText shortDescription = findViewById(R.id.short_description);
            shortDescription.setText(event.getShortDescription());
            EditText title = findViewById(R.id.nameEvent);
            title.setText(event.getTitle());
            EditText localisation = findViewById(R.id.localisation);
            localisation.setText(event.getPosition());
            EditText date = findViewById(R.id.date);
            date.setText(event.getDate());
            EditText description = findViewById(R.id.description_event);
            description.setText(event.getDescription());
            View validate = findViewById(R.id.check_bloc);
            validate.setOnClickListener(click -> modification(event));
        } else {
            View validate = findViewById(R.id.check_bloc);
            validate.setOnClickListener(click -> addNew());
        }
    }

    public void modification(Event event) {
        EditText title = findViewById(R.id.nameEvent);
        EditText shortDescription = findViewById(R.id.short_description);
        EditText place = findViewById(R.id.localisation);
        EditText description = findViewById(R.id.description_event);
        //Event newEvent = new Event(event.getId(),
        //        title.getText().toString(),
        //        R.drawable.image_default,
        //        shortDescription.getText().toString(),
        //        place.getText().toString(),
        //        description.getText().toString());
        //ListEvent.modificateEvent(newEvent);
        //TODO ajouter gestion base de données
        finish();
    }

    public void addNew() {
        EditText title = findViewById(R.id.nameEvent);
        EditText shortDescription = findViewById(R.id.short_description);
        EditText place = findViewById(R.id.localisation);
        EditText description = findViewById(R.id.description_event);
        // Event newEvent = new Event(ListEvent.generateId(),
        //         title.getText().toString(),
        //         R.drawable.image_default,
        //         shortDescription.getText().toString(),
        //         place.getText().toString(),
        //         description.getText().toString());
        // ListEvent.addEvent(newEvent);
        // ListEvent.addUserEvent(newEvent.id);
        //TODO ajouter gestion base de données
        finish();
    }

}