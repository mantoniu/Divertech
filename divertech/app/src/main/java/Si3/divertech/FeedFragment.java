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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.stream.Collectors;


public class FeedFragment extends Fragment implements ClickableFragment {
    private Context context;
    private ObserverAdapter adapter;
    private FeedType feedType;

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
            adapter = new NotificationAdapter(this, getContext(), eventId != null ? filterNotificationByEvent(eventId) : null);
            NotificationList.getInstance().addObserver(adapter);
            ListEvent.getInstance().addObserver(adapter);
        } else {
            adapter = new EventAdapter(this, getContext());
            ListEvent.getInstance().addObserver(adapter);
        }
        listView.setAdapter(adapter);
        return view;
    }

    public List<Notification> filterNotificationByEvent(String eventId) {
        NotificationList.getInstance().getNotifications().add(new Notification());
        return NotificationList.getInstance().getNotifications()
                .stream()
                .filter(event -> event.getEventId().equals(eventId))
                .collect(Collectors.toList());
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
            intent.putExtra("eventId", ListEvent.getInstance().getEvent(notification.getEventId()));
            startActivity(intent);
        });

        popupView.findViewById(R.id.close_button).setOnClickListener((click) -> popup.dismiss());

        ((TextView) popupView.findViewById(R.id.notification_type)).setText(notification.getTitle());
        ((TextView) popupView.findViewById(R.id.notification_description)).setText(notification.getDescription());

        popup.showAtLocation(requireView(), Gravity.CENTER, 0, 0);
    }

    @Override
    public void onClick(String itemId) {
        if (feedType == FeedType.EVENTS) {
            Intent intent = new Intent(context, EventActivity.class);
            intent.putExtra("event", ListEvent.getInstance().getEvent(itemId));
            startActivity(intent);
        } else {
            if (UserData.getConnectedUser().getIsAdmin()) {
                Intent intent = new Intent(getContext(), MultiPagesAdminActivity.class);
                intent.putExtra("type", NotificationTypes.CONTACT);
                startActivity(intent);
            } else {
                Log.d("CLICKED_FRAGMENT", "");
                createPopup(NotificationList.getInstance().getNotification(itemId));
            }
        }
    }
}