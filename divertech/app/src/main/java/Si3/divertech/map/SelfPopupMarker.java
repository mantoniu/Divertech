package Si3.divertech.map;

import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;

import Si3.divertech.R;

public class SelfPopupMarker extends PopupMarker{
    public SelfPopupMarker(LayoutInflater layoutInflater){
        super(layoutInflater, R.layout.marker_popup_layout_self);
    }

    @Override
    public void addInfoMarker(Marker marker) {
        TextView title = customPopUp.findViewById(R.id.description);
        title.setText(R.string.selfPosition);
    }
}
