package Si3.divertech.feed;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;

import Si3.divertech.R;
import Si3.divertech.events.EventActivity;
import Si3.divertech.notifications.Notification;
import Si3.divertech.notifications.NotificationAdapter;
import Si3.divertech.notifications.NotificationList;
import Si3.divertech.users.UserData;

public class NotificationFeed extends Feed {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (NotificationList.getInstance().isInitialized())
            setEmptyText();
        NotificationList.getInstance().addObserver(this);
        return view;
    }

    @Override
    public BaseAdapter getAdapter() {
        return new NotificationAdapter(this, getContext(), null);
    }

    @Override
    public void onClick(String itemId) {
        createPopup(NotificationList.getInstance().getNotification(itemId));
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

        ((TextView) popupView.findViewById(R.id.notification_type)).setText(String.format("%s", UserData.getInstance().getConnectedUser().getLanguage().equals("fr") ? notification.getType().getTitleFr() : notification.getType().getTitleEn()));
        ((TextView) popupView.findViewById(R.id.notification_description)).setText(UserData.getInstance().getConnectedUser().getLanguage().equals("fr") ? notification.getDescription() : notification.getDescriptionEn());

        popup.showAtLocation(requireView(), Gravity.CENTER, 0, 0);
    }

    @Override
    public int getEmptyTextRes() {
        return R.string.no_notification;
    }
}
