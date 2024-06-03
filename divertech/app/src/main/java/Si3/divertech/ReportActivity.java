package Si3.divertech;

import android.os.Bundle;
import android.os.StrictMode;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

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
import Si3.divertech.utils.LangUtils;

public class ReportActivity extends RequireUserActivity {
    private String eventId;
    private NotificationTypes type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        LangUtils.changeActionBarTitle(this);
        eventId = getIntent().getStringExtra(getString(R.string.event_id));
        setContentView(R.layout.activity_admin_report);



        View b = findViewById(R.id.return_arrow);
        b.setOnClickListener(click -> finish());

        Button button = findViewById(R.id.send_button);

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
            if (testError())
                return;

            if (!NetworkTest.isNetworkAvailable(this.getApplication())) {
                Spannable centeredText = new SpannableString(getString(R.string.no_connection));
                centeredText.setSpan(
                        new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                        0, centeredText.length() - 1,
                        Spannable.SPAN_INCLUSIVE_INCLUSIVE
                );
                Toast.makeText(getApplicationContext(), centeredText, Toast.LENGTH_LONG).show();
                return;
            }
            //notify all users that there is a new feed post
            NotificationList.getInstance().sendNotification(eventId, type, Objects.requireNonNull(((TextInputEditText) findViewById(R.id.edit_text_area)).getText()).toString(), Objects.requireNonNull(((TextInputEditText) findViewById(R.id.edit_text_area_en)).getText()).toString(), UserData.getInstance().getUserId());
            NotificationContent notification;
            String imgURL = "https://firebasestorage.googleapis.com/v0/b/divertech-6032b.appspot.com/o/NotificationImage%2Finfo.png?alt=media&token=f95e2232-938e-4c32-961a-b1711e0461d6";
            notification = new NotificationContent(type, Objects.requireNonNull(((TextInputEditText) findViewById(R.id.edit_text_area)).getText()).toString(), Objects.requireNonNull(((TextInputEditText) findViewById(R.id.edit_text_area_en)).getText()).toString(), imgURL, NotificationChannel.CHANNEL_INFO);
            if (type == NotificationTypes.INCIDENT) {
                imgURL = "https://firebasestorage.googleapis.com/v0/b/divertech-6032b.appspot.com/o/NotificationImage%2Fwarning.png?alt=media&token=97890267-6b58-436a-8ece-feeaf5a8d203";
                notification = new NotificationContent(type, Objects.requireNonNull(((TextInputEditText) findViewById(R.id.edit_text_area)).getText()).toString(), Objects.requireNonNull(((TextInputEditText) findViewById(R.id.edit_text_area_en)).getText()).toString(), imgURL, NotificationChannel.CHANNEL_WARNING);
            }
            NotifyUser.notifyUserWithEventId(eventId, notification, this);
            finish();
        });
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
        TextInputLayout MessageLayoutEn = findViewById(R.id.description_en);
        if (Objects.requireNonNull(((TextInputEditText) findViewById(R.id.edit_text_area_en)).getText()).toString().isEmpty()) {
            MessageLayoutEn.setError(getResources().getText(R.string.error_no_message));
            findViewById(R.id.edit_text_area_en).requestFocus();
            findViewById(R.id.edit_text_area_en).requestLayout();
            res = true;
        }
        return res;
    }


}
