package Si3.divertech;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import Si3.divertech.feed.Feed;
import Si3.divertech.feed.FeedFactory;
import Si3.divertech.feed.FeedType;
import Si3.divertech.users.UserData;

public class MainActivity extends RequireUserActivity implements ClickableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String eventId = intent.getStringExtra(getString(R.string.event_id));

        ImageButton button = findViewById(R.id.return_arrow);

        if (intent.getBooleanExtra(getString(R.string.back_possible), false)) {
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(click -> finish());
        }

        Log.d("antoniu => ", UserData.getInstance().getConnectedUser().toString());
        Feed feedFragment = FeedFactory.createFeed(FeedType.NOTIFICATION, UserData.getInstance().getConnectedUser().getUserType());
        Bundle feedFragmentBundle = new Bundle();

        feedFragmentBundle.putString(getString(R.string.event_id), eventId);
        feedFragment.setArguments(feedFragmentBundle);

        if (eventId == null) {
            Bundle footMenuBundle = new Bundle();
            footMenuBundle.putInt("page", 1);
            FootMenu f = new FootMenu();
            f.setArguments(footMenuBundle);
            getSupportFragmentManager().beginTransaction().add(R.id.footMenu, f).commit();
        }

        getSupportFragmentManager().beginTransaction().add(R.id.notification_feed, feedFragment).commit();
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