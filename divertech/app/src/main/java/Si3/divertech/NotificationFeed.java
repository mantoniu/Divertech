package Si3.divertech;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.Observable;
import java.util.Observer;

public class NotificationFeed extends Feed {
    private NotificationCreatorObserver notificationCreatorObserver;
    private Intent intent;

    private class NotificationCreatorObserver implements Observer {
        @Override
        public void update(Observable o, Object arg) {
            if (intent != null)
                startActivity(intent);
            NotificationCreator.getInstance().deleteObserver(notificationCreatorObserver);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        String eventId = requireArguments().getString(getString(R.string.event_id));

        if (eventId != null) {
            getBinding().emptyText.setText(R.string.no_notification);
            getBinding().feed.setEmptyView(getBinding().emptyText);
        }

        setAdapter(new NotificationAdapter(this, getContext(), eventId));
        NotificationList.getInstance().addObserver(this);
        return view;
    }

    @Override
    public void onClick(String itemId) {
        if (UserData.getInstance().getConnectedUser().getIsAdmin()) {
            intent = new Intent(getContext(), MultiPagesAdminActivity.class);
            intent.putExtra("type", NotificationList.getInstance().getNotification(itemId).getType());
            intent.putExtra(getString(R.string.notification_id), itemId);
            if (notificationCreatorObserver == null)
                notificationCreatorObserver = new NotificationCreatorObserver();

            NotificationCreator.getInstance().addObserver(notificationCreatorObserver);
            NotificationCreator.getInstance().getUser(NotificationList.getInstance().getNotification(itemId).getNotificationCreatorUser());
        } else {
            Log.d("CLICKED_FRAGMENT", "");
            createPopup(NotificationList.getInstance().getNotification(itemId));
        }
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
            Intent intent = new Intent(getContext(), EventActivity.class);
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
    public void update(Observable o, Object arg) {
        super.update(R.string.no_notification);
    }
}
