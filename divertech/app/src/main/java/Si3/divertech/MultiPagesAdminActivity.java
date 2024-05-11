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
        View close = findViewById(R.id.bloc_close);
        close.setOnClickListener(click -> finish());
        TextView title = findViewById(R.id.title);
        TextView description = findViewById(R.id.description);
        int type = getIntent().getIntExtra("type", 0);

        String notifId = getIntent().getStringExtra("id");
        Notification notif = NotificationList.getNotification(notifId);

        description.setText(notif.getDescription());

        View deleteButton = findViewById(R.id.bloc_close);
        deleteButton.setOnClickListener(click -> {
            NotificationList.deleteNotification(notif.getId());
            finish();
        });

        NotificationCreator.getInstance().addObserver(this);

        setWriterInformations();

        switch (type) {
            case EventActivity.CONTACT:
                title.setText("Objet message");
                break;
            case EventActivity.OBJET:
                title.setText("Objet Perdu");
                break;
            default:
                title.setText("Titre incident");
                break;
        }
    }

    private void setWriterInformations() {
        User writer = NotificationCreator.getInstance().getNotificationCreatorUser();
        TextView name = findViewById(R.id.name);
        name.setText(writer.getName() + "\n" + writer.getLastName());
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
        setWriterInformations();
    }
}
