package Si3.divertech;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import Si3.divertech.qr_reader.CameraPreviewActivity;

public class ListEventActivity extends AppCompatActivity implements ClickableActivity{
    private final List<Event> eventList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listevent);
        Bundle b = new Bundle();
        b.putInt("page", 3);
        FootMenu f = new FootMenu();
        f.setArguments(b);
        getSupportFragmentManager().beginTransaction().add(R.id.footMenu, f).commit();
        View headerView = findViewById(R.id.header_menu);
        ((TextView) headerView.findViewById(R.id.feed_title)).setText(R.string.events);

        // Event feed fragment
        Bundle feedFragmentBundle = new Bundle();
        FeedFragment feedFragment = new FeedFragment();
        feedFragmentBundle.putInt(getString(R.string.FEED_TYPE), FeedType.EVENTS.ordinal());
        feedFragmentBundle.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) eventList);
        feedFragment.setArguments(feedFragmentBundle);
        getSupportFragmentManager().beginTransaction().add(R.id.events_feed, feedFragment).commit();

        findViewById(R.id.button_add).setOnClickListener(click -> {
            if (UserData.getConnectedUser().getIsAdmin()) {
                Intent intent = new Intent(getApplicationContext(), CreateEventActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getApplicationContext(), CameraPreviewActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public Context getContext(){
        return getApplicationContext();
    }
}