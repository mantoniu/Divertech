package Si3.divertech;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.stream.Collectors;

public class NotificationAdapter extends BaseAdapter {
    private final String eventId;
    private final Context context;
    private final ClickableFragment fragment;

    public NotificationAdapter(ClickableFragment fragment, Context context, String eventId) {
        this.context = context;
        this.fragment = fragment;
        this.eventId = eventId;
    }

    public List<Notification> getItemsList() {
        if (eventId != null)
            return filterByEventId();
        return NotificationList.getInstance().getNotifications();
    }

    public List<Notification> filterByEventId() {
        return NotificationList.getInstance().getNotifications()
                .stream()
                .filter(event -> event.getEventId().equals(eventId))
                .collect(Collectors.toList());
    }

    @Override
    public int getCount() {
        return getItemsList().size();
    }

    @Override
    public Object getItem(int position) {
        return getItemsList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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

        deleteButton.setOnClickListener(click -> {
            NotificationList.getInstance().deleteNotification(notification.getId());
            notifyDataSetChanged();
        });

        layoutItem.setTag(notification.getId());

        if(ListEvent.getInstance().containsEvent(notification.getEventId())){
            Picasso.get().load(ListEvent.getInstance().getEvent(notification.getEventId()).getPictureUrl()).into(notificationImage);
            notificationTitle.setText(ListEvent.getInstance().getEvent(notification.getEventId()).getTitle());
            notificationContent.setText(notification.getType());
        }

        layoutItem.setOnClickListener((click) -> fragment.onClick(notification.getId()));

        return layoutItem;
    }
}
