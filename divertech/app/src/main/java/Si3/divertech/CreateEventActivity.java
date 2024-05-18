package Si3.divertech;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.util.Objects;
import java.util.regex.Pattern;

import Si3.divertech.databinding.ActivityAdminNewEventBinding;
import Si3.divertech.events.Event;
import Si3.divertech.events.EventList;

public class CreateEventActivity extends AppCompatActivity {
    private ActivityAdminNewEventBinding binding;
    private boolean error = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminNewEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.more.setOnClickListener(click -> {
            if (binding.descriptionTextInput.getVisibility() == View.VISIBLE) {
                binding.descriptionTextInput.setVisibility(View.GONE);
                binding.more.setImageResource(R.drawable.add);
            } else {
                findViewById(R.id.description_text_input).setVisibility(View.VISIBLE);
                binding.more.setImageResource(R.drawable.close2);
            }
        });

        binding.buttonCancel.setOnClickListener(click -> finish());

        binding.date.setOnClickListener(click -> {
            DatePickerFragment dateFragment = new DatePickerFragment(getSupportFragmentManager(), true, getWindow().getDecorView().getRootView());
            dateFragment.show(getSupportFragmentManager(), "datePicker");
        });

        ActivityResultLauncher<Intent> startCrop = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            //TODO
                        }
                    }
                }
        );

        binding.cardImageEvent.setOnClickListener(click -> {
            Intent intent = new Intent(getApplicationContext(), ImageCropperActivity.class);
            startCrop.launch(intent);
        });

        addTextWatcher();
        String eventId = getIntent().getStringExtra(getString(R.string.event_id));
        if (EventList.getInstance().containsEvent(eventId)) {
            if (!EventList.getInstance().getEvent(eventId).getPictureUrl().isEmpty())
                Picasso.get().load(EventList.getInstance().getEvent(eventId).getPictureUrl()).into(binding.imageEvent);

            EditText shortDescription = findViewById(R.id.short_description);
            shortDescription.setText(EventList.getInstance().getEvent(eventId).getShortDescription());
            EditText title = findViewById(R.id.title);
            title.setText(EventList.getInstance().getEvent(eventId).getTitle());
            //EditText localisation = findViewById(R.id.localisation);
            //localisation.setText(EventList.getInstance().getEvent(eventId).getPosition());
            TextView date2 = findViewById(R.id.add_calendar);
            date2.setText(EventList.getInstance().getEvent(eventId).getDate());
            EditText description = findViewById(R.id.description);
            description.setText(EventList.getInstance().getEvent(eventId).getDescription());
            View validate = findViewById(R.id.button_validate);
            validate.setOnClickListener(click -> modification(EventList.getInstance().getEvent(eventId)));
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
        //EventList.modificateEvent(newEvent);
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
        // Event newEvent = new Event(EventList.generateId(),
        //         title.getText().toString(),
        //         R.drawable.image_default,
        //         shortDescription.getText().toString(),
        //         place.getText().toString(),
        //         description.getText().toString());
        // EventList.addEvent(newEvent);
        // EventList.addUserEvent(newEvent.id);
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