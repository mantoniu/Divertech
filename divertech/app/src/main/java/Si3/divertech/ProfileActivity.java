package Si3.divertech;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.util.List;

import java.util.Observable;
import java.util.Observer;

import Si3.divertech.users.UserData;

public class ProfileActivity extends AppCompatActivity implements Observer {

    private List<TextInputLayout> fields;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fields=List.of(
                findViewById(R.id.lastName_container),
                findViewById(R.id.firstName_container),
                findViewById(R.id.mail_container),
                findViewById(R.id.phone_container),
                findViewById(R.id.address_container),
                findViewById(R.id.city_container),
                findViewById(R.id.postalcode_container),
                findViewById(R.id.password_container)
        );

        setValues();
        UserData.getInstance().addObserver(this);
        findViewById(R.id.return_arrow).setOnClickListener(click -> finish());
        setOnClickEdit();
    }

    private void SetUpFields(TextInputLayout field, EditText edit, EditText confirm){
        if(field == findViewById(R.id.password_container)){
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
        if(field==findViewById(R.id.lastName_container)){
            user.setLastName(edit.getText().toString());
        }
        else if(field==findViewById(R.id.firstName_container)){
            user.setFirstName(edit.getText().toString());
        }
        else if(field==findViewById(R.id.mail_container)){
            changeMail(edit, confirm, error, alertDialog, user);
        }
        else if(field==findViewById(R.id.phone_container)){
            if(!FormatChecker.checkPhone(edit)){
                error.setText("Numéro de téléphone invalide");
                error.setVisibility(View.VISIBLE);
                return;
            }
            user.setPhoneNumber(edit.getText().toString());
        }
        else if(field==findViewById(R.id.address_container)){
            user.setAddress(edit.getText().toString());
        }
        else if(field==findViewById(R.id.city_container)){
            user.setCity(edit.getText().toString());
        }
        else if(field==findViewById(R.id.postalcode_container)){
            user.setPostalCode(edit.getText().toString());
        }
        else if(field==findViewById(R.id.password_container)){
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
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
        User user = UserData.getInstance().getConnectedUser();
        TextView name = findViewById(R.id.username);
        name.setText(user.getFirstName()+" "+user.getLastName());
        TextView lastName = findViewById(R.id.lastName);
        lastName.setText(user.getLastName());
        TextView firstName = findViewById(R.id.firstName);
        firstName.setText(user.getFirstName());
        TextView email = findViewById(R.id.mail);
        email.setText(user.getEmail());
        TextView phone = findViewById(R.id.phone);
        phone.setText(user.getPhoneNumber());
        TextView address = findViewById(R.id.address);
        address.setText(user.getAddress());
        TextView city = findViewById(R.id.city);
        city.setText(user.getCity());
        TextView postalCode = findViewById(R.id.postalcode);
        postalCode.setText(user.getPostalCode());
        TextView password = findViewById(R.id.password);
        password.setText("**********");
    }
}