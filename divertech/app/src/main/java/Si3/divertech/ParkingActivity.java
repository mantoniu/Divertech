package Si3.divertech;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.card.MaterialCardView;

public class ParkingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking);

        ImageView returnToEvent = findViewById(R.id.return_arrow);
        returnToEvent.setOnClickListener(v -> finish());

        Button parkingButton = findViewById(R.id.send_Button);
        parkingButton.setOnClickListener(v -> finish());

        View headerView = findViewById(R.id.header_menu);
        ((TextView) headerView.findViewById(R.id.feed_title)).setText(R.string.parking_space);

        MaterialCardView datePicker = findViewById(R.id.card_date);
        datePicker.setOnClickListener(click -> {
            CreateEventActivity.DatePickerFragment newFragment = new CreateEventActivity.DatePickerFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);
            newFragment.show(ft, "datePicker");
        });
    }
}