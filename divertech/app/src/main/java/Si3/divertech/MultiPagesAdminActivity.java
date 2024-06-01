package Si3.divertech;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Observable;
import java.util.Observer;

import Si3.divertech.notifications.NotificationCreator;
import Si3.divertech.notifications.NotificationList;
import Si3.divertech.notifications.NotificationTypes;
import Si3.divertech.users.User;

public class MultiPagesAdminActivity extends RequireUserActivity implements Observer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String notificationId = getIntent().getStringExtra(getString(R.string.notification_id));
        NotificationList.getInstance().getNotification(notificationId);

        setContentView(R.layout.activity_multi_pages_admin);
        View b = findViewById(R.id.return_arrow);
        b.setOnClickListener(click -> finish());
        View deleteButton = findViewById(R.id.bloc_close);
        deleteButton.setOnClickListener(click -> {
            NotificationList.getInstance().deleteNotification(notificationId);
            finish();
        });

        TextInputEditText description = findViewById(R.id.edit_text_area);
        AutoCompleteTextView typeSelector = findViewById(R.id.selector);
        int type = getIntent().getIntExtra("type", 0);
        NotificationTypes types = NotificationTypes.values()[type];
        typeSelector.setText(types.getContent());
        description.setText(NotificationList.getInstance().getNotification(notificationId).getDescription());

        NotificationCreator.getInstance().addObserver(this);
        setWriterInformation();
    }

    private void setWriterInformation() {
        User writer = NotificationCreator.getInstance().getNotificationCreatorUser();
        TextView name = findViewById(R.id.name);
        name.setText(String.format("%s\n%s", writer.getFirstName(), writer.getLastName()));
        View call = findViewById(R.id.bloc_contact_refuse);
        call.setOnClickListener(click -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + writer.getPhoneNumber()));
            startActivity(intent);
        });

        View mail = findViewById(R.id.bloc_contact_accept);
        mail.setOnClickListener(click -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + writer.getEmail()));
            startActivity(intent);
        });

    }

    @Override
    public void update(Observable o, Object arg) {
        setWriterInformation();
    }
}
