package Si3.divertech;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NotificationAdapter extends BaseAdapter {
    private final Map<String, Notification> notificationMap;
    private final Context context;
    private final ClickableFragment fragment;

    public NotificationAdapter(ClickableFragment fragment, Context context, Map<String, Notification> notificationMap) {
        this.context = context;
        this.notificationMap = notificationMap;
        this.fragment = fragment;
    }

    public List<Notification> getItemsList() {
        return new ArrayList<>(notificationMap.values());
    }

    @Override
    public int getCount() {
        return notificationMap.size();
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
        Log.d("TITLE", notification.getTitle());

        deleteButton.setOnClickListener(click -> {
            NotificationList.deleteNotification(notification.getId());
            notifyDataSetChanged();
        });

        layoutItem.setTag(notification.getId());

        Picasso.get().load(ListEvent.getEvent(notification.getEventId()).getPictureUrl()).into(notificationImage);
        notificationTitle.setText(ListEvent.getEvent(notification.getEventId()).getTitle());
        notificationContent.setText(notification.getTitle());

        layoutItem.setOnClickListener((click) -> fragment.onClick(notification.getId()));

        return layoutItem;
    }
}
