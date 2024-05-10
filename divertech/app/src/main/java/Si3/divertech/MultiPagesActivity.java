package Si3.divertech;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class MultiPagesActivity extends AppCompatActivity {

    private NotificationTypes type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_pages);
        View b = findViewById(R.id.return_arrow);
        b.setOnClickListener(click -> finish());

        Button button = findViewById(R.id.send_Button);
        button.setOnClickListener(click -> forward());

        View headerView = findViewById(R.id.header_menu);
        ((TextView) headerView.findViewById(R.id.feed_title)).setText(R.string.contact);

        NotificationTypeAdapter adapter = new NotificationTypeAdapter(getApplicationContext(), R.layout.list_item, new ArrayList<>(Arrays.stream(NotificationTypes.values()).collect(Collectors.toList())));
        AutoCompleteTextView test = findViewById(R.id.selector);
        test.setAdapter(adapter);
        test.setOnItemClickListener((parent, arg1, position, arg3) -> type = adapter.getItem(position));
        test.setOnDismissListener(() -> {
                    TextInputLayout typeMassageLayout = findViewById(R.id.type_message_selection);
                    if (((AutoCompleteTextView) findViewById(R.id.selector)).getText().toString().isEmpty()) {
                        typeMassageLayout.setErrorEnabled(true);
                        typeMassageLayout.setError(getResources().getText(R.string.error_type_message));
                        findViewById(R.id.selector).requestFocus();
                    } else {
                        typeMassageLayout.setErrorEnabled(false);
                    }
                }
        );
    }

    private void forward() {
        Log.d("TEST", type.getTitle());
        finish();
    }

}
