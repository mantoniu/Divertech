package Si3.divertech;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.canhub.cropper.CropImageView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

import Si3.divertech.databinding.ActivityAdminNewEventBinding;
import Si3.divertech.events.EventList;
import Si3.divertech.utils.DateListener;
import Si3.divertech.utils.DatePickerFragment;
import Si3.divertech.utils.DateUtils;
import Si3.divertech.utils.UploadUtils;

public class CreateEventActivity extends AppCompatActivity implements DateListener {
    private ActivityAdminNewEventBinding binding;
    private String eventId;
    private String newPictureUrl;
    private ZonedDateTime date;
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

        binding.buttonCancel.setOnClickListener(click -> {
            if (newPictureUrl != null && EventList.getInstance().getEvent(eventId).getPictureUrl() != null && !EventList.getInstance().getEvent(eventId).getPictureUrl().equals(newPictureUrl)) {
                FirebaseStorage.getInstance().getReferenceFromUrl(newPictureUrl).delete();
            }
            finish();
        });

        binding.date.setOnClickListener(click -> {
            DatePickerFragment dateFragment = new DatePickerFragment(getSupportFragmentManager(), true, getWindow().getDecorView().getRootView(), this);
            dateFragment.show(getSupportFragmentManager(), "datePicker");
        });

