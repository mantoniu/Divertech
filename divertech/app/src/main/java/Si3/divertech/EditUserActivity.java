package Si3.divertech;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Objects;

public class EditUserActivity extends AppCompatActivity implements DataBaseListener {
    private static final int REQUEST_CODE_IMAGE_CROPPER = 123;
    private String newImageURL = "";
    private boolean waitingUpload = false;
    private TextInputEditText password;
    private View popupView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_user);

        User user = UserData.getConnectedUser();

        if (user == null)
            return;

        Spinner spinner = findViewById(R.id.language);
        LangAdapter adapter = new LangAdapter(this);
        spinner.setAdapter(adapter);

        TextInputEditText username = findViewById(R.id.email);
        TextInputEditText address = findViewById(R.id.address);
        TextInputEditText name = findViewById(R.id.firstName);
        TextInputEditText lastName = findViewById(R.id.name);
        TextInputEditText phoneNumber = findViewById(R.id.phone);
        ImageView profilePicture = findViewById(R.id.profile_picture);

        findViewById(R.id.return_arrow).setOnClickListener(click -> finish());


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

        profilePicture.setOnClickListener(click -> {
            Intent intent = new Intent(getApplicationContext(), ImageCropperActivity.class);
            startCrop.launch(intent);
        });

        if (!user.getPictureUrl().isEmpty())
            Picasso.get().load(user.getPictureUrl()).into(profilePicture);

        username.setText(user.getEmail());
        address.setText(user.getAddress());
        name.setText(user.getName());
        lastName.setText(user.getLastName());
        phoneNumber.setText(user.getPhoneNumber());

        findViewById(R.id.save_modifications).setOnClickListener(click -> {
            if (waitingUpload) {
                Toast.makeText(getApplicationContext(), "Upload de l'image en cours ...", Toast.LENGTH_LONG)
                        .show();
                return;
            }

            if (!(checkInput(username, findViewById(R.id.username_container), "Email requis")
                    && checkInput(lastName, findViewById(R.id.name_container), "Nom requis")
                    && checkInput(name, findViewById(R.id.firstName_container), "Prénom requis")
                    && checkInput(phoneNumber, findViewById(R.id.phone_container), "Numéro de téléphone requis"))) {
                return;
            }

            createPopup(name.getText().toString(), lastName.getText().toString(), address.getText().toString(), phoneNumber.getText().toString(), spinner.getSelectedItem().toString(), username.getText().toString());
        });
    }

    private boolean checkInput(TextInputEditText editText, TextInputLayout container, String error) {
        if (editText.getText() != null && editText.getText().toString().isEmpty()) {
            container.setError(error);
            editText.requestFocus();
            return false;
        }
        return true;
    }

    private void createPopup(String name, String lastName, String address, String phoneNumber, String language, String email) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.password_request_popup, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        PopupWindow popup = new PopupWindow(popupView, width, height, true);

        popup.setAnimationStyle(R.style.Animation);

        password = popupView.findViewById(R.id.password);
        if (password == null) {
            Log.d("NULL", "");
        }

        popupView.findViewById(R.id.confirm_button).setOnClickListener((click) -> {
            if (Objects.requireNonNull(password.getText()).toString().isEmpty()) {
                ((TextInputLayout) popupView.findViewById(R.id.password_container)).setError("Le mot de passe est requis");
                return;
            }
            //TODO picture url
            UserData.updateUser(name, lastName, address, phoneNumber, language, email, password.getText().toString(), newImageURL, this);
        });

        popupView.findViewById(R.id.close_button).setOnClickListener((click) -> popup.dismiss());


        popup.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    @Override
    public void onDataBaseResponse(DataBaseResponses response) {
        if (password == null)
            return;

        switch (response) {
            case SUCCESS:
                finish();
                break;
            case BAD_PASSWORD:
                ((TextInputLayout) popupView.findViewById(R.id.password_container)).setError("Le mot de passe saisi est incorrect");
                password.requestFocus();
                break;
            default:
        }
    }

    public void showErrorMessage(ProgressBar progressBar) {
        Toast.makeText(getApplicationContext(), "Erreur lors du chargement de l'image", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.INVISIBLE);
        waitingUpload = false;
    }

    public void uploadImage(String url) {
        if (url == null)
            return;

        ProgressBar progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        waitingUpload = true;

        try {
            InputStream inputStream = getContentResolver().openInputStream(Uri.parse(url));

            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] data = stream.toByteArray();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference filePath = storage.getReference().child("/users/" + UserData.getUserId() + ".jpg");
            UploadTask uploadTask = filePath.putBytes(data);

            uploadTask.addOnSuccessListener(taskSnapshot -> {
                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(uri -> {
                    newImageURL = uri.toString();
                    Log.d("NEWIMAGEURL", newImageURL);
                    Picasso.get().load(newImageURL).into((ImageView) findViewById(R.id.profile_picture));
                    progressBar.setVisibility(View.INVISIBLE);
                    waitingUpload = false;
                }).addOnFailureListener(exception -> showErrorMessage(progressBar));
            }).addOnFailureListener(exception -> {
                showErrorMessage(progressBar);
            });

            inputStream.close();
        } catch (Exception e) {
            showErrorMessage(progressBar);
        }
    }
}