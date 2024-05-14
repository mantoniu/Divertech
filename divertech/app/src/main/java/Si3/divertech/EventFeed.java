package Si3.divertech;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.Observable;

public class EventFeed extends Feed {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        setAdapter(new EventAdapter(this, getContext()));
        ListEvent.getInstance().requestData();

        return view;
    }

    @Override
    public void onClick(String itemId) {
        Intent intent = new Intent(getContext(), EventActivity.class);
        intent.putExtra(getString(R.string.event_id), itemId);
        startActivity(intent);
    }

    @Override
    public void update(Observable o, Object arg) {
        super.update(R.string.no_event_registered);
    }
}