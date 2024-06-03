package Si3.divertech.users;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.canhub.cropper.CropImageView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import Si3.divertech.FormatChecker;
import Si3.divertech.ImageCropperActivity;
import Si3.divertech.R;
import Si3.divertech.utils.UploadUtils;

public class ProfileController {
    private ProfileView view;

    public ProfileController() {
    }

    public void editProfilePicture() {
        Intent intent = new Intent(view.getContext(), ImageCropperActivity.class);
        intent.putExtra("aspectRatioX", 150);
        intent.putExtra("aspectRatioY", 150);
        intent.putExtra("shape", CropImageView.CropShape.OVAL.ordinal());
        view.startCrop(intent);
    }

    protected void setOnClickEdit() {
        for (TextInputLayout field : view.getFields()) {
            field.setEndIconOnClickListener(click -> {
                LayoutInflater inflater = view.getInflater();
                View dialogView = inflater.inflate(R.layout.dialog_layout, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                if (field.getHint() != null){
                    String title=field.getHint().toString().toLowerCase();
                    builder.setTitle(title);
                }
                EditText confirm = dialogView.findViewById(R.id.confirm);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();

                EditText edit = dialogView.findViewById(R.id.edit);

                SetUpFields(field, edit, confirm);

                Button validate = dialogView.findViewById(R.id.okButton);
                TextView error = dialogView.findViewById(R.id.errorTextView);

                validate.setOnClickListener(click1 -> {
                    fieldListener(field, edit, confirm, error, alertDialog);
                    if (field != view.getBinding().passwordContainer && field != view.getBinding().mailContainer) {
                        alertDialog.dismiss();
                    }

                });

                Button cancel = dialogView.findViewById(R.id.cancelButton);
                cancel.setOnClickListener(click1 -> alertDialog.dismiss());

                alertDialog.show();
            });

        }
    }

    private void SetUpFields(TextInputLayout field, EditText edit, EditText confirm) {
        if (field == view.getBinding().passwordContainer) {
            edit.setHint(R.string.past_password);
            confirm.setHint(R.string.new_password);
            confirm.setInputType(129);
            confirm.setVisibility(View.VISIBLE);
        } else {
            if (field.getEditText() != null)
                edit.setText(field.getEditText().getText());
            if (field == view.getBinding().mailContainer) {
                confirm.setHint(R.string.confirm_password);
                confirm.setInputType(129);
                confirm.setVisibility(View.VISIBLE);
            }
        }
        if (field.getEditText() != null)
            edit.setInputType(field.getEditText().getInputType());
        edit.setFilters(field.getEditText().getFilters());
    }

    private void fieldListener(TextInputLayout field, EditText edit, EditText confirm, TextView error, AlertDialog alertDialog) {
        if (!FormatChecker.globalCheck(edit)) {
            error.setText(R.string.canot_be_empty);
            error.setVisibility(View.VISIBLE);
            return;
        }
        if (field == view.getBinding().lastNameContainer) {
            UserData.getInstance().setLastName(edit.getText().toString());
        } else if (field == view.getBinding().firstNameContainer) {
            UserData.getInstance().setFirstName(edit.getText().toString());
        } else if (field == view.getBinding().mailContainer) {
            changeMail(edit, confirm, error, alertDialog);
        } else if (field == view.getBinding().phoneContainer) {
            if (!FormatChecker.checkPhone(edit)) {
                error.setText(R.string.invalid_phone_number);
                error.setVisibility(View.VISIBLE);
                return;
            }
            UserData.getInstance().setPhoneNumber(edit.getText().toString());
        } else if (field == view.getBinding().addressContainer) {
            UserData.getInstance().setAddress(edit.getText().toString());
        } else if (field == view.getBinding().cityContainer) {
            UserData.getInstance().setCity(edit.getText().toString());
        } else if (field == view.getBinding().postalcodeContainer) {
            UserData.getInstance().setPostalCode(edit.getText().toString());
        } else if (field == view.getBinding().passwordContainer) {
            changePassword(edit, confirm, error, alertDialog);
        }
    }

    private void showErrorMessage() {
        Toast.makeText(view.getContext(), R.string.error_loading_image, Toast.LENGTH_SHORT).show();
    }

    protected void uploadImage(String url) {
        if (url == null)
            return;

        ProgressBar progressBar = view.getBinding().progressBar;
        progressBar.setVisibility(View.VISIBLE);

        OnSuccessListener<? super Uri> successListener = uri -> {
            UserData.getInstance().setPictureUrl(uri.toString());
            progressBar.setVisibility(View.INVISIBLE);
        };

        OnFailureListener failureListener = (OnFailureListener) -> {
            progressBar.setVisibility(View.INVISIBLE);
            showErrorMessage();
        };

        UploadUtils.uploadImage(url, "/users/" + UserData.getInstance().getUserId() + ".jpg", 40, view.getContext(), successListener, failureListener);
    }


    private void changeMail(EditText edit, EditText confirm, TextView error, AlertDialog alertDialog) {
        if (!FormatChecker.checkMail(edit)) {
            error.setText(R.string.invalid_email_address);
            error.setVisibility(View.VISIBLE);
            return;
        }

        if (UserData.getInstance().updateUserEmail(edit.getText().toString(), confirm.getText().toString()).thenAccept(
                result -> {
                    if (Boolean.FALSE.equals(result)) {
                        error.setText(R.string.incorrect_password);
                        error.setVisibility(View.VISIBLE);
                    } else {
                        alertDialog.dismiss();
                    }
                }
        ).isCompletedExceptionally()) {
            error.setText(R.string.email_update_error);
            error.setVisibility(View.VISIBLE);
        }

        UserData.getInstance().setUserEmail(edit.getText().toString());
    }

    private void changePassword(EditText edit, EditText confirm, TextView error, AlertDialog alertDialog) {
        if (!FormatChecker.checkPasswordLength(confirm)) {
            error.setText(R.string.password_need_to_contain_six_characters);
            error.setVisibility(View.VISIBLE);
            return;
        }
        if (!FormatChecker.checkPasswordDigits(confirm)) {
            error.setText(R.string.password_need_to_contain_one_number);
            error.setVisibility(View.VISIBLE);
            return;
        }

        if (UserData.getInstance().changeUserPassword(confirm.getText().toString(), edit.getText().toString()).thenAccept(
                result -> {
                    if (Boolean.FALSE.equals(result)) {
                        error.setText(R.string.incorrect_password);
                        error.setVisibility(View.VISIBLE);
                    } else {
                        alertDialog.dismiss();
                    }
                }
        ).isCompletedExceptionally()) {
            error.setText(R.string.password_update_error);
            error.setVisibility(View.VISIBLE);
        }
    }

    protected void setValues() {
        if (!UserData.getInstance().getConnectedUser().getPictureUrl().isEmpty())
            Picasso.get().load(UserData.getInstance().getConnectedUser().getPictureUrl()).into(view.getBinding().profilePicture);

        view.getBinding().username.setText(String.format("%s %s", UserData.getInstance().getConnectedUser().getFirstName(), UserData.getInstance().getConnectedUser().getLastName()));
        view.getBinding().lastName.setText(UserData.getInstance().getConnectedUser().getLastName());
        view.getBinding().firstName.setText(UserData.getInstance().getConnectedUser().getFirstName());
        view.getBinding().mail.setText(UserData.getInstance().getConnectedUser().getEmail());
        view.getBinding().phone.setText(UserData.getInstance().getConnectedUser().getPhoneNumber());
        view.getBinding().address.setText(UserData.getInstance().getConnectedUser().getAddress());
        view.getBinding().city.setText(UserData.getInstance().getConnectedUser().getCity());
        view.getBinding().postalcode.setText(UserData.getInstance().getConnectedUser().getPostalCode());
        view.getBinding().password.setText("**********");
    }

    public void setView(ProfileView view) {
        this.view = view;
    }
}
