package Si3.divertech.events;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import Si3.divertech.CreateEventActivity;
import Si3.divertech.MainActivity;
import Si3.divertech.MultiPagesActivity;
import Si3.divertech.R;

public class AdminEventActivity extends EventActivities {
    @Override
    public void setActivity() {
        setContentView(R.layout.activity_admin_event);
        Intent modification = new Intent(getApplicationContext(), CreateEventActivity.class);
        modification.putExtra(getString(R.string.event_id), getEventId());
        View change = findViewById(R.id.edit);
        change.setOnClickListener(click -> startActivity(modification));
        Intent report = new Intent(getApplicationContext(), MultiPagesActivity.class);
        View reportButton = findViewById(R.id.button_report);
        reportButton.setOnClickListener(click -> startActivity(report));

        View feed = findViewById(R.id.card_feed_type);
        feed.setOnClickListener(click -> {
            Log.d("Admin", getEventId() + " ");
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra(getString(R.string.event_id), getEventId());
            intent.putExtra(getString(R.string.back_possible), true);
            startActivity(intent);
        });
    }
}
