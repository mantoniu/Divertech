package Si3.divertech;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class EventActivity extends AppCompatActivity {

    private static final int WRITE_CALENDAR_PERMISSION_CODE = 101;
    private static final int READ_CALENDAR_PERMISSION_CODE = 102;

    boolean isTextViewClicked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String eventId = getIntent().getStringExtra(getString(R.string.event_id));

        if (UserData.getInstance().getConnectedUser().getIsAdmin()) {
            setContentView(R.layout.activity_admin_event);
            Intent modification = new Intent(getApplicationContext(), CreateEventActivity.class);
            modification.putExtra(getString(R.string.event_id), eventId);
            View change = findViewById(R.id.edit);
            change.setOnClickListener(click -> startActivity(modification));
            Intent report = new Intent(getApplicationContext(), MultiPagesActivity.class);
            View reportButton = findViewById(R.id.button_report);
            reportButton.setOnClickListener(click -> startActivity(report));

            View feed = findViewById(R.id.card_feed_type);
            feed.setOnClickListener(click -> {
                Log.d("Admin", eventId + " ");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(getString(R.string.event_id), eventId);
                startActivity(intent);
            });

            updateInfo(eventId);
        } else {
            setContentView(R.layout.activity_event);
            Intent modification = new Intent(getApplicationContext(), MultiPagesActivity.class);
            View contact = findViewById(R.id.contact_organizer);
            contact.setOnClickListener(click -> startActivity(modification));


            checkPermission(Manifest.permission.WRITE_CALENDAR, WRITE_CALENDAR_PERMISSION_CODE);
            checkPermission(Manifest.permission.READ_CALENDAR, READ_CALENDAR_PERMISSION_CODE);

            if (ListEvent.getInstance().getEvent(eventId) == null) {
                finish();
                return;
            }


            MaterialCardView map = findViewById(R.id.card_name_event);
            map.setOnClickListener(click -> {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                intent.putExtra("pos", eventId);
                startActivity(intent);
                finish();
            });

            Intent curIntent = getIntent();
            String qrData = curIntent.getStringExtra("qr_data");
            if (qrData != null) {
                Toast.makeText(getApplicationContext(), qrData, Toast.LENGTH_LONG).show();
            }


            findViewById(R.id.card_date).setOnClickListener((click) -> addEventToCalendar(ListEvent.getInstance().getEvent(eventId)));

            MaterialCardView parkingLayout = findViewById(R.id.card_parking);
            parkingLayout.setOnClickListener(v -> {
                Intent intent = new Intent(getApplicationContext(), ParkingActivity.class);
                startActivity(intent);
            });
            updateInfo(eventId);
        }
    }

    private void updateInfo(String eventId) {
        if (ListEvent.getInstance().getEvent(eventId) == null)
            return;

        TextView title = findViewById(R.id.name_event);
        title.setText(ListEvent.getInstance().getEvent(eventId).getTitle());

        TextView place = findViewById(R.id.localisation);
        place.setText(ListEvent.getInstance().getEvent(eventId).getPosition());

        TextView description = findViewById(R.id.description);
        description.setMaxLines(3);
        description.setText(ListEvent.getInstance().getEvent(eventId).getDescription());
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

        View b = findViewById(R.id.return_arrow);
        b.setOnClickListener(click -> finish());

        Picasso.get().load(ListEvent.getInstance().getEvent(eventId).getPictureUrl()).into((ImageView) findViewById(R.id.image_event));

        ((TextView) findViewById(R.id.date)).setText(ListEvent.getInstance().getEvent(eventId).getFormattedDate());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void addEventToCalendar(Event event) {
        if (eventExistsInCalendar(event.getTitle(), event.getZonedDate())) {
            Toast.makeText(getApplicationContext(), "L'évènement est déjà dans l'agenda", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != -1) {
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, event.getZonedDate().toInstant().toEpochMilli());
            values.put(CalendarContract.Events.TITLE, event.getTitle());
            values.put(CalendarContract.Events.DESCRIPTION, event.getShortDescription());
            values.put(CalendarContract.Events.CALENDAR_ID, getDefaultCalendarId());
            values.put(CalendarContract.Events.EVENT_TIMEZONE, ZoneId.systemDefault().getId());
            values.put(CalendarContract.Events.DURATION, "PT2H");

            Uri uri = getContentResolver().insert(CalendarContract.Events.CONTENT_URI, values);
            if (uri != null) {
                Toast.makeText(getApplicationContext(), "L'évènement a été ajouté à l'agenda", Toast.LENGTH_LONG).show();
            }
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


    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) == -1) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        } else {
            Log.d("PERMISSION", "Permission already granted");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_CALENDAR_PERMISSION_CODE || requestCode == WRITE_CALENDAR_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == 0) {
                Log.d("PERMISSION", "Calendar Permission Granted");
            } else Log.d("PERMISSION", "Camera Permission Denied");
        }
    }
}