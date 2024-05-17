package Si3.divertech.events;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.card.MaterialCardView;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import Si3.divertech.MapActivity;
import Si3.divertech.MultiPagesActivity;
import Si3.divertech.ParkingActivity;
import Si3.divertech.R;

public class EventActivity extends EventActivities {

    private static final int WRITE_CALENDAR_PERMISSION_CODE = 101;
    private static final int READ_CALENDAR_PERMISSION_CODE = 102;
    @Override
    public void setActivity() {
        setContentView(R.layout.activity_event);
        Intent modification = new Intent(getApplicationContext(), MultiPagesActivity.class);
        View contact = findViewById(R.id.contact_organizer);
        contact.setOnClickListener(click -> startActivity(modification));


        checkPermission(Manifest.permission.WRITE_CALENDAR, WRITE_CALENDAR_PERMISSION_CODE);
        checkPermission(Manifest.permission.READ_CALENDAR, READ_CALENDAR_PERMISSION_CODE);
        if (!EventList.getInstance().containsEvent(getEventId())) {
            finish();
            return;
        }

        MaterialCardView map = findViewById(R.id.card_name_event);
        map.setOnClickListener(click -> {
            Intent intent = new Intent(getApplicationContext(), MapActivity.class);
            intent.putExtra(getString(R.string.event_id), getEventId());
            startActivity(intent);
            finish();
        });

        findViewById(R.id.card_date).setOnClickListener((click) -> addEventToCalendar(getEventId()));

        MaterialCardView parkingLayout = findViewById(R.id.card_parking);
        parkingLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ParkingActivity.class);
            startActivity(intent);
        });
    }

    private void addEventToCalendar(String eventId) {
        if (eventExistsInCalendar(EventList.getInstance().getEvent(eventId).getTitle(), EventList.getInstance().getEvent(eventId).getZonedDate())) {
            Toast.makeText(getApplicationContext(), "L'évènement est déjà dans l'agenda", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != -1) {
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, EventList.getInstance().getEvent(eventId).getZonedDate().toInstant().toEpochMilli());
            values.put(CalendarContract.Events.TITLE, EventList.getInstance().getEvent(eventId).getTitle());
            values.put(CalendarContract.Events.DESCRIPTION, EventList.getInstance().getEvent(eventId).getShortDescription());
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