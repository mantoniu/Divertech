package Si3.divertech;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        ImageView backButton = findViewById(R.id.goback);
        backButton.setOnClickListener(v -> {
            finish();
        });

        Spinner spinner = findViewById(R.id.language);
        LangAdapter adapter = new LangAdapter(this);
        spinner.setOnItemSelectedListener(this);
        spinner.setAdapter(adapter);
        spinner.setSelection(LanguageSelected.LANGUAGE_SELECTED.equals("fr") ? 0 : 1);

        Button registerButton = findViewById(R.id.register);
        registerButton.setOnClickListener(v -> {
            TextInputEditText username = findViewById(R.id.username);
            TextInputEditText password = findViewById(R.id.password);
            TextInputEditText confirmPassword = findViewById(R.id.confirm_password);

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
                            Toast.makeText(RegisterActivity.this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Impossible de s'inscrire. Vérifiez votre adresse email", Toast.LENGTH_SHORT).show();
                        }
                    });
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(LanguageSelected.LANGUAGE_SELECTED.equals((String) parent.getItemAtPosition(position)))
            return;
        LanguageSelected.LANGUAGE_SELECTED = (String) parent.getItemAtPosition(position);
        LanguageSelected.setLanguage(this);
        recreate();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

}
