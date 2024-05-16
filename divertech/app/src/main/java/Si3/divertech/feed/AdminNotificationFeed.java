package Si3.divertech.feed;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;

import java.util.Observable;
import java.util.Observer;

import Si3.divertech.MultiPagesAdminActivity;
import Si3.divertech.R;
import Si3.divertech.notifications.NotificationAdapter;
import Si3.divertech.notifications.NotificationCreator;
import Si3.divertech.notifications.NotificationList;

public class AdminNotificationFeed extends NotificationFeed {
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

        getBinding().emptyText.setText(R.string.no_notification);
        getBinding().feed.setEmptyView(getBinding().emptyText);

        NotificationList.getInstance().addObserver(this);
        return view;
    }

    @Override
    public BaseAdapter getAdapter() {
        return new NotificationAdapter(this, getContext(), requireArguments().getString(getString(R.string.event_id)));
    }

    @Override
    public void onClick(String itemId) {
        intent = new Intent(getContext(), MultiPagesAdminActivity.class);
        intent.putExtra("type", NotificationList.getInstance().getNotification(itemId).getType());
        intent.putExtra(getString(R.string.notification_id), itemId);
        if (notificationCreatorObserver == null)
            notificationCreatorObserver = new NotificationCreatorObserver();

        NotificationCreator.getInstance().addObserver(notificationCreatorObserver);
        NotificationCreator.getInstance().getUser(NotificationList.getInstance().getNotification(itemId).getNotificationCreatorUser());
    }
}
