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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class EventActivity extends AppCompatActivity {

    public static final int REPORTING = 0;
    public static final int CONTACT = 1;
    public static final int OBJET = 2;
    private static final int WRITE_CALENDAR_PERMISSION_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Event event = getIntent().getParcelableExtra("event");

        checkPermission(Manifest.permission.WRITE_CALENDAR, WRITE_CALENDAR_PERMISSION_CODE);

        if(event==null) {
            finish();
            return;
        }

        Log.d("test", event.getTitle());

        TextView titre = findViewById(R.id.nameEvent);
        titre.setText(event.getTitle());

        TextView place = findViewById(R.id.localisation);
        place.setText(String.format("Lieu : %s", event.getPlace()));

        TextView description = findViewById(R.id.description_event);
        description.setText(event.getDescription());
        Log.d("Event description",event.getDescription());
        Log.d("Event date", event.getDate());

        Intent curIntent = getIntent();
        String qrData = curIntent.getStringExtra("qr_data");
        if (qrData != null) {
            Toast.makeText(getApplicationContext(), qrData, Toast.LENGTH_LONG).show();
        }

        Intent modification = new Intent(getApplicationContext(), MultiPagesActivity.class);
        View report = findViewById(R.id.bloc_reporting);
        report.setOnClickListener(click-> {
            modification.putExtra("type", REPORTING);
            startActivity(modification);
        });
        View contact = findViewById(R.id.bloc_contact);
        contact.setOnClickListener(click-> {
            modification.putExtra("type", CONTACT);
            startActivity(modification);
        });
        View objets = findViewById(R.id.bloc_lost_object);
        objets.setOnClickListener(click-> {
            modification.putExtra("type", OBJET);
            startActivity(modification);
        });

        View b = findViewById(R.id.return_arrow);
        b.setOnClickListener(click-> finish());

        findViewById(R.id.calendar_add).setOnClickListener((click) -> addEventToCalendar(event));

        ConstraintLayout parkingLayout = findViewById(R.id.parking);
        parkingLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ParkingActivity.class);
            startActivity(intent);
        });
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
            Log.d("TEST", "Permission already granted");
        }
    }
}