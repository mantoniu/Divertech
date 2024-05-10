package Si3.divertech;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NotificationAdapter extends ObserverAdapter {
    private List<Notification> notificationList;
    private final Context context;
    private final ClickableFragment fragment;


    public NotificationAdapter(ClickableFragment fragment, Context context, List<Notification> notificationList) {
        this.context = context;
        this.fragment = fragment;
        this.notificationList = notificationList;
    }

    public NotificationAdapter(ClickableFragment fragment, Context context) {
        this(fragment, context, null);
    }

    public List<Notification> getItemsList() {
        if (notificationList != null)
            return notificationList;
        return NotificationList.getInstance().getNotifications();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View layoutItem = convertView == null ? mInflater.inflate(R.layout.notification_item, parent, false) : convertView;

        // Layout items recuperation
        ImageView notificationImage = layoutItem.findViewById(R.id.item_image);
        TextView notificationTitle = layoutItem.findViewById(R.id.item_title);
        TextView notificationContent = layoutItem.findViewById(R.id.item_content);
        ImageView deleteButton = layoutItem.findViewById(R.id.delete_button);

        // Filling the layout with notifications values
        Notification notification = getItemsList().get(position);
        Log.d("TITLE", notification.getTitle());

        deleteButton.setOnClickListener(click -> {
            NotificationList.getInstance().deleteNotification(notification.getId());
            notifyDataSetChanged();
        });

        layoutItem.setTag(notification.getId());

        if (ListEvent.getInstance().containsEvent(notification.getEventId())) {
            Picasso.get().load(ListEvent.getInstance().getEvent(notification.getEventId()).getPictureUrl()).into(notificationImage);
            notificationTitle.setText(ListEvent.getInstance().getEvent(notification.getEventId()).getTitle());
            notificationContent.setText(notification.getTitle());
        }

        layoutItem.setOnClickListener((click) -> fragment.onClick(notification.getId()));

        return layoutItem;
    }
}
