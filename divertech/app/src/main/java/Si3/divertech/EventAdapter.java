package Si3.divertech;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class EventAdapter extends ObserverAdapter {
    private final Context context;
    private final ClickableFragment fragment;

    public EventAdapter(ClickableFragment fragment, Context context) {
        this.context = context;
        this.fragment = fragment;
    }

    public List<Event> getItemsList() {
        return ListEvent.getInstance().getEvents();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View layoutItem = convertView == null ? mInflater.inflate(R.layout.event_item, parent, false) : convertView;

        // Layout items recuperation
        ImageView eventImage = layoutItem.findViewById(R.id.item_image);
        TextView eventTitle = layoutItem.findViewById(R.id.item_title);
        TextView eventDescription = layoutItem.findViewById(R.id.item_content);

        // Filling the layout with notifications values
        Event event = getItemsList().get(position);

        layoutItem.setTag(event.getId());

        Picasso.get().load(event.getPictureUrl()).into(eventImage);
        eventTitle.setText(event.getTitle());
        eventDescription.setText(event.getShortDescription());

        layoutItem.setOnClickListener((click) -> fragment.onClick(event.getId()));

        return layoutItem;
    }
}
