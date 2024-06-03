package Si3.divertech.feed;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;

import Si3.divertech.ParkingReservationAdminActivity;
import Si3.divertech.R;
import Si3.divertech.parking.ParkingAdapter;
import Si3.divertech.parking.ParkingList;

public class ParkingFeed extends Feed {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ParkingList.getInstance().addObserver(this);
        setEmptyText();
        return view;
    }

    @Override
    public BaseAdapter getAdapter() {
        return new ParkingAdapter(this, getContext(), requireArguments().getString(getString(R.string.event_id)));
    }

    @Override
    public void onClick(String itemId) {
        Log.d("test", itemId);
        Intent intent = new Intent(getContext(), ParkingReservationAdminActivity.class);
        intent.putExtra("reservationId", itemId);
        startActivity(intent);
    }

    @Override
    public int getEmptyTextRes() {
        return R.string.no_parking_reservations;
    }
}
