package Si3.divertech;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import Si3.divertech.parking.ParkingList;
import Si3.divertech.users.UserData;
import Si3.divertech.utils.DatePickerFragment;

public class ParkingActivity extends RequireUserActivity {

    private boolean error = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking);

        ImageView returnToEvent = findViewById(R.id.return_arrow);
        returnToEvent.setOnClickListener(v -> finish());

        Button parkingButton = findViewById(R.id.send_button);
        parkingButton.setOnClickListener(v -> {
            testError();
            if (!error) {
                TextInputEditText address = findViewById(R.id.adress);
                TextInputEditText phone = findViewById(R.id.phone_number);
                TextInputEditText licencePlate = findViewById(R.id.licence_plate);
                TextView date = findViewById(R.id.add_calendar);
                ParkingList.getInstance().addReservation(getIntent().getStringExtra("eventId"), Objects.requireNonNull(licencePlate.getText()).toString(), Objects.requireNonNull(phone.getText()).toString(), Objects.requireNonNull(address.getText()).toString(), date.getText().toString(), UserData.getInstance().getUserId());
                finish();
            }
        });

        MaterialCardView datePicker = findViewById(R.id.card_date);
        datePicker.setOnClickListener(click -> {
            DatePickerFragment newFragment = new DatePickerFragment(getSupportFragmentManager(), false, getWindow().getDecorView().getRootView());
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);
            newFragment.show(ft, "datePicker");
        });
        addTextWatcher();
    }


    public void testError() {
        boolean test = false;
        TextInputLayout typeMessageLayout = findViewById(R.id.adress_container);
        if (Objects.requireNonNull(((TextInputEditText) findViewById(R.id.adress)).getText()).toString().isEmpty()) {
            typeMessageLayout.setErrorEnabled(true);
            typeMessageLayout.setError(getString(R.string.address_required));
            findViewById(R.id.adress).requestFocus();
            test = true;
        }
        typeMessageLayout = findViewById(R.id.phone_number_container);
        if (Objects.requireNonNull(((TextInputEditText) findViewById(R.id.phone_number)).getText()).toString().isEmpty()) {
            typeMessageLayout.setErrorEnabled(true);
            typeMessageLayout.setError(getString(R.string.phone_number_required));
            findViewById(R.id.phone_number).requestFocus();
            test = true;
        }
        typeMessageLayout = findViewById(R.id.licence_plate_container);
        if (Objects.requireNonNull(((TextInputEditText) findViewById(R.id.licence_plate)).getText()).toString().isEmpty()) {
            typeMessageLayout.setErrorEnabled(true);
            typeMessageLayout.setError(getString(R.string.licence_plate_required));
            findViewById(R.id.licence_plate).requestFocus();
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
        TextInputEditText title = findViewById(R.id.adress);
        title.addTextChangedListener(createTextWatcher(findViewById(R.id.adress_container), getString(R.string.address_required)));
        TextInputEditText short_description = findViewById(R.id.phone_number);
        short_description.addTextChangedListener(createTextWatcher(findViewById(R.id.phone_number_container), getString(R.string.phone_number_required)));
        TextInputEditText address = findViewById(R.id.licence_plate);
        address.addTextChangedListener(createTextWatcher(findViewById(R.id.licence_plate_container), getString(R.string.licence_plate_required)));
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