package Si3.divertech;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import Si3.divertech.parking.ParkingList;

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
        Log.d("test", ParkingList.getInstance().getReservations(id).getStatus().toString());
        switch (ParkingList.getInstance().getReservations(id).getStatus()) {
            case ACCEPTED:
                title.setText("Accepté");
                break;
            case WAITING:
                title.setText("En attente");
                break;
            case REFUSE:
                title.setText("Refusé");
                break;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        showResult();
    }
}
