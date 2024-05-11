package Si3.divertech;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Observable;
import java.util.Observer;

public class MultiPagesAdminActivity extends AppCompatActivity implements Observer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_pages_admin);
        View b = findViewById(R.id.return_arrow);
        b.setOnClickListener(click -> finish());
        View deleteButton = findViewById(R.id.bloc_close);
        TextView title = findViewById(R.id.title);
        TextView description = findViewById(R.id.description);
        int type = getIntent().getIntExtra("type", 0);

        String notificationId = getIntent().getStringExtra(getString(R.string.notification_id));
        NotificationList.getInstance().getNotification(notificationId);

        description.setText(NotificationList.getInstance().getNotification(notificationId).getDescription());

        deleteButton.setOnClickListener(click -> {
            NotificationList.getInstance().deleteNotification(notificationId);
            finish();
        });

        NotificationCreator.getInstance().addObserver(this);

        setWriterInformation();

        switch (type) {
            case EventActivity.CONTACT:
                title.setText(R.string.message_object);
                break;
            case EventActivity.OBJET:
                title.setText(R.string.losen_object);
                break;
            default:
                title.setText(R.string.incident_title);
                break;
        }
    }

    private void setWriterInformation() {
        User writer = NotificationCreator.getInstance().getNotificationCreatorUser();
        TextView name = findViewById(R.id.name);
        name.setText(String.format("%s\n%s", writer.getName(), writer.getLastName()));
        View call = findViewById(R.id.bloc_contact_phone);
        call.setOnClickListener(click -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + writer.getPhoneNumber()));
            startActivity(intent);
        });

        View mail = findViewById(R.id.bloc_contact_mail);
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
