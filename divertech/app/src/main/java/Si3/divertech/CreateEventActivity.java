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
import Si3.divertech.users.UserData;
import Si3.divertech.utils.DateListener;
import Si3.divertech.utils.DatePickerFragment;
import Si3.divertech.utils.DateUtils;
import Si3.divertech.utils.UploadUtils;

public class CreateEventActivity extends RequireUserActivity implements DateListener, View.OnFocusChangeListener {
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
            if (newPictureUrl != null && (eventId == null || (EventList.getInstance().getEvent(eventId).getPictureUrl() != null && !EventList.getInstance().getEvent(eventId).getPictureUrl().equals(newPictureUrl)))) {
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
            binding.socialNetwork.setText(EventList.getInstance().getEvent(eventId).getInstagramURL());
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

        OnSuccessListener<? super Uri> successListener = uri -> {
            newPictureUrl = uri.toString();
            Picasso.get().load(newPictureUrl).into(binding.imageEvent);
            binding.uploadProgress.setVisibility(View.INVISIBLE);
            binding.error.setVisibility(View.GONE);
        };

        OnFailureListener failureListener = (OnFailureListener) -> showErrorMessage();
        UploadUtils.uploadImage(url, "/events/" + UUID.randomUUID().toString() + ".jpg", 60, getApplicationContext(), successListener, failureListener);
    }

    public void writeEvent() {
        testError();
        if (error)
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
        String organizerId = UserData.getInstance().getUserId();

        String instagramURL = "";
        if (binding.socialNetwork.getText()!= null) instagramURL = binding.socialNetwork.getText().toString();

        EventList.getInstance().writeEvent(eventId, title, newPictureUrl, shortDescription, address, postalCode, city, description, instagramURL, date.format(DateTimeFormatter.ISO_ZONED_DATE_TIME), organizerId);
        finish();
    }

    public void testError() {
        if (Objects.requireNonNull(((TextInputEditText) binding.title).getText()).toString().isEmpty()) {
            binding.titleTextInput.setErrorEnabled(true);
            binding.titleTextInput.setError(getString(R.string.title_required));
            binding.title.requestFocus();
            error = true;
        }
        if (Objects.requireNonNull(((TextInputEditText) binding.shortDescription).getText()).toString().isEmpty()) {
            binding.shortDescriptionTextInput.setErrorEnabled(true);
            binding.shortDescriptionTextInput.setError(getString(R.string.short_description_required));
            binding.shortDescription.requestFocus();
            error = true;
        }
        if (Objects.requireNonNull(((TextInputEditText) binding.address).getText()).toString().isEmpty()) {
            binding.addressTextInput.setErrorEnabled(true);
            binding.addressTextInput.setError(getString(R.string.address_required));
            binding.address.requestFocus();
            error = true;
        }
        if (Objects.requireNonNull(((TextInputEditText) binding.city).getText()).toString().isEmpty()) {
            binding.cityTextInput.setErrorEnabled(true);
            binding.cityTextInput.setError(getString(R.string.city_required));
            binding.city.requestFocus();
            error = true;
        }
        if (Objects.requireNonNull(((TextInputEditText) binding.postalCode).getText()).toString().isEmpty()) {
            binding.postalCodeTextInput.setErrorEnabled(true);
            binding.postalCodeTextInput.setError(getString(R.string.postal_code_required));
            binding.postalCode.requestFocus();
            error = true;
        }
        if (binding.imageEvent.getDrawable() == null) {
            binding.error.setVisibility(View.VISIBLE);
            error = true;
        }

        TextView calendar = binding.addCalendar;
        if (calendar.getText().toString().isEmpty() || calendar.getText().toString().contentEquals(getResources().getText(R.string.choose_date_required))) {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone((ConstraintLayout) binding.calendarConstraintLayout);
            constraintSet.connect(R.id.date, ConstraintSet.TOP, R.id.calendar_constraint_layout, ConstraintSet.TOP, 40);
            constraintSet.connect(R.id.date, ConstraintSet.BOTTOM, R.id.add_calendar, ConstraintSet.TOP);
            constraintSet.applyTo(findViewById(R.id.calendar_constraint_layout));
            calendar.setText(R.string.choose_date_required);
            calendar.setTextColor(getResources().getColor(R.color.red, getTheme()));
            calendar.setVisibility(View.VISIBLE);
            calendar.requestFocus();
            error = true;
        }
        if (!testAddress()) {
            error = true;
        }
    }

