package Si3.divertech;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements ClickableActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String eventId = intent.getStringExtra("eventId");
        View headerView = findViewById(R.id.header_menu);
        ((TextView) headerView.findViewById(R.id.feed_title)).setText(R.string.notifications);

        Bundle bundle = new Bundle();


        if (eventId != null) {
            bundle.putInt(getString(R.string.FEED_TYPE), FeedType.NOTIFICATION.ordinal());
            bundle.putString("eventId", eventId);
        } else {
            bundle.putInt("page", 1);
            FootMenu f = new FootMenu();
            f.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.footMenu, f).commit();

            // Notification feed fragment
            bundle.putInt(getString(R.string.FEED_TYPE), FeedType.NOTIFICATION.ordinal());
        }
        launchFeedFragment(bundle, R.id.notification_feed);
    }

    public void launchFeedFragment(Bundle bundle, @IdRes int res) {
        FeedFragment feedFragment = new FeedFragment();
        feedFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(res, feedFragment).commit();
    }

    @Override
    public Context getContext(){
        return getApplicationContext();
    }
}