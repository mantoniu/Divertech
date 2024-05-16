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
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Observable;
import java.util.Observer;

import Si3.divertech.databinding.FragmentFeedBinding;

public class FeedFragment extends Fragment implements ClickableFragment {
    private Context context;
    private BaseAdapter adapter;
    private FeedType feedType;
    private Intent intent;
    private NotificationCreatorObserver notificationCreatorObserver;
    private FragmentFeedBinding binding;

    private class NotifyAdapterObserver implements Observer {
        @Override
        public void update(Observable o, Object arg) {
            if (adapter != null)
                adapter.notifyDataSetChanged();
        }
    }

    private class NotificationListObserver extends NotifyAdapterObserver {
        @Override
        public void update(Observable o, Object arg) {
            super.update(o, arg);
            binding.emptyText.setText(R.string.no_notification);
            binding.feed.setEmptyView(binding.emptyText);
        }
    }

    private class ListEventObserver extends NotifyAdapterObserver {
        @Override
        public void update(Observable o, Object arg) {
            super.update(o, arg);
            if (feedType == FeedType.EVENTS) {
                binding.emptyText.setText(R.string.no_event_registered);
                binding.feed.setEmptyView(binding.emptyText);
            }
        }
    }

    private class NotificationCreatorObserver implements Observer {
        @Override
        public void update(Observable o, Object arg) {
            if (intent != null)
                startActivity(intent);
            NotificationCreator.getInstance().deleteObserver(notificationCreatorObserver);
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            binding.emptyText.setText(savedInstanceState.getString("test"));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFeedBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        feedType = FeedType.values()[requireArguments().getInt(getString(R.string.FEED_TYPE))];
        String eventId = requireArguments().getString(getString(R.string.event_id));


        if (feedType == FeedType.NOTIFICATION) {
            if (eventId != null) {
                binding.emptyText.setText(R.string.no_notification);
                binding.feed.setEmptyView(binding.emptyText);
            }
            adapter = new NotificationAdapter(this, getContext(), eventId);
            NotificationList.getInstance().addObserver(new NotificationListObserver());
            ListEvent.getInstance().addObserver(new ListEventObserver());
        } else {
            adapter = new EventAdapter(this, getContext());
            ListEvent.getInstance().addObserver(new ListEventObserver());
            ListEvent.getInstance().requestData();
        }
        binding.feed.setAdapter(adapter);
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
    }


    private void createPopup(Notification notification) {
        LayoutInflater inflater = (LayoutInflater) requireActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.notification_popup, requireActivity().findViewById(R.id.notification_popup));

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        PopupWindow popup = new PopupWindow(popupView, width, height, true);

        popup.setAnimationStyle(R.style.Animation);

        popupView.findViewById(R.id.go_to_event).setOnClickListener((click) -> {
            popup.dismiss();
            Intent intent = new Intent(context, EventActivity.class);
            intent.putExtra(getString(R.string.event_id), notification.getEventId());
            startActivity(intent);
        });
        popupView.findViewById(R.id.layout).setOnClickListener((click) -> popup.dismiss());

        popupView.findViewById(R.id.close_button).setOnClickListener((click) -> popup.dismiss());

        ((TextView) popupView.findViewById(R.id.notification_type)).setText(String.format("%s", notification.getType()));
        ((TextView) popupView.findViewById(R.id.notification_description)).setText(notification.getDescription());

        popup.showAtLocation(requireView(), Gravity.CENTER, 0, 0);
    }

    @Override
    public void onClick(String itemId) {
        if (feedType == FeedType.EVENTS) {
            intent = new Intent(context, EventActivity.class);
            Log.d("test", getString(R.string.event_id) + itemId);
            intent.putExtra(getString(R.string.event_id), itemId);
            startActivity(intent);
        } else if (UserData.getInstance().getConnectedUser().getIsAdmin()) {
            intent = new Intent(getContext(), MultiPagesAdminActivity.class);
            intent.putExtra("type", NotificationList.getInstance().getNotification(itemId).getType());
            intent.putExtra(getString(R.string.notification_id), itemId);
            if (feedType == FeedType.NOTIFICATION) {
                if (notificationCreatorObserver == null)
                    notificationCreatorObserver = new NotificationCreatorObserver();
                NotificationCreator.getInstance().addObserver(notificationCreatorObserver);
            }
            NotificationCreator.getInstance().getUser(NotificationList.getInstance().getNotification(itemId).getNotificationCreatorUser());
        } else {
            Log.d("CLICKED_FRAGMENT", "");
            createPopup(NotificationList.getInstance().getNotification(itemId));
        }
    }
}