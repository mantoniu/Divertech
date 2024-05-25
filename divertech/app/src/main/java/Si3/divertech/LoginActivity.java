package Si3.divertech;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Observable;
import java.util.Observer;

import Si3.divertech.databinding.ActivityLoginBinding;
import Si3.divertech.users.UserData;

public class LoginActivity extends AppCompatActivity implements Observer {
    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextView register = binding.register;
        register.setOnClickListener(click -> {
            Intent i = new Intent(this, RegisterActivity.class);
            startActivity(i);
        });

        Button login = binding.login;
        login.setOnClickListener(click -> {
            if (!NetwordTest.isNetworkAvailable(this.getApplication())) {
                Spannable centeredText = new SpannableString("Pas de connexion internet\nMerci de vérifier votre connection"); //TODO à reprendre
                centeredText.setSpan(
                        new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                        0, centeredText.length() - 1,
                        Spannable.SPAN_INCLUSIVE_INCLUSIVE
                );
                Toast.makeText(getApplicationContext(), centeredText, Toast.LENGTH_LONG).show();
                return;
            }

            ProgressBar loading = findViewById(R.id.progress);
            loading.setVisibility(View.VISIBLE);
            TextInputEditText username = binding.username;
            TextInputEditText password = binding.password;

            if (username.getText() != null && username.getText().toString().isEmpty()) {
                TextInputLayout usernameLayout = findViewById(R.id.username_container);
                usernameLayout.setError("Nom d'utilisateur requis");
                findViewById(R.id.username).requestFocus();
                loading.setVisibility(View.GONE);
                return;
            }

            if (password.getText() != null && password.getText().toString().isEmpty()) {
                TextInputLayout passwordLayout = findViewById(R.id.password_container);
                passwordLayout.setError("Mot de passe requis");
                findViewById(R.id.password).requestFocus();
                loading.setVisibility(View.GONE);
                return;
            }

            mAuth.signInWithEmailAndPassword(username.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                UserData.getInstance().addObserver(this);
                                UserData.getInstance().requestUserData(user);
                            }
                        } else {
                            Toast.makeText(this, "Impossible de se connecter", Toast.LENGTH_SHORT).show();
                            TextInputLayout passwordLayout = findViewById(R.id.password_container);
                            passwordLayout.setError("Nom d'utilisateur ou mot de passe incorrect");
                            loading.setVisibility(View.GONE);
                        }
                    });
        });

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Button google = findViewById(R.id.google);
        google.setOnClickListener(click -> signInGoogle());
    }


    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            try {
                GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.d("LOGIN ERROR", "", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        mAuth.signInWithCredential(GoogleAuthProvider.getCredential(idToken, null))
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Connexion réussie", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(this, MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(this, "Impossible de se connecter avec Google :(", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void update(Observable o, Object arg) {
        if (UserData.getInstance().getConnectedUser() == null)
            return;

        Toast.makeText(this, "Connexion réussie", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        UserData.getInstance().deleteObserver(this);
        finish();
    }
}