        ActivityResultLauncher<Intent> startCrop = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            uploadImage(data.getStringExtra("croppedImageUri"));
                        }
                    }
                }
        );

        binding.cardImageEvent.setOnClickListener(click -> {
            Intent intent = new Intent(getApplicationContext(), ImageCropperActivity.class);
            intent.putExtra("aspectRatioX", 1300);
            intent.putExtra("aspectRatioY", 900);
            intent.putExtra("shape", CropImageView.CropShape.RECTANGLE.ordinal());
            startCrop.launch(intent);
        });

        addTextWatcher();
        eventId = getIntent().getStringExtra(getString(R.string.event_id));
        if (EventList.getInstance().containsEvent(eventId)) {
            if (EventList.getInstance().getEvent(eventId).getPictureUrl() != null && !EventList.getInstance().getEvent(eventId).getPictureUrl().isEmpty())
                Picasso.get().load(EventList.getInstance().getEvent(eventId).getPictureUrl()).into(binding.imageEvent);

            if (EventList.getInstance().getEvent(eventId).getZonedDate() != null) {
                date = EventList.getInstance().getEvent(eventId).getZonedDate();
                binding.addCalendar.setText(EventList.getInstance().getEvent(eventId).getFormattedDate());
                setDate();
            }

            binding.shortDescription.setText(EventList.getInstance().getEvent(eventId).getShortDescription());
            binding.title.setText(EventList.getInstance().getEvent(eventId).getTitle());
            binding.postalCode.setText(EventList.getInstance().getEvent(eventId).getPostalCode());
            binding.address.setText(EventList.getInstance().getEvent(eventId).getAddress());
            binding.city.setText(EventList.getInstance().getEvent(eventId).getCity());
            binding.description.setText(EventList.getInstance().getEvent(eventId).getDescription());
        }
        binding.buttonValidate.setOnClickListener(click -> writeEvent());
    }

    public void showErrorMessage() {
        Toast.makeText(getApplicationContext(), "Erreur lors du chargement de l'image", Toast.LENGTH_SHORT).show();
    }

    private void uploadImage(String url) {
        if (url == null)
            return;

        binding.uploadProgress.setVisibility(View.VISIBLE);

        OnSuccessListener<? super Uri> successListener = (OnSuccessListener<? super Uri>) uri -> {
            newPictureUrl = uri.toString();
            Picasso.get().load(newPictureUrl).into(binding.imageEvent);
            binding.uploadProgress.setVisibility(View.INVISIBLE);
        };

        OnFailureListener failureListener = (OnFailureListener) -> {
            showErrorMessage();
        };

        UploadUtils.uploadImage(url, "/events/" + UUID.randomUUID().toString() + ".jpg", 60, getApplicationContext(), successListener, failureListener);
    }

    public void writeEvent() {
        if (!testError())
            return;

        if (binding.title.getText() == null || binding.shortDescription.getText() == null || binding.address.getText() == null
                || binding.postalCode.getText() == null || binding.city.getText() == null || binding.description.getText() == null)
            return;


        String title = binding.title.getText().toString();
        String shortDescription = binding.shortDescription.getText().toString();
        String address = binding.address.getText().toString();
        String postalCode = binding.postalCode.getText().toString();
        String city = binding.city.getText().toString();
        String description = binding.description.getText().toString();

        EventList.getInstance().writeEvent(eventId, title, newPictureUrl, shortDescription, address, postalCode, city, description, date.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));
        finish();
    }

    public boolean testError() {
        TextInputLayout typeMessageLayout = findViewById(R.id.title_text_input);
        if (Objects.requireNonNull(((TextInputEditText) findViewById(R.id.title)).getText()).toString().isEmpty()) {
            typeMessageLayout.setErrorEnabled(true);
            typeMessageLayout.setError(getString(R.string.title_required));
            findViewById(R.id.title).requestFocus();
            return false;
        }
        typeMessageLayout = findViewById(R.id.short_description_text_input);
        if (Objects.requireNonNull(((TextInputEditText) findViewById(R.id.short_description)).getText()).toString().isEmpty()) {
            typeMessageLayout.setErrorEnabled(true);
            typeMessageLayout.setError(getString(R.string.short_description_required));
            findViewById(R.id.short_description).requestFocus();
            return false;
        }
        typeMessageLayout = findViewById(R.id.address_text_input);
        if (Objects.requireNonNull(((TextInputEditText) findViewById(R.id.address)).getText()).toString().isEmpty()) {
            typeMessageLayout.setErrorEnabled(true);
            typeMessageLayout.setError(getString(R.string.address_required));
            findViewById(R.id.address).requestFocus();
            return false;
        }
        typeMessageLayout = findViewById(R.id.city_text_input);
        if (Objects.requireNonNull(((TextInputEditText) findViewById(R.id.city)).getText()).toString().isEmpty()) {
            typeMessageLayout.setErrorEnabled(true);
            typeMessageLayout.setError(getString(R.string.city_required));
            findViewById(R.id.city).requestFocus();
            return false;
        }
        typeMessageLayout = findViewById(R.id.postal_code_text_input);
        if (Objects.requireNonNull(((TextInputEditText) findViewById(R.id.postal_code)).getText()).toString().isEmpty()) {
            typeMessageLayout.setErrorEnabled(true);
            typeMessageLayout.setError(getString(R.string.postal_code_required));
            findViewById(R.id.postal_code).requestFocus();
            return false;
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
            return false;
        }
        return true;
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

    @Override
    public void onDateChoose(int day, int month, int year, int hour, int minute) {
        date = ZonedDateTime.of(LocalDateTime.of(year, month, day, hour, minute), ZoneId.systemDefault());
        setDate();
    }

    private void setDate() {
        TextView textCalendar = findViewById(R.id.add_calendar);
        Log.d("antoniu => ", DateUtils.formatZonedDate(date));
        textCalendar.setText(DateUtils.formatZonedDate(date));
        findViewById(R.id.add_calendar).setVisibility(View.VISIBLE);
        textCalendar.setTextColor(getResources().getColor(R.color.black, getApplicationContext().getTheme()));
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone((ConstraintLayout) findViewById(R.id.calendar_constraint_layout));
        constraintSet.connect(R.id.date, ConstraintSet.TOP, R.id.calendar_constraint_layout, ConstraintSet.TOP, 40);
        constraintSet.connect(R.id.date, ConstraintSet.BOTTOM, R.id.add_calendar, ConstraintSet.TOP);
        constraintSet.applyTo(findViewById(R.id.calendar_constraint_layout));
    }
}