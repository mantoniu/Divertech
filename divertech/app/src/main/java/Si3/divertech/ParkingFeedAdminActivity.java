package Si3.divertech;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import Si3.divertech.feed.Feed;
import Si3.divertech.feed.FeedFactory;
import Si3.divertech.feed.FeedType;
import Si3.divertech.parking.ParkingList;
import Si3.divertech.users.UserData;

public class ParkingFeedAdminActivity extends RequireUserActivity implements ClickableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_reservations);
        Intent intent = getIntent();
        String eventId = intent.getStringExtra(getString(R.string.event_id));

        ImageButton button = findViewById(R.id.return_arrow);
        button.setOnClickListener(click -> {
            ParkingList.getInstance().reset();
            finish();
        });


        Feed feedFragment = FeedFactory.createFeed(FeedType.PARKING, UserData.getInstance().getConnectedUser().getUserType());
        Bundle feedFragmentBundle = new Bundle();
        Log.d("test", eventId);
        feedFragmentBundle.putString(getString(R.string.event_id), eventId);
        feedFragment.setArguments(feedFragmentBundle);
        getSupportFragmentManager().beginTransaction().add(R.id.parking_feed, feedFragment).commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }
}