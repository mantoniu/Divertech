package Si3.divertech;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class EditUserActivity extends AppCompatActivity implements DataBaseListener {
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

        findViewById(R.id.return_arrow).setOnClickListener(click -> finish());

        username.setText(user.getEmail());
        address.setText(user.getAddress());
        name.setText(user.getName());
        lastName.setText(user.getLastName());
        phoneNumber.setText(user.getPhoneNumber());

        findViewById(R.id.save_modifications).setOnClickListener(click -> {
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

            UserData.updateUser(name, lastName, address, phoneNumber, language, email, password.getText().toString(), this);
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
}