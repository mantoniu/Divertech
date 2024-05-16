package Si3.divertech.feed;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;

import Si3.divertech.R;
import Si3.divertech.events.EventActivitiesFactory;
import Si3.divertech.events.EventAdapter;
import Si3.divertech.events.EventList;
import Si3.divertech.users.UserData;

public class EventFeed extends Feed {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (EventList.getInstance().isInitialized())
            setEmptyText();
        return view;
    }

    @Override
    public void onClick(String itemId) {
        Intent intent = new Intent(getContext(), EventActivitiesFactory.getEventActivityClass(UserData.getInstance().getConnectedUser().getUserType()));
        intent.putExtra(getString(R.string.event_id), itemId);
        startActivity(intent);
    }

    @Override
    public BaseAdapter getAdapter() {
        return new EventAdapter(this, getContext());
    }

    @Override
    public int getEmptyTextRes() {
        return R.string.no_event_registered;
    }
}