package Si3.divertech;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ListEventAdapter extends BaseAdapter {

    private final Map<String, Event> eventMap;
    private final Context context;

    public ListEventAdapter(Context context, Map<String, Event> eventMap){
        this.context = context;
        this.eventMap = eventMap;
    }

    @Override
    public int getCount() {
        return eventMap.size();
    }

    @Override
    public Object getItem(int position) {
        return getEventList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private List<Event> getEventList() {
        return new ArrayList<>(eventMap.values());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = convertView == null ? mInflater.inflate(R.layout.event_layout,parent, false) : convertView;

        TextView title = convertView.findViewById(R.id.title);
        TextView description = convertView.findViewById(R.id.description);
        ImageView image = convertView.findViewById(R.id.image);

        List<Event> eventList = getEventList();
        convertView.setTag(eventList.get(position).getId());

        title.setText(eventList.get(position).getTitle());
        description.setText(eventList.get(position).getShortDesciption());
        image.setImageResource(eventList.get(position).getImage());

        return convertView;
    }
}
