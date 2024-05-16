package Si3.divertech.map;

import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import Si3.divertech.ListEvent;
import Si3.divertech.R;

public class EventPopupMarker extends PopupMarker{

    public EventPopupMarker(LayoutInflater layoutInflater){
        super(layoutInflater,R.layout.marker_popup_layout);
    }

    @Override
    public void addInfoMarker(Marker marker) {
        if (ListEvent.getInstance().containsEvent(Objects.requireNonNull(marker.getTag()).toString())) {
            TextView title = customPopUp.findViewById(R.id.title);
            title.setText(ListEvent.getInstance().getEvent(marker.getTag().toString()).getTitle());
            ImageView picture = customPopUp.findViewById(R.id.image);
            Picasso.get().load(ListEvent.getInstance().getEvent(marker.getTag().toString()).getPictureUrl()).into(picture);
            TextView description = customPopUp.findViewById(R.id.description);
            description.setText(ListEvent.getInstance().getEvent(marker.getTag().toString()).getShortDescription());
        }
    }
}
