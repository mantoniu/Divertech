package Si3.divertech;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import Si3.divertech.notifications.NotificationList;
import Si3.divertech.notifications.NotificationTypeAdapter;
import Si3.divertech.notifications.NotificationTypes;
import Si3.divertech.notificationservice.NotificationChannel;
import Si3.divertech.notificationservice.NotificationContent;
import Si3.divertech.notificationservice.NotifyUser;
import Si3.divertech.users.UserData;

public class ReportActivity extends AppCompatActivity {
    private String eventId;
    private NotificationTypes type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        eventId = getIntent().getStringExtra(getString(R.string.event_id));
        setContentView(R.layout.activity_admin_report);



        View b = findViewById(R.id.return_arrow);
        b.setOnClickListener(click -> finish());

        Button button = findViewById(R.id.send_button);
        CheckBox checkBox = findViewById(R.id.checkbox);

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

        button.setOnClickListener(click -> {
            if(testError())
                return;
            //notify all users that there is a new feed post
            NotificationList.getInstance().sendNotification(eventId, type, Objects.requireNonNull(((TextInputEditText) findViewById(R.id.edit_text_area)).getText()).toString(), Objects.requireNonNull(((TextInputEditText) findViewById(R.id.edit_text_area_en)).getText()).toString(), UserData.getInstance().getUserId(), this);
            NotificationContent notification;
            String imgURL="https://firebasestorage.googleapis.com/v0/b/divertech-6032b.appspot.com/o/NotificationImage%2Finfo.png?alt=media&token=f95e2232-938e-4c32-961a-b1711e0461d6";
            notification = new NotificationContent(type, Objects.requireNonNull(((TextInputEditText) findViewById(R.id.edit_text_area)).getText()).toString(), Objects.requireNonNull(((TextInputEditText) findViewById(R.id.edit_text_area_en)).getText()).toString(), imgURL, NotificationChannel.CHANNEL_INFO);
            if(checkBox.isChecked()) {
                imgURL = "https://firebasestorage.googleapis.com/v0/b/divertech-6032b.appspot.com/o/NotificationImage%2Fwarning.png?alt=media&token=97890267-6b58-436a-8ece-feeaf5a8d203";
                notification = new NotificationContent(type, Objects.requireNonNull(((TextInputEditText) findViewById(R.id.edit_text_area)).getText()).toString(), Objects.requireNonNull(((TextInputEditText) findViewById(R.id.edit_text_area_en)).getText()).toString(), imgURL, NotificationChannel.CHANNEL_WARNING);
            }
            NotifyUser.notifyUserWithEventId(eventId, notification, this);
            finish();
        });

        findViewById(R.id.edit_text_area).setLayoutParams(new FrameLayout.LayoutParams(findViewById(R.id.edit_text_area).getLayoutParams().width, Resources.getSystem().getDisplayMetrics().heightPixels - 420 * (getApplicationContext().getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        findViewById(R.id.description).setLayoutParams(params);

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
        if (Objects.requireNonNull(((TextInputEditText) findViewById(R.id.edit_text_area)).getText()).toString().isEmpty()) {
            MessageLayout.setError(getResources().getText(R.string.error_no_message));
            findViewById(R.id.edit_text_area).requestFocus();
            findViewById(R.id.edit_text_area).requestLayout();
            res = true;
        }
        return res;
    }


}