    private void addTextWatcher() {
        TextInputEditText title = binding.title;
        title.addTextChangedListener(createTextWatcher(binding.titleTextInput, getString(R.string.title_required)));
        TextInputEditText short_description = binding.shortDescription;
        short_description.addTextChangedListener(createTextWatcher(binding.shortDescriptionTextInput, getString(R.string.title_required)));
        TextInputEditText address = binding.address;
        address.addTextChangedListener(createTextWatcher(binding.addressTextInput, getString(R.string.address_required)));
        TextInputEditText city = binding.city;
        city.addTextChangedListener(createTextWatcher(binding.cityTextInput, getString(R.string.city_required)));


        TextInputEditText postalCode = binding.postalCode;
        postalCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.addressValidation.setVisibility(View.GONE);
                Pattern p = Pattern.compile("[0-9][1-8]([0-9]{3}).");
                if (p.matcher(s.toString()).matches() || s.length() != 5) {
                    error = true;
                    ((TextInputLayout) binding.postalCodeTextInput).setErrorEnabled(true);
                    ((TextInputLayout) binding.postalCodeTextInput).setError(getString(R.string.postal_code_required));
                    binding.postalCode.requestFocus();
                } else {
                    error = false;
                    ((TextInputLayout) binding.postalCodeTextInput).setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        binding.socialNetwork.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    if (!Objects.requireNonNull(binding.socialNetwork.getText()).toString().matches("(instagram.com/)\\S*")) {
                        binding.socialMediaTextInput.setErrorEnabled(true);
                        binding.socialMediaTextInput.setError(getString(R.string.url_not_good));
                        binding.socialMediaTextInput.requestFocus();
                        error = true;
                    } else {
                        binding.socialMediaTextInput.setErrorEnabled(false);
                        error = false;
                    }
                }
                if (s.toString().isEmpty()) {
                    binding.socialMediaTextInput.setErrorEnabled(false);
                    error = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        postalCode.setOnFocusChangeListener(this);
        address.setOnFocusChangeListener(this);
        city.setOnFocusChangeListener(this);
    }


    private TextWatcher createTextWatcher(TextInputLayout view, String string) {
        return new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (view == binding.addressTextInput || view == binding.cityTextInput)
                    binding.addressValidation.setVisibility(View.GONE);
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
        TextView textCalendar = binding.addCalendar;
        Log.d("antoniu => ", DateUtils.formatZonedDate(date));
        textCalendar.setText(DateUtils.formatZonedDate(date));
        binding.addCalendar.setVisibility(View.VISIBLE);
        textCalendar.setTextColor(getResources().getColor(R.color.black, getApplicationContext().getTheme()));
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone((ConstraintLayout) binding.calendarConstraintLayout);
        constraintSet.connect(R.id.date, ConstraintSet.TOP, R.id.calendar_constraint_layout, ConstraintSet.TOP, 40);
        constraintSet.connect(R.id.date, ConstraintSet.BOTTOM, R.id.add_calendar, ConstraintSet.TOP);
        constraintSet.applyTo(binding.calendarConstraintLayout);
    }

    private boolean testAddress() {
        String cityText = Objects.requireNonNull(((TextInputLayout) binding.cityTextInput).getEditText()).getText().toString();
        String addressText = Objects.requireNonNull(((TextInputLayout) binding.addressTextInput).getEditText()).getText().toString();
        String postalCode = Objects.requireNonNull(((TextInputLayout) binding.postalCodeTextInput).getEditText()).getText().toString();
        if (!cityText.isEmpty() && !addressText.isEmpty() && !postalCode.isEmpty()) {
            if (MapActivity.getAddress(cityText + ", " + addressText + ", " + postalCode, getApplicationContext()) == null) {
                ((TextView) binding.addressValidation).setText(R.string.address_false);
                binding.addressValidation.setVisibility(View.VISIBLE);
                ((TextView) binding.addressValidation).setTextColor(getColor(R.color.red));
                binding.postalCodeTextInput.setErrorEnabled(true);
                binding.postalCodeTextInput.setError(" ");
                binding.cityTextInput.setErrorEnabled(true);
                binding.cityTextInput.setError(" ");
                binding.addressTextInput.setErrorEnabled(true);
                binding.addressTextInput.setError(" ");
                return false;
            } else {
                ((TextView) binding.addressValidation).setText(R.string.address_good);
                binding.addressValidation.setVisibility(View.VISIBLE);
                ((TextView) binding.addressValidation).setTextColor(getColor(R.color.green));
                binding.postalCodeTextInput.setErrorEnabled(false);
                binding.cityTextInput.setErrorEnabled(false);
                binding.addressTextInput.setErrorEnabled(false);
                return true;
            }
        }
        return false;
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            testAddress();
        }
    }
}