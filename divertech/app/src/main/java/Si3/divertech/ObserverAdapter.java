package Si3.divertech;

import android.util.Log;
import android.widget.BaseAdapter;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public abstract class ObserverAdapter extends BaseAdapter implements Observer {
    public abstract List<?> getItemsList();

    @Override
    public Object getItem(int position) {
        return getItemsList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return getItemsList().size();
    }

    @Override
    public void update(Observable o, Object arg) {
        Log.d("UPDATE", "");
        notifyDataSetChanged();
    }
}
