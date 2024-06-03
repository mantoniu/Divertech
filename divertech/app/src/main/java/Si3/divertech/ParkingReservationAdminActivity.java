package Si3.divertech;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.util.Optional;

import Si3.divertech.parking.ParkingCreator;
import Si3.divertech.parking.ParkingList;
import Si3.divertech.users.User;

public class ParkingReservationAdminActivity extends RequireUserActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_reservation_admin);
        Intent intent = getIntent();
        String reservationId = intent.getStringExtra("reservationId");
        TextInputEditText address = findViewById(R.id.address);
        address.setText(ParkingList.getInstance().getReservations(reservationId).getAddress());
        TextInputEditText phone_number = findViewById(R.id.phone_number);
        phone_number.setText(ParkingList.getInstance().getReservations(reservationId).getPhone());
        TextInputEditText licence_plate = findViewById(R.id.licence_plate);
        licence_plate.setText(ParkingList.getInstance().getReservations(reservationId).getLicencePlate());
        TextView date = findViewById(R.id.date);
        date.setText(ParkingList.getInstance().getReservations(reservationId).getDate());
        ImageView button = findViewById(R.id.return_arrow);
        button.setOnClickListener(click -> finish());

        ImageView picture = findViewById(R.id.profile_picture);
        Optional<User> user = ParkingCreator.getUserInList(ParkingList.getInstance().getReservations(reservationId).getUserCreatorId());
        if (!user.isPresent()) return;
        Picasso.get().load(user.get().getPictureUrl()).into(picture);
        ((TextView) findViewById(R.id.name)).setText(user.get().getLastName() + "\n" + user.get().getFirstName());

        findViewById(R.id.bloc_contact_refuse).setOnClickListener(click -> {
            ParkingList.getInstance().refuseReservation(reservationId);
            finish();
        });
        findViewById(R.id.bloc_contact_accept).setOnClickListener(click -> {
            ParkingList.getInstance().acceptReservation(reservationId);
            finish();
        });
    }
}
