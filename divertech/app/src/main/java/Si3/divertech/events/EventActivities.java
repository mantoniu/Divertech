package Si3.divertech.events;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.Observable;
import java.util.Observer;

import Si3.divertech.R;
import Si3.divertech.RequireUserActivity;

public abstract class EventActivities extends RequireUserActivity implements Observer {
    private String eventId;
    private boolean isTextViewClicked = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventId = getIntent().getStringExtra(getString(R.string.event_id));
        EventList.getInstance().addObserver(this);
        setActivity();
        updateInfo();
    }

    public abstract void setActivity();

    public String getEventId() {
        return eventId;
    }

    @Override
    public void update(Observable o, Object arg) {
        updateInfo();
    }

    protected void updateInfo() {
        if (!EventList.getInstance().containsEvent(eventId))
            return;

        TextView title = findViewById(R.id.name_event);
        title.setText(EventList.getInstance().getEvent(eventId).getTitle());

        TextView place = findViewById(R.id.localisation);
        place.setText(EventList.getInstance().getEvent(eventId).getFullAddress());

        TextView description = findViewById(R.id.description);
        description.setMaxLines(3);
        description.setText(EventList.getInstance().getEvent(eventId).getDescription());
        ImageView button = findViewById(R.id.more);
        button.setOnClickListener(click -> {
            if (isTextViewClicked) {
                //This will shrink textview to 2 lines if it is expanded.
                description.setMaxLines(3);
                button.setImageResource(R.drawable.more);
                isTextViewClicked = false;
            } else {
                //This will expand the textview if it is of 2 lines
                description.setMaxLines(Integer.MAX_VALUE);
                button.setImageResource(R.drawable.less);
                isTextViewClicked = true;
            }
        });

        findViewById(R.id.return_arrow).setOnClickListener(click -> finish());

        Picasso.get().load(EventList.getInstance().getEvent(eventId).getPictureUrl())
                .into((ImageView) findViewById(R.id.image_event));

        ((TextView) findViewById(R.id.date)).setText(EventList.getInstance().getEvent(eventId).getFormattedDate());
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateInfo();
    }
}
