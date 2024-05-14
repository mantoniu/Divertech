package Si3.divertech;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements ClickableActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String eventId = intent.getStringExtra(getString(R.string.event_id));
        View headerView = findViewById(R.id.header_menu);
        ((TextView) headerView.findViewById(R.id.feed_title)).setText(R.string.notifications);

        ImageButton button = findViewById(R.id.return_arrow);

        if (intent.getBooleanExtra(getString(R.string.back_possible), false)) {
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(click -> finish());
        }

        Feed feedFragment = new NotificationFeed();

        if (eventId != null) {
            Bundle notificationBundle = new Bundle();
            notificationBundle.putString(getString(R.string.event_id), eventId);
            feedFragment.setArguments(notificationBundle);
            getSupportFragmentManager().beginTransaction().add(R.id.notification_feed, feedFragment).commit();
        } else {
            Bundle b = new Bundle();
            b.putInt("page", 1);
            FootMenu f = new FootMenu();
            f.setArguments(b);
            getSupportFragmentManager().beginTransaction().add(R.id.footMenu, f).commit();

            // Notification feed fragment
            Bundle notificationBundle = new Bundle();
            notificationBundle.putInt(getString(R.string.FEED_TYPE), FeedType.NOTIFICATION.ordinal());
            feedFragment.setArguments(notificationBundle);
            getSupportFragmentManager().beginTransaction().add(R.id.notification_feed, feedFragment).commit();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    public Context getContext(){
        return getApplicationContext();
    }
}