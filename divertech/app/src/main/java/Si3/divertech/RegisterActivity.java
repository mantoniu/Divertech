package Si3.divertech;

import android.os.Bundle;
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
    private boolean areFieldsValid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        ImageView backButton = findViewById(R.id.goback);
        backButton.setOnClickListener(v -> finish());

        Spinner spinner = findViewById(R.id.language);
        LangAdapter adapter = new LangAdapter(this);
        spinner.setAdapter(adapter);

        Button registerButton = findViewById(R.id.register);
        registerButton.setOnClickListener(v -> {
            TextInputEditText username = findViewById(R.id.username);
            TextInputEditText password = findViewById(R.id.password);
            TextInputEditText confirmPassword = findViewById(R.id.confirm_password);
            TextInputEditText address = findViewById(R.id.address);
            TextInputEditText name = findViewById(R.id.firstName);
            TextInputEditText lastName = findViewById(R.id.name);
            TextInputEditText phoneNumber = findViewById(R.id.phone);

            if(username.getText().toString().isEmpty()){
                TextInputLayout usernameLayout = findViewById(R.id.username_container);
                usernameLayout.setError("Nom d'utilisateur requis");
                findViewById(R.id.username).requestFocus();
                return;
            }

            if(password.getText().toString().isEmpty()){
                TextInputLayout passwordLayout = findViewById(R.id.password_container);
                passwordLayout.setError("Mot de passe requis");
                findViewById(R.id.password).requestFocus();
                return;
            }
            if(password.getText().toString().length() < 6){
                TextInputLayout passwordLayout = findViewById(R.id.password_container);
                passwordLayout.setError("Le mot de passe doit contenir au moins 6 caractères");
                findViewById(R.id.password).requestFocus();
                return;
            }
            //check alphanumeric
            if(!password.getText().toString().matches(".*\\d.*") || !password.getText().toString().matches(".*[a-zA-Z].*")){
                TextInputLayout passwordLayout = findViewById(R.id.password_container);
                passwordLayout.setError("Le mot de passe doit contenir chiffres, lettres et caractères spéciaux");
                findViewById(R.id.password).requestFocus();
                return;
            }
            if(confirmPassword.getText().toString().isEmpty()){
                TextInputLayout confirmPasswordLayout = findViewById(R.id.confirm_password_container);
                confirmPasswordLayout.setError("Confirmation du mot de passe requise");
                findViewById(R.id.confirm_password).requestFocus();
                return;
            }

            if(!password.getText().toString().equals(confirmPassword.getText().toString())){
                TextInputLayout confirmPasswordLayout = findViewById(R.id.confirm_password_container);
                confirmPasswordLayout.setError("Les mots de passe ne correspondent pas");
                findViewById(R.id.confirm_password).requestFocus();
                return;
            }

            mAuth.createUserWithEmailAndPassword(username.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null && name.getText() != null && lastName.getText() != null && address.getText() != null && phoneNumber.getText() != null) {
                                UserData.writeNewUser(user.getUid(), name.getText().toString(), lastName.getText().toString(), username.getText().toString(), address.getText().toString(), phoneNumber.getText().toString(), spinner.getSelectedItem().toString());
                            }
                            Toast.makeText(RegisterActivity.this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Impossible de s'inscrire. Vérifiez votre adresse email", Toast.LENGTH_SHORT).show();
                        }
                    });
        });


    }
}