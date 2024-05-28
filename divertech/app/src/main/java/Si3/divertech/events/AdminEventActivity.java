package Si3.divertech.events;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import Si3.divertech.CreateEventActivity;
import Si3.divertech.MainActivity;
import Si3.divertech.MultiPagesActivity;
import Si3.divertech.R;
import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

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

        findViewById(R.id.share).setOnClickListener(click -> createPopup());

        findViewById(R.id.delete).setOnClickListener(click -> {
            EventList.getInstance().deleteEvent(getEventId());
            finish();
        });

        findViewById(R.id.card_feed_type).setOnClickListener(click -> {
            Log.d("Admin", getEventId() + " ");
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra(getString(R.string.event_id), getEventId());
            intent.putExtra(getString(R.string.back_possible), true);
            startActivity(intent);
        });
    }


    private void createPopup() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.share_popup, null);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        PopupWindow popup = new PopupWindow(popupView, width, height, true);

        popup.setAnimationStyle(R.style.Animation);

        TextView eventId = popupView.findViewById(R.id.event_id);
        eventId.setText(getEventId());
        eventId.setOnClickListener(click -> {
            ClipboardManager cm = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Event ID", getEventId());
            cm.setPrimaryClip(clip);
            Toast.makeText(getApplicationContext(), "Copié dans le presse papier", Toast.LENGTH_SHORT).show();
        });

        QRGEncoder qrgEncoder = new QRGEncoder(getEventId(), null, QRGContents.Type.TEXT, 200);
        Bitmap bitmap = qrgEncoder.getBitmap(0);
        ((ImageView) popupView.findViewById(R.id.qr_code)).setImageBitmap(bitmap);

        popupView.findViewById(R.id.layout).setOnClickListener((click) -> popup.dismiss());
        popupView.findViewById(R.id.close_button).setOnClickListener((click) -> popup.dismiss());

        popup.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    @Override
    protected void updateInfo() {
        if (!EventList.getInstance().containsEvent(getEventId()))
            return;
        super.updateInfo();
        if (!EventList.getInstance().getEvent(getEventId()).getInstagramURL().isEmpty()) {
            findViewById(R.id.card_insta).setVisibility(View.VISIBLE);
            TextView url = findViewById(R.id.instaURL);
            url.setText(EventList.getInstance().getEvent(getEventId()).getInstagramURL());
        } else findViewById(R.id.card_insta).setVisibility(View.GONE);
    }
}
