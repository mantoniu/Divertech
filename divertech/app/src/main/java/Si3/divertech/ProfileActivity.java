package Si3.divertech;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.canhub.cropper.CropImageView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import Si3.divertech.databinding.ActivityProfileBinding;
import Si3.divertech.users.User;
import Si3.divertech.users.UserData;
import Si3.divertech.utils.UploadUtils;

public class ProfileActivity extends AppCompatActivity implements Observer {

    private List<TextInputLayout> fields;
    private ActivityProfileBinding binding;
    private boolean waitingUpload = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

        binding.profilePicture.setOnClickListener(click -> {
            Intent intent = new Intent(getApplicationContext(), ImageCropperActivity.class);
            intent.putExtra("aspectRatioX", 150);
            intent.putExtra("aspectRatioY", 150);
            intent.putExtra("shape", CropImageView.CropShape.OVAL.ordinal());
            startCrop.launch(intent);
        });

        fields=List.of(
                binding.lastNameContainer,
                binding.firstNameContainer,
                binding.mailContainer,
                binding.phoneContainer,
                binding.addressContainer,
                binding.cityContainer,
                binding.postalcodeContainer,
                binding.passwordContainer
        );

        setValues();
        UserData.getInstance().addObserver(this);
        binding.returnArrow.setOnClickListener(click -> finish());
        setOnClickEdit();
    }

    private void SetUpFields(TextInputLayout field, EditText edit, EditText confirm){
        if (field == binding.passwordContainer) {
            edit.setHint("Ancien mot de passe");
            confirm.setHint("Nouveau mot de passe");
            confirm.setInputType(129);
            confirm.setVisibility(View.VISIBLE);
        }else{
            edit.setText(field.getEditText().getText());
            if(field == findViewById(R.id.mail_container)){
                confirm.setHint("Confirmer le mot de passe");
                confirm.setInputType(129);
                confirm.setVisibility(View.VISIBLE);
            }
        }
        edit.setInputType(field.getEditText().getInputType());
        edit.setFilters(field.getEditText().getFilters());

    }

    private void changePassword(EditText edit, EditText confirm, TextView error, AlertDialog alertDialog, User user){
        if(!FormatChecker.checkPasswordLength(confirm)){
            error.setText("Le mot de passe doit contenir au moins 6 caractères");
            error.setVisibility(View.VISIBLE);
            return;
        }
        if(!FormatChecker.checkPasswordDigits(confirm)){
            error.setText("Le mot de passe doit contenir au moins un chiffre");
            error.setVisibility(View.VISIBLE);
            return;
        }

        if(user.changeUserPassword(confirm.getText().toString(),edit.getText().toString()).thenAccept(
                result -> {
                    if (Boolean.FALSE.equals(result)) {
                        error.setText("Mot de passe incorrect");
                        error.setVisibility(View.VISIBLE);
                    } else {
                        alertDialog.dismiss();
                    }
                }
        ).isCompletedExceptionally()){
            error.setText("Erreur lors de la mise à jour du mot de passe");
            error.setVisibility(View.VISIBLE);
        }
    }

    private void changeMail(EditText edit, EditText confirm, TextView error, AlertDialog alertDialog, User user){
        if(!FormatChecker.checkMail(edit)){
            error.setText("Adresse mail invalide");
            error.setVisibility(View.VISIBLE);
            return;
        }

        if(user.updateUserEmail(edit.getText().toString(), confirm.getText().toString()).thenAccept(
                result -> {
                    if(Boolean.FALSE.equals(result)){
                        error.setText("Mot de passe incorrect");
                        error.setVisibility(View.VISIBLE);
                    }
                    else{
                        alertDialog.dismiss();
                    }
                }
        ).isCompletedExceptionally()){
            error.setText("Erreur lors de la mise à jour de l'adresse mail");
            error.setVisibility(View.VISIBLE);
        }

        user.setEmail(edit.getText().toString());
    }

    private void fieldListener(TextInputLayout field, EditText edit, EditText confirm, TextView error, AlertDialog alertDialog, User user){
        if(!FormatChecker.globalCheck(edit)){
            error.setText("Ce champ ne peut pas être vide");
            error.setVisibility(View.VISIBLE);
            return;
        }
        if (field == binding.lastNameContainer) {
            user.setLastName(edit.getText().toString());
        } else if (field == binding.firstNameContainer) {
            user.setFirstName(edit.getText().toString());
        } else if (field == binding.mailContainer) {
            changeMail(edit, confirm, error, alertDialog, user);
        } else if (field == binding.phoneContainer) {
            if(!FormatChecker.checkPhone(edit)){
                error.setText("Numéro de téléphone invalide");
                error.setVisibility(View.VISIBLE);
                return;
            }
            user.setPhoneNumber(edit.getText().toString());
        } else if (field == binding.addressContainer) {
            user.setAddress(edit.getText().toString());
        } else if (field == binding.cityContainer) {
            user.setCity(edit.getText().toString());
        } else if (field == binding.postalcodeContainer) {
            user.setPostalCode(edit.getText().toString());
        } else if (field == binding.passwordContainer) {
            changePassword(edit, confirm, error, alertDialog, user);
        }

    }

    private void setOnClickEdit(){
        User user = UserData.getInstance().getConnectedUser();
        for(TextInputLayout field : fields){
            field.setEndIconOnClickListener(click -> {
                //init dialog
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_layout, null);
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme);
                builder.setTitle("Modifier votre " + field.getHint().toString().toLowerCase());

                EditText confirm = dialogView.findViewById(R.id.confirm);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();

                EditText edit = dialogView.findViewById(R.id.edit);

                SetUpFields(field, edit, confirm);

                Button validate = dialogView.findViewById(R.id.okButton);
                TextView error = dialogView.findViewById(R.id.errorTextView);

                validate.setOnClickListener(click1 -> {
                    fieldListener(field, edit, confirm, error, alertDialog, user);
                    if(field!=findViewById(R.id.password_container) && field!=findViewById(R.id.mail_container)){
                        alertDialog.dismiss();
                    }

                });

                Button cancel = dialogView.findViewById(R.id.cancelButton);
                cancel.setOnClickListener(click1 -> alertDialog.dismiss());

                alertDialog.show();
            });

        }
    }


    @Override
    public void update(Observable o, Object arg) {
        setValues();
    }

    private void setValues() {
        if (!UserData.getInstance().getConnectedUser().getPictureUrl().isEmpty())
            Picasso.get().load(UserData.getInstance().getConnectedUser().getPictureUrl()).into(binding.profilePicture);

        binding.username.setText(String.format("%s %s", UserData.getInstance().getConnectedUser().getFirstName(), UserData.getInstance().getConnectedUser().getLastName()));
        binding.lastName.setText(UserData.getInstance().getConnectedUser().getLastName());
        binding.firstName.setText(UserData.getInstance().getConnectedUser().getFirstName());
        binding.mail.setText(UserData.getInstance().getConnectedUser().getEmail());
        binding.phone.setText(UserData.getInstance().getConnectedUser().getPhoneNumber());
        binding.address.setText(UserData.getInstance().getConnectedUser().getAddress());
        binding.city.setText(UserData.getInstance().getConnectedUser().getCity());
        binding.postalcode.setText(UserData.getInstance().getConnectedUser().getPostalCode());
        binding.password.setText("**********");
    }

    public void showErrorMessage(ProgressBar progressBar) {
        Toast.makeText(getApplicationContext(), "Erreur lors du chargement de l'image", Toast.LENGTH_SHORT).show();
        waitingUpload = false;
    }

    private void uploadImage(String url) {
        if (url == null)
            return;

        ProgressBar progressBar = binding.progressBar;
        progressBar.setVisibility(View.VISIBLE);
        waitingUpload = true;

        OnSuccessListener<? super Uri> successListener = (OnSuccessListener<? super Uri>) uri -> {
            UserData.getInstance().setPictureUrl(uri.toString());
            progressBar.setVisibility(View.INVISIBLE);
            waitingUpload = false;
        };

        OnFailureListener failureListener = (OnFailureListener) -> {
            progressBar.setVisibility(View.INVISIBLE);
            showErrorMessage(progressBar);
        };

        UploadUtils.uploadImage(url, "/users/" + UserData.getInstance().getUserId() + ".jpg", 40, getApplicationContext(), successListener, failureListener);
    }
}