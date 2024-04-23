package Si3.divertech;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements ClickableActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle b = new Bundle();
        b.putInt("page",1);
        FootMenu f = new FootMenu();
        f.setArguments(b);
        getSupportFragmentManager().beginTransaction().add(R.id.footMenu,f).commit();

        // Notification feed fragment
        Bundle notificationBundle = new Bundle();
        notificationBundle.putInt(getString(R.string.FEED_TYPE), FeedType.NOTIFICATION.ordinal());
        FeedFragment feedFragment = new FeedFragment();
        feedFragment.setArguments(notificationBundle);
        getSupportFragmentManager().beginTransaction().add(R.id.notification_feed, feedFragment).commit();
    }

    @Override
    public Context getContext(){
        return getApplicationContext();
    }
}