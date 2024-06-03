package Si3.divertech;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import Si3.divertech.databinding.ActivityRegisterBinding;
import Si3.divertech.users.UserData;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private boolean areFieldsValid = false;
    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        ImageView backButton = findViewById(R.id.goback);
        Button registerButton = findViewById(R.id.register);

        setListenerOnField(binding.lastName, binding.lastNameContainer, false, false, false);
        setListenerOnField(binding.firstName, binding.firstNameContainer, false, false, false);
        setListenerOnField(binding.username, binding.usernameContainer, true, false, false);
        setListenerOnField(binding.phone, binding.passwordContainer, false, false, true);

        setListenerOnField(findViewById(R.id.password), findViewById(R.id.password_container), false, true, false);
        setListenerOnField(findViewById(R.id.confirm_password), findViewById(R.id.confirm_password_container), false, true, false);



        checkPasswordMatch();

        backButton.setOnClickListener(v -> finish());

        Spinner spinner = findViewById(R.id.language);
        LangAdapter adapter = new LangAdapter(this);
        spinner.setAdapter(adapter);

        registerButton.setOnClickListener(v -> {
            if (!areFieldsValid) {
                Toast.makeText(RegisterActivity.this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
                return;
            }

            if (binding.username.getText() == null || binding.password.getText() == null)
                return;

            mAuth.createUserWithEmailAndPassword(binding.username.getText().toString(), binding.password.getText().toString())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                if (binding.firstName.getText() == null || binding.lastName.getText() == null || binding.phone.getText() == null || binding.address.getText() == null || binding.postalcode.getText() == null || binding.city.getText() == null)
                                    return;

                                UserData.getInstance().writeNewUser(user.getUid(), binding.firstName.getText().toString(), binding.lastName.getText().toString(), binding.username.getText().toString(),
                                        binding.phone.getText().toString(), spinner.getSelectedItem().toString(), binding.address.getText().toString(), binding.postalcode.getText().toString(),
                                        binding.city.getText().toString());
                            }
                            Toast.makeText(RegisterActivity.this, R.string.register_successful, Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Log.d("INSCRIPTION ERROR : ", "", task.getException());
                            Toast.makeText(RegisterActivity.this, R.string.register_error, Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void setListenerOnField(TextInputEditText field, TextInputLayout layout, boolean mailFormat, boolean passwordFormat, boolean phoneFormat) {
        field.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (!FormatChecker.globalCheck(field)) {
                    layout.setError(getString(R.string.empty_field));
                    areFieldsValid = false;
                } else {
                    if (mailFormat && !FormatChecker.checkMail(field)) {
                        layout.setError(getString(R.string.invalid_email_address));
                        areFieldsValid = false;
                    } else if (passwordFormat && !FormatChecker.checkPasswordLength(field)) {
                        layout.setError(getString(R.string.password_need_to_contain_six_characters));
                        areFieldsValid = false;
                    }
                    //has digits
                    else if (passwordFormat && !FormatChecker.checkPasswordDigits(field)) {
                        layout.setError(getString(R.string.password_need_to_contain_one_number));
                        areFieldsValid = false;
                    }
                    else if (phoneFormat && !FormatChecker.checkPhone(field)) {
                        layout.setError(getString(R.string.invalid_phone_number));
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
        TextInputEditText password = binding.password;
        TextInputEditText confirmPassword = binding.confirmPassword;
        TextInputLayout confirmPasswordLayout = binding.confirmPasswordContainer;

        confirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (confirmPassword.getText() == null || confirmPassword.getText().toString().isEmpty()) {
                    confirmPasswordLayout.setError(getString(R.string.empty_field));
                    areFieldsValid = false;
                } else if (password.getText() != null && !confirmPassword.getText().toString().equals(password.getText().toString())) {
                    confirmPasswordLayout.setError(getString(R.string.incorrect_password));
                    areFieldsValid = false;
                } else {
                    confirmPasswordLayout.setError(null);
                    areFieldsValid = true;
                }
            }
        });
    }


}
