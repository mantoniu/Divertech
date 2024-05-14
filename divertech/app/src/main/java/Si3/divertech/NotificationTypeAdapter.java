package Si3.divertech;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class NotificationTypeAdapter extends ArrayAdapter<NotificationTypes> {

    private final ArrayList<NotificationTypes> mData;
    private final LayoutInflater mInflater;
    private final int viewResourceId;

    public NotificationTypeAdapter(Context context, int viewResourceId, ArrayList<NotificationTypes> elements) {
        super(context, viewResourceId, elements);
        this.mData = elements;
        this.mInflater = LayoutInflater.from(context);
        this.viewResourceId = viewResourceId;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public NotificationTypes getItem(int position) {
        return mData.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView != null) {
            TextView txt = convertView.findViewById(R.id.element);
            txt.setText(mData.get(position).getContent());
        } else {
            convertView = mInflater.inflate(viewResourceId, parent, false);
            TextView txt = convertView.findViewById(R.id.element);
            txt.setText(mData.get(position).getContent());
        }

        return convertView;
    }
}
