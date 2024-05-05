package Si3.divertech;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class LangAdapter extends BaseAdapter {


    private List<String> mData;
    private LayoutInflater mInflater;

    public LangAdapter(Context context) {
        this.mData = Arrays.asList("fr", "en");
        this.mInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mData.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View holder= mInflater.inflate(R.layout.lang_layout, parent, false);

        TextView txt=holder.findViewById(R.id.lang_txt);
        if(position==0){
            txt.setText("Français");
        }else{
            txt.setText("English");
        }

        ImageView img=holder.findViewById(R.id.lang_img);
        if(position==0) {
            img.setImageResource(R.drawable.fr_flag);
        }else{
            img.setImageResource(R.drawable.en_flag);
        }


        return holder;
    }

}
