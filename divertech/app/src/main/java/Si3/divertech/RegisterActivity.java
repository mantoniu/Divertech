package Si3.divertech;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private boolean areFieldsValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        ImageView backButton = findViewById(R.id.goback);
        Button registerButton = findViewById(R.id.register);


        setListenerOnField(findViewById(R.id.lastName), findViewById(R.id.lastName_container), false, false, false);
        setListenerOnField(findViewById(R.id.firstName), findViewById(R.id.firstName_container), false, false, false);
        setListenerOnField(findViewById(R.id.username), findViewById(R.id.username_container), true, false, false);
        setListenerOnField(findViewById(R.id.phone), findViewById(R.id.phone_container), false, false, true);

        setListenerOnField(findViewById(R.id.password), findViewById(R.id.password_container), false, true, false);
        setListenerOnField(findViewById(R.id.confirm_password), findViewById(R.id.confirm_password_container), false, true, false);



        checkPasswordMatch();

        backButton.setOnClickListener(v -> finish());

        Spinner spinner = findViewById(R.id.language);
        LangAdapter adapter = new LangAdapter(this);
        spinner.setAdapter(adapter);





        registerButton.setOnClickListener(v -> {
            if (!areFieldsValid) {
                Toast.makeText(RegisterActivity.this, "Veuillez remplir correctement tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.d("test",((TextInputEditText)findViewById(R.id.username)).getText().toString());
            mAuth.createUserWithEmailAndPassword(((TextInputEditText)findViewById(R.id.username)).getText().toString(), ((TextInputEditText)findViewById(R.id.password)).getText().toString())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                //writeNewUser(String userId, String firstName, String lastName, String mail, String phoneNumber, String language, String address,String postalCode, String city)
                                UserData.writeNewUser(user.getUid(),((TextInputEditText)findViewById(R.id.firstName)).getText().toString(),((TextInputEditText)findViewById(R.id.lastName)).getText().toString(),((TextInputEditText)findViewById(R.id.username)).getText().toString(),((TextInputEditText)findViewById(R.id.phone)).getText().toString(),spinner.getSelectedItem().toString(),((TextInputEditText)findViewById(R.id.address)).getText().toString(),((TextInputEditText)findViewById(R.id.postalcode)).getText().toString(),((TextInputEditText)findViewById(R.id.city)).getText().toString());
                            }
                            Toast.makeText(RegisterActivity.this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            //show error message
                            task.getException().printStackTrace();
                            Toast.makeText(RegisterActivity.this, "Impossible de s'inscrire. Veuillez réessayer ulterieurement", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void setListenerOnField(TextInputEditText field, TextInputLayout layout, boolean mailFormat, boolean passwordFormat, boolean phoneFormat) {
        field.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (field.getText() == null || field.getText().toString().isEmpty()) {
                    layout.setError("Champ vide. Veuillez le remplir");
                    areFieldsValid = false;
                } else {
                    if (mailFormat && (!field.getText().toString().contains("@") || !field.getText().toString().contains("."))) {
                        layout.setError("Adresse email invalide");
                        areFieldsValid = false;
                    } else if (passwordFormat && field.getText().toString().length() < 6) {
                        layout.setError("Mot de passe trop court (6 caractères minimum)");
                        areFieldsValid = false;
                    }
                    //has digits
                    else if (passwordFormat && !field.getText().toString().matches(".*\\d.*")) {
                        layout.setError("Le mot de passe doit contenir au moins un chiffre");
                        areFieldsValid = false;
                    }
                    else if (phoneFormat && field.getText().toString().length() <8) {
                        layout.setError("Numéro de téléphone invalide");
                        areFieldsValid = false;
                    }
                    else {
                        layout.setError(null);
                        areFieldsValid = true;
                    }
                }
            }
        });
    }

    private void checkPasswordMatch(){
        TextInputEditText password = findViewById(R.id.password);
        TextInputEditText confirmPassword = findViewById(R.id.confirm_password);
        TextInputLayout confirmPasswordLayout = findViewById(R.id.confirm_password_container);

        confirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (confirmPassword.getText() == null || confirmPassword.getText().toString().isEmpty()) {
                    confirmPasswordLayout.setError("Champ vide. Veuillez le remplir");
                    areFieldsValid = false;
                } else if (!confirmPassword.getText().toString().equals(password.getText().toString())) {
                    confirmPasswordLayout.setError("Les mots de passe ne correspondent pas");
                    areFieldsValid = false;
                } else {
                    confirmPasswordLayout.setError(null);
                    areFieldsValid = true;
                }
            }
        });
    }


}
