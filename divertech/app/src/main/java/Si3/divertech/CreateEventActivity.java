package Si3.divertech;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;
import java.util.regex.Pattern;

public class CreateEventActivity extends AppCompatActivity {

    private boolean error = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_event);

        View buttonAdd = findViewById(R.id.more);
        buttonAdd.setOnClickListener(click -> {
            if (findViewById(R.id.description_text_input).getVisibility() == View.VISIBLE) {
                findViewById(R.id.description_text_input).setVisibility(View.GONE);
                ((ImageView) buttonAdd).setImageResource(R.drawable.add);
            } else {
                findViewById(R.id.description_text_input).setVisibility(View.VISIBLE);
                ((ImageView) buttonAdd).setImageResource(R.drawable.close2);
            }
        });

        View cancel = findViewById(R.id.button_cancel);
        cancel.setOnClickListener(click -> finish());

        MaterialCardView date = findViewById(R.id.card_date);
        date.setOnClickListener(click -> {
            DatePickerFragment dateFragment = new DatePickerFragment(getSupportFragmentManager(), true, getWindow().getDecorView().getRootView());
            dateFragment.show(getSupportFragmentManager(), "datePicker");
        });

        addTextWatcher();
        String eventId = getIntent().getStringExtra("eventId");
        Event event = ListEvent.getEventMap().get(eventId);
        if (event != null) {
            EditText shortDescription = findViewById(R.id.short_description);
            shortDescription.setText(event.getShortDescription());
            EditText title = findViewById(R.id.title);
            title.setText(event.getTitle());
            //EditText localisation = findViewById(R.id.localisation);
            //localisation.setText(event.getPosition());
            TextView date2 = findViewById(R.id.add_calendar);
            date2.setText(event.getDate());
            EditText description = findViewById(R.id.description);
            description.setText(event.getDescription());
            View validate = findViewById(R.id.button_validate);
            validate.setOnClickListener(click -> modification(event));
        } else {
            View validate = findViewById(R.id.button_validate);
            validate.setOnClickListener(click -> addNew());
        }
    }

    public void modification(Event event) {
        //EditText title = findViewById(R.id.title);
        //EditText shortDescription = findViewById(R.id.short_description);
        //EditText place = findViewById(R.id.localisation);
        //EditText description = findViewById(R.id.description);
        //Event newEvent = new Event(event.getId(),
        //        title.getText().toString(),
        //        R.drawable.image_default,
        //        shortDescription.getText().toString(),
        //        place.getText().toString(),
        //        description.getText().toString());
        //ListEvent.modificateEvent(newEvent);
        //TODO ajouter gestion base de données
        testError();
        if (!error) {
            finish();
        }
    }

    public void addNew() {
        //EditText title = findViewById(R.id.title);
        //EditText shortDescription = findViewById(R.id.short_description);
        //EditText place = findViewById(R.id.localisation);
        //EditText description = findViewById(R.id.description);
        // Event newEvent = new Event(ListEvent.generateId(),
        //         title.getText().toString(),
        //         R.drawable.image_default,
        //         shortDescription.getText().toString(),
        //         place.getText().toString(),
        //         description.getText().toString());
        // ListEvent.addEvent(newEvent);
        // ListEvent.addUserEvent(newEvent.id);
        //TODO ajouter gestion base de données
        testError();
        if (!error) {
            finish();
        }
    }

    public void testError() {
        boolean test = false;
        TextInputLayout typeMessageLayout = findViewById(R.id.title_text_input);
        if (Objects.requireNonNull(((TextInputEditText) findViewById(R.id.title)).getText()).toString().isEmpty()) {
            typeMessageLayout.setErrorEnabled(true);
            typeMessageLayout.setError(getString(R.string.title_required));
            findViewById(R.id.title).requestFocus();
            test = true;
        }
        typeMessageLayout = findViewById(R.id.short_description_text_input);
        if (Objects.requireNonNull(((TextInputEditText) findViewById(R.id.short_description)).getText()).toString().isEmpty()) {
            typeMessageLayout.setErrorEnabled(true);
            typeMessageLayout.setError(getString(R.string.short_description_required));
            findViewById(R.id.short_description).requestFocus();
            test = true;
        }
        typeMessageLayout = findViewById(R.id.address_text_input);
        if (Objects.requireNonNull(((TextInputEditText) findViewById(R.id.address)).getText()).toString().isEmpty()) {
            typeMessageLayout.setErrorEnabled(true);
            typeMessageLayout.setError(getString(R.string.address_required));
            findViewById(R.id.address).requestFocus();
            test = true;
        }
        typeMessageLayout = findViewById(R.id.city_text_input);
        if (Objects.requireNonNull(((TextInputEditText) findViewById(R.id.city)).getText()).toString().isEmpty()) {
            typeMessageLayout.setErrorEnabled(true);
            typeMessageLayout.setError(getString(R.string.city_required));
            findViewById(R.id.city).requestFocus();
            test = true;
        }
        typeMessageLayout = findViewById(R.id.postal_code_text_input);
        if (Objects.requireNonNull(((TextInputEditText) findViewById(R.id.postal_code)).getText()).toString().isEmpty()) {
            typeMessageLayout.setErrorEnabled(true);
            typeMessageLayout.setError(getString(R.string.postal_code_required));
            findViewById(R.id.postal_code).requestFocus();
            test = true;
        }
        TextView calendar = findViewById(R.id.add_calendar);
        if (calendar.getText().toString().isEmpty() || calendar.getText().toString().contentEquals(getResources().getText(R.string.choose_date_required))) {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone((ConstraintLayout) findViewById(R.id.calendar_constraint_layout));
            constraintSet.connect(R.id.date, ConstraintSet.TOP, R.id.calendar_constraint_layout, ConstraintSet.TOP, 40);
            constraintSet.connect(R.id.date, ConstraintSet.BOTTOM, R.id.add_calendar, ConstraintSet.TOP);
            constraintSet.applyTo(findViewById(R.id.calendar_constraint_layout));
            calendar.setText(R.string.choose_date_required);
            calendar.setTextColor(getResources().getColor(R.color.red, getTheme()));
            calendar.setVisibility(View.VISIBLE);
            calendar.requestFocus();
            test = true;
        }
        error = test;
    }

    private void addTextWatcher() {
        TextInputEditText title = findViewById(R.id.title);
        title.addTextChangedListener(createTextWatcher(findViewById(R.id.title_text_input), getString(R.string.title_required)));
        TextInputEditText short_description = findViewById(R.id.short_description);
        short_description.addTextChangedListener(createTextWatcher(findViewById(R.id.short_description_text_input), getString(R.string.title_required)));
        TextInputEditText address = findViewById(R.id.address);
        address.addTextChangedListener(createTextWatcher(findViewById(R.id.address_text_input), getString(R.string.address_required)));
        TextInputEditText city = findViewById(R.id.city);
        city.addTextChangedListener(createTextWatcher(findViewById(R.id.city_text_input), getString(R.string.city_required)));


        TextInputEditText postalCode = findViewById(R.id.postal_code);
        postalCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Pattern p = Pattern.compile("[0-9][1-8]([0-9]{3}).");
                if (p.matcher(s.toString()).matches() || s.length() != 5) {
                    error = true;
                    ((TextInputLayout) findViewById(R.id.postal_code_text_input)).setErrorEnabled(true);
                    ((TextInputLayout) findViewById(R.id.postal_code_text_input)).setError(getString(R.string.postal_code_required));
                    findViewById(R.id.postal_code).requestFocus();
                } else {
                    error = false;
                    ((TextInputLayout) findViewById(R.id.postal_code_text_input)).setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    private TextWatcher createTextWatcher(TextInputLayout view, String string) {
        return new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    error = false;
                    view.setErrorEnabled(false);
                } else {
                    error = true;
                    view.setErrorEnabled(true);
                    view.setError(string);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }

}