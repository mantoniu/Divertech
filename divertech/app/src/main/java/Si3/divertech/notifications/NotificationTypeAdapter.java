package Si3.divertech.notifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import Si3.divertech.R;
import Si3.divertech.users.UserData;

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
            if (UserData.getInstance().getConnectedUser().getLanguage().equals("en"))
                txt.setText(mData.get(position).getContentEn());
            txt.setText(mData.get(position).getContentFr());
        } else {
            convertView = mInflater.inflate(viewResourceId, parent, false);
            TextView txt = convertView.findViewById(R.id.element);
            if (UserData.getInstance().getConnectedUser().getLanguage().equals("en"))
                txt.setText(mData.get(position).getContentEn());
            txt.setText(mData.get(position).getContentFr());
        }

        return convertView;
    }
}
