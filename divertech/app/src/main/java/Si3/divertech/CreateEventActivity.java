package Si3.divertech;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class CreateEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_event);

        ImageView back = findViewById(R.id.return_arrow);
        back.setOnClickListener(click -> finish());
        View cancel = findViewById(R.id.bloc_cancel);
        cancel.setOnClickListener(click -> finish());

        TextView date = findViewById(R.id.date);
        String eventId = getIntent().getStringExtra(getString(R.string.event_id));
        if (ListEvent.getInstance().getEvent(eventId) != null) {
            EditText shortDescription = findViewById(R.id.short_description);
            shortDescription.setText(ListEvent.getInstance().getEvent(eventId).getShortDescription());
            EditText title = findViewById(R.id.nameEvent);
            title.setText(ListEvent.getInstance().getEvent(eventId).getTitle());
            EditText localisation = findViewById(R.id.localisation);
            localisation.setText(ListEvent.getInstance().getEvent(eventId).getPosition());

            date.setText(ListEvent.getInstance().getEvent(eventId).getDate());
            EditText description = findViewById(R.id.description_event);
            description.setText(ListEvent.getInstance().getEvent(eventId).getDescription());
            View validate = findViewById(R.id.check_bloc);
            validate.setOnClickListener(click -> modification(ListEvent.getInstance().getEvent(eventId)));
        } else {
            View validate = findViewById(R.id.check_bloc);
            validate.setOnClickListener(click -> addNew());
        }
        date.setOnClickListener(click -> {
            DatePickerFragment newFragment = new DatePickerFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);
            newFragment.show(ft, "datePicker");
        });

    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker.
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it.
            return new DatePickerDialog(requireContext(), this, year, month, day);
            //return new DatePickerDialog(requireContext(),R.style.ThemeOverlay_App_DatePicker, this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date the user picks.
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