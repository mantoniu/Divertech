package Si3.divertech.map;

import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import Si3.divertech.R;
import Si3.divertech.events.EventList;

public class EventPopupMarker extends PopupMarker{

    public EventPopupMarker(LayoutInflater layoutInflater){
        super(layoutInflater,R.layout.marker_popup_layout);
    }

    @Override
    public void addInfoMarker(Marker marker) {
        if (marker.getTag() != null && EventList.getInstance().containsEvent(marker.getTag().toString())) {
            TextView title = customPopUp.findViewById(R.id.title);
            title.setText(EventList.getInstance().getEvent(marker.getTag().toString()).getTitle());
            ImageView picture = customPopUp.findViewById(R.id.image);
            Picasso.get().load(EventList.getInstance().getEvent(marker.getTag().toString()).getPictureUrl()).into(picture);
            TextView description = customPopUp.findViewById(R.id.description);
            description.setText(EventList.getInstance().getEvent(marker.getTag().toString()).getShortDescription());
        }
    }
}
