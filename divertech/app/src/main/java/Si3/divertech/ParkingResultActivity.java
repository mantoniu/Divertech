package Si3.divertech;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import Si3.divertech.parking.ParkingList;
import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class ParkingResultActivity extends RequireUserActivity implements Observer {

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_result);
        id = getIntent().getStringExtra("reservationId");
        ParkingList.getInstance().addObserver(this);
        ImageView button = findViewById(R.id.return_arrow);
        button.setOnClickListener(click -> finish());
        showResult();
    }

    private void showResult() {
        TextView title = findViewById(R.id.title);
        TextView id_text = findViewById(R.id.id);
        Log.d("test", ParkingList.getInstance().getReservations(id).getStatus().toString());
        switch (ParkingList.getInstance().getReservations(id).getStatus()) {
            case ACCEPTED:
                title.setText(R.string.accepted_state);
                QRGEncoder qrgEncoder = new QRGEncoder(id, null, QRGContents.Type.TEXT, 200);
                Bitmap bitmap = qrgEncoder.getBitmap(0);
                ((ImageView) findViewById(R.id.qr_code)).setImageBitmap(bitmap);
                break;
            case WAITING:
                title.setText(R.string.waiting_state);
                id_text.setText(R.string.waiting_text);
                break;
            case REFUSE:
                title.setText(R.string.refuse_state);
                id_text.setText(R.string.refuse_text);

                break;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        showResult();
    }
}
