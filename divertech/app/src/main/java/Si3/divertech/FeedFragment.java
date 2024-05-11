package Si3.divertech;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Observable;
import java.util.Observer;

public class FeedFragment extends Fragment implements ClickableFragment, Observer {
    private Context context;
    private BaseAdapter adapter;
    private FeedType feedType;
    private Intent intent;

    private class NotificationCreatorObserver implements Observer {
        @Override
        public void update(Observable o, Object arg) {
            if (intent != null)
                startActivity(intent);
        }
    }

    public FeedFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        ListView listView = view.findViewById(R.id.feed);

        feedType = FeedType.values()[requireArguments().getInt(getString(R.string.FEED_TYPE))];
        String eventId = requireArguments().getString("eventId");

        if (feedType == FeedType.NOTIFICATION) {
            adapter = new NotificationAdapter(this, getContext(), eventId);
            NotificationList.getInstance().addObserver(this);
            ListEvent.getInstance().addObserver(this);
        } else {
            adapter = new EventAdapter(this, getContext());
            ListEvent.getInstance().addObserver(this);
        }
        listView.setAdapter(adapter);
        return view;
    }

    @Nullable
    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }


    private void createPopup(Notification notification) {
        LayoutInflater inflater = (LayoutInflater) requireActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.notification_popup, requireActivity().findViewById(R.id.notification_popup));

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        PopupWindow popup = new PopupWindow(popupView, width, height, true);

        popup.setAnimationStyle(R.style.Animation);

        popupView.findViewById(R.id.go_to_event).setOnClickListener((click) -> {
            popup.dismiss();
            Intent intent = new Intent(context, EventActivity.class);
            intent.putExtra(getString(R.string.eventid), notification.getEventId());
            startActivity(intent);
        });

        popupView.findViewById(R.id.close_button).setOnClickListener((click) -> popup.dismiss());

        ((TextView) popupView.findViewById(R.id.notification_type)).setText(notification.getType());
        ((TextView) popupView.findViewById(R.id.notification_description)).setText(notification.getDescription());

        popup.showAtLocation(requireView(), Gravity.CENTER, 0, 0);
    }

    @Override
    public void onClick(String itemId) {
        if (feedType == FeedType.EVENTS) {
            intent = new Intent(context, EventActivity.class);
            intent.putExtra(getString(R.string.eventid), itemId);
            startActivity(intent);
        } else if (UserData.getInstance().getConnectedUser().getIsAdmin()) {
            intent = new Intent(getContext(), MultiPagesAdminActivity.class);
            intent.putExtra("type", NotificationList.getInstance().getNotification(itemId).getType());
            intent.putExtra(getString(R.string.eventid), itemId);
            NotificationCreator.getInstance().addObserver(new NotificationCreatorObserver());
            NotificationCreator.getInstance().getUser(NotificationList.getInstance().getNotification(itemId).getNotificationCreatorUser());
        } else {
            Log.d("CLICKED_FRAGMENT", "");
            createPopup(NotificationList.getInstance().getNotification(itemId));
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        adapter.notifyDataSetChanged();
    }
}