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
import java.util.stream.Collectors;


public class ListEventAdapter extends BaseAdapter {

    private List<Event> listEvent;
    private Context context;

    public ListEventAdapter(Context context, Map<String, Event> eventMap){
        this.context = context;
        this.listEvent = new ArrayList<>(eventMap.values());
    }

    @Override
    public int getCount() {
        return listEvent.size();
    }

    @Override
    public Object getItem(int position) {
        return listEvent.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = convertView == null ? mInflater.inflate(R.layout.event_layout,parent, false) : convertView;
        convertView.setTag(position);

        TextView title = convertView.findViewById(R.id.title);
        TextView description = convertView.findViewById(R.id.description);
        ImageView image = convertView.findViewById(R.id.image);

        title.setText(listEvent.get(position).getTitle());
        description.setText(listEvent.get(position).getShortDesciption());
        image.setImageResource(listEvent.get(position).getImage());

        return convertView;
    }
}
