package Si3.divertech;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
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
        AutoCompleteTextView spinner = findViewById(R.id.selector);
        spinner.setAdapter(adapter);
        spinner.setOnItemClickListener((parent, arg1, position, arg3) -> type = adapter.getItem(position));
        spinner.setOnDismissListener(() -> {
                    TextInputLayout typeMessageLayout = findViewById(R.id.type_message_selection);
                    if (((AutoCompleteTextView) findViewById(R.id.selector)).getText().toString().isEmpty()) {
                        typeMessageLayout.setErrorEnabled(true);
                        typeMessageLayout.setError(getResources().getText(R.string.error_type_message));
                        findViewById(R.id.selector).requestFocus();
                    } else {
                        typeMessageLayout.setErrorEnabled(false);
                    }
                }
        );
        findViewById(R.id.edit_text_area).setLayoutParams(new FrameLayout.LayoutParams(findViewById(R.id.edit_text_area).getLayoutParams().width, Resources.getSystem().getDisplayMetrics().heightPixels - 420 * (getApplicationContext().getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)));
        Log.d("testoooo", findViewById(R.id.edit_text_area).getLayoutParams().height + "");
        findViewById(R.id.edit_text_area).requestLayout();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        findViewById(R.id.description).setLayoutParams(params);
    }

    private void forward() {
        if (!testError()) {
            finish();
        }
    }


    public boolean testError() {
        boolean res = false;
        TextInputLayout typeMessageLayout = findViewById(R.id.type_message_selection);
        if (((AutoCompleteTextView) findViewById(R.id.selector)).getText().toString().isEmpty()) {
            typeMessageLayout.setErrorEnabled(true);
            typeMessageLayout.setError(getResources().getText(R.string.error_type_message));
            findViewById(R.id.selector).requestFocus();
            res = true;
        }
        TextInputLayout MessageLayout = findViewById(R.id.description);
        if (((TextInputEditText) findViewById(R.id.edit_text_area)).getText().toString().isEmpty()) {
            MessageLayout.setError(getResources().getText(R.string.error_type_message));
            findViewById(R.id.edit_text_area).requestFocus();
            findViewById(R.id.edit_text_area).requestLayout();
            res = true;
        }
        return res;
    }
}
