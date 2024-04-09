package Si3.divertech;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ListEventAdapter extends BaseAdapter {

    private ListEvent listEvent;
    private Context context;

    public ListEventAdapter(Context context, ListEvent listEvent){
        this.context = context;
        this.listEvent = listEvent;
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
        convertView.setTag(position+1);

        TextView title = convertView.findViewById(R.id.title);
        TextView description = convertView.findViewById(R.id.description);
        ImageView image = convertView.findViewById(R.id.image);

        title.setText(listEvent.get(position+1).getTitle());
        description.setText(listEvent.get(position+1).getShortDesciption());
        image.setImageResource(listEvent.get(position+1).getImage());

        return convertView;
    }
}
