package Si3.divertech;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import Si3.divertech.notifications.NotificationList;
import Si3.divertech.notificationservice.NotificationChannel;
import Si3.divertech.notificationservice.NotificationContent;
import Si3.divertech.notificationservice.NotifyUser;
import Si3.divertech.users.UserData;

public class ReportActivity extends AppCompatActivity {
    private String eventId;

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
        TextView title = findViewById(R.id.title);
        CheckBox checkBox = findViewById(R.id.checkbox);


        button.setOnClickListener(click -> {
            if(testError())
                return;
            //notify all users that there is a new feed post
            NotificationList.getInstance().sendNotification(eventId,title.getText().toString(),((TextInputEditText) findViewById(R.id.edit_text_area)).getText().toString(), UserData.getInstance().getUserId(),this);
            NotificationContent notification;
            String imgURL="https://firebasestorage.googleapis.com/v0/b/divertech-6032b.appspot.com/o/NotificationImage%2Finfo.png?alt=media&token=f95e2232-938e-4c32-961a-b1711e0461d6";
            notification= new NotificationContent(title.getText().toString(), ((TextInputEditText) findViewById(R.id.edit_text_area)).getText().toString(),imgURL, NotificationChannel.CHANNEL_INFO);
            if(checkBox.isChecked()) {
                imgURL = "https://firebasestorage.googleapis.com/v0/b/divertech-6032b.appspot.com/o/NotificationImage%2Fwarning.png?alt=media&token=97890267-6b58-436a-8ece-feeaf5a8d203";
                notification= new NotificationContent(title.getText().toString(), ((TextInputEditText) findViewById(R.id.edit_text_area)).getText().toString(),imgURL,NotificationChannel.CHANNEL_WARNING);
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
        TextInputLayout titleLayout = findViewById(R.id.textfield_title);
        if (Objects.requireNonNull(((TextInputEditText) findViewById(R.id.title)).getText()).toString().isEmpty()) {
            titleLayout.setError(getResources().getText(R.string.error_no_message));
            findViewById(R.id.title).requestFocus();
            findViewById(R.id.title).requestLayout();
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
