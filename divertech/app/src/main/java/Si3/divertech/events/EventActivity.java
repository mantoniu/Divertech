package Si3.divertech.events;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Observable;
import java.util.Optional;

import Si3.divertech.MapActivity;
import Si3.divertech.MultiPagesActivity;
import Si3.divertech.ParkingActivity;
import Si3.divertech.ParkingResultActivity;
import Si3.divertech.R;
import Si3.divertech.parking.ParkingList;
import Si3.divertech.parking.Reservations;
import Si3.divertech.users.User;

public class EventActivity extends EventActivities {
    private final ActivityResultLauncher<String> requestWritePermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            addEventToCalendar(getEventId());
        } else {
            Toast.makeText(getApplicationContext(), R.string.permission_denied, Toast.LENGTH_LONG)
                    .show();
        }
    });

    private final ActivityResultLauncher<String> requestReadPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            requestWritePermission.launch(Manifest.permission.WRITE_CALENDAR);
        } else {
            Toast.makeText(getApplicationContext(), R.string.permission_denied, Toast.LENGTH_LONG)
                    .show();
        }
    });

    @Override
    public void setActivity() {
        setContentView(R.layout.activity_event);
        Intent modification = new Intent(getApplicationContext(), MultiPagesActivity.class);
        modification.putExtra("eventId", getEventId());
        View contact = findViewById(R.id.contact_organizer);
        contact.setOnClickListener(click -> startActivity(modification));

        if (!EventList.getInstance().containsEvent(getEventId())) {
            finish();
            return;
        }

        MaterialCardView map = findViewById(R.id.card_name_event);
        map.setOnClickListener(click -> {
            Intent intent = new Intent(getApplicationContext(), MapActivity.class);
            intent.putExtra(getString(R.string.event_id), getEventId());
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.card_date).setOnClickListener((click) -> requestReadPermission.launch(Manifest.permission.READ_CALENDAR));

        MaterialCardView parkingLayout = findViewById(R.id.card_parking);
        parkingLayout.setOnClickListener(v -> {

                Optional<Reservations> result = ParkingList.getInstance().existReservation(getEventId());
                Intent intent = new Intent(getApplicationContext(), ParkingActivity.class);
                if (result.isPresent()) {
                    intent = new Intent(getApplicationContext(), ParkingResultActivity.class);
                    intent.putExtra("reservationId", result.get().getId());
                } else {
                    intent.putExtra("eventId", getEventId());
                }
                startActivity(intent);

        });

        EventOrganizer.getInstance().addObserver(this);
        EventOrganizer.getInstance().getUser(EventList.getInstance().getEvent(getEventId()).getOrganizer());
        ParkingList.getInstance();
    }

    private void addEventToCalendar(String eventId) {
        if (eventExistsInCalendar(EventList.getInstance().getEvent(eventId).getTitle(), EventList.getInstance().getEvent(eventId).getZonedDate())) {
            Toast.makeText(getApplicationContext(), R.string.event_already_in_diary, Toast.LENGTH_LONG)
                    .show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, EventList.getInstance().getEvent(eventId).getZonedDate().toInstant().toEpochMilli());
        values.put(CalendarContract.Events.TITLE, EventList.getInstance().getEvent(eventId).getTitle());
        values.put(CalendarContract.Events.DESCRIPTION, EventList.getInstance().getEvent(eventId).getShortDescription());
        values.put(CalendarContract.Events.CALENDAR_ID, getDefaultCalendarId());
        values.put(CalendarContract.Events.EVENT_TIMEZONE, ZoneId.systemDefault().getId());
        values.put(CalendarContract.Events.DURATION, "PT2H");

        Uri uri = getContentResolver().insert(CalendarContract.Events.CONTENT_URI, values);
        if (uri != null) {
            Toast.makeText(getApplicationContext(), R.string.event_added_to_diary, Toast.LENGTH_LONG).show();
        }
    }

    private long getDefaultCalendarId() {
        String[] projection = new String[]{CalendarContract.Calendars._ID};
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = CalendarContract.Calendars.IS_PRIMARY + " = ?";
        String[] selectionArgs = new String[]{"1"};

        Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs, null);

        if (cursor != null && cursor.moveToFirst()) {
            int index = cursor.getColumnIndex(CalendarContract.Calendars._ID);
            if (index != -1) {
                long calendarId = cursor.getLong(index);
                cursor.close();
                return calendarId;
            }
        }
        return -1;
    }

    private boolean eventExistsInCalendar(String title, ZonedDateTime startDateTime) {
        String[] projection = new String[]{CalendarContract.Events._ID};
        String selection = CalendarContract.Events.TITLE + "=? AND " + CalendarContract.Events.DTSTART + "=?";
        String[] selectionArgs = new String[]{title, String.valueOf(startDateTime.toInstant().toEpochMilli())};

        Cursor cursor = getContentResolver().query(
                CalendarContract.Events.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
        );

        boolean eventExists = cursor != null && cursor.getCount() > 0;

        if (cursor != null) {
            cursor.close();
        }

        return eventExists;
    }

    private void showOrganizer() {
        User organizer = EventOrganizer.getInstance().getEventOrganizer();
        Picasso.get().load(organizer.getPictureUrl())
                .into((ImageView) findViewById(R.id.picture_organizer));
        TextView organizerText = findViewById(R.id.organizer);
        organizerText.setText(String.format("%s %s", organizer.getFirstName(), organizer.getLastName()));
    }

    @Override
    public void update(Observable o, Object arg) {
        showOrganizer();
    }

    @Override
    protected void updateInfo() {
        if (!EventList.getInstance().containsEvent(getEventId()))
            return;
        super.updateInfo();

        // -- réseau social

        ImageView Instagram = findViewById(R.id.insta);
        MaterialCardView web = findViewById(R.id.pageFeed);
        web.setVisibility(View.GONE);
        Instagram.setVisibility(View.GONE);

        if (!(EventList.getInstance().getEvent(getEventId()).getInstagramURL() == null || EventList.getInstance().getEvent(getEventId()).getInstagramURL().isEmpty())) {
            WebView myWebView = findViewById(R.id.web_view);
            myWebView.getSettings().setJavaScriptEnabled(true);
            myWebView.getSettings().setDomStorageEnabled(true);
            myWebView.getSettings().setUserAgentString("Android");
            myWebView.loadUrl(EventList.getInstance().getEvent(getEventId()).getInstagramURL());
            Instagram.setVisibility(View.VISIBLE);
            Instagram.setOnClickListener(click -> {
                if (web.getVisibility() == View.VISIBLE) {
                    web.setVisibility(View.GONE);
                    Log.d("insta", "gone");
                } else {
                    web.setVisibility(View.VISIBLE);
                    Log.d("insta", "visible");
                }
            });
        }
    }
}