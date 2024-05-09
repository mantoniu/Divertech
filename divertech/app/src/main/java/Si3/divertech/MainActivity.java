package Si3.divertech;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements ClickableActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String eventId = intent.getStringExtra("eventId");
        View headerView = findViewById(R.id.header_menu);
        TextView feedTitle = headerView.findViewById(R.id.feed_title);

        if (eventId != null) {
            feedTitle.setText(R.string.events);
            Bundle notificationBundle = new Bundle();
            notificationBundle.putInt(getString(R.string.FEED_TYPE), FeedType.NOTIFICATION.ordinal());
            notificationBundle.putString("eventId", eventId);
            FeedFragment feedFragment = new FeedFragment();
            feedFragment.setArguments(notificationBundle);
            getSupportFragmentManager().beginTransaction().add(R.id.notification_feed, feedFragment).commit();
        } else {
            feedTitle.setText(R.string.notifications);
            Bundle b = new Bundle();
            b.putInt("page", 1);
            FootMenu f = new FootMenu();
            f.setArguments(b);
            getSupportFragmentManager().beginTransaction().add(R.id.footMenu, f).commit();
            ((TextView) findViewById(R.id.header_menu).findViewById(R.id.feed_title)).setText(R.string.notifications);

            // Notification feed fragment
            Bundle notificationBundle = new Bundle();
            notificationBundle.putInt(getString(R.string.FEED_TYPE), FeedType.NOTIFICATION.ordinal());
            FeedFragment feedFragment = new FeedFragment();
            feedFragment.setArguments(notificationBundle);
            getSupportFragmentManager().beginTransaction().add(R.id.notification_feed, feedFragment).commit();
        }
    }

    @Override
    public Context getContext(){
        return getApplicationContext();
    }
}