package Si3.divertech;

import android.content.Context;
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

public class FeedAdapter extends BaseAdapter {
    private final Map<String, ? extends Adaptable> feedMap;
    private final Context context;
    private final boolean deleteButton;

    public FeedAdapter(Context context, Map<String, ? extends Adaptable> feedMap, boolean deleteButton) {
        this.context = context;
        this.feedMap = feedMap;
        this.deleteButton = deleteButton;
    }

    public FeedAdapter(Context context, Map<String, ? extends Adaptable> feedMap) {
        this(context, feedMap, false);
    }

    public List<? extends Adaptable> getItemsList() {
        return new ArrayList<>(feedMap.values());
    }

    @Override
    public int getCount() {
        return feedMap.size();
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
        View layoutItem = convertView == null ? mInflater.inflate(R.layout.notification_layout, parent, false) : convertView;

        // Layout items recuperation
        ImageView notificationImage = layoutItem.findViewById(R.id.notification_image);
        TextView notificationTitle = layoutItem.findViewById(R.id.notification_title);
        TextView notificationContent = layoutItem.findViewById(R.id.notification_content);
        ImageView deleteButton = layoutItem.findViewById(R.id.delete_button);

        if (this.deleteButton) {
            deleteButton.setOnClickListener(click -> {
                NotificationList.deleteNotification(layoutItem.getTag().toString());
                notifyDataSetChanged();
            });
        } else deleteButton.setVisibility(View.INVISIBLE);

        // Filling the layout with notifications values
        Adaptable notification = getItemsList().get(position);

        layoutItem.setTag(notification.getId());

        Picasso.get().load(notification.getPictureUrl()).into(notificationImage);
        notificationTitle.setText(notification.getTitle());
        notificationTitle.setTextSize(24);
        notificationContent.setText(notification.getShortDescription());
        notificationContent.setTextSize(14);

        return layoutItem;
    }
}
