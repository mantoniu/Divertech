package Si3.divertech.parking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.stream.Collectors;

import Si3.divertech.ClickableFragment;
import Si3.divertech.R;
import Si3.divertech.users.User;

public class ParkingAdapter extends BaseAdapter implements Observer {
    private final String eventId;

    private String userCreatorId;

    private final Context context;
    private final ClickableFragment fragment;

    private View layoutItem = null;

    public ParkingAdapter(ClickableFragment fragment, Context context, String eventId) {
        this.context = context;
        this.fragment = fragment;
        this.eventId = eventId;
    }

    public List<Reservations> getReservationsList() {
        if (eventId != null)
            return filterByEventId();
        return ParkingList.getInstance().getReservations();
    }

    public List<Reservations> filterByEventId() {
        return ParkingList.getInstance().getReservations()
                .stream()
                .filter(event -> event.getEventId().equals(eventId))
                .collect(Collectors.toList());
    }

    @Override
    public int getCount() {
        return getReservationsList().size();
    }

    @Override
    public Object getItem(int position) {
        return getReservationsList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View layoutItem = convertView == null ? mInflater.inflate(R.layout.reservation_item, parent, false) : convertView;
        ParkingCreator.getInstance().addObserver(this);
        this.userCreatorId = getReservationsList().get(position).getUserCreatorId();
        ParkingCreator.getInstance().getUser(this.userCreatorId);
        this.layoutItem = layoutItem;

        layoutItem.setOnClickListener((click) -> fragment.onClick(getReservationsList().get(position).getId()));
        return layoutItem;
    }

    public void updateData() {
        ImageView picture = layoutItem.findViewById(R.id.profile_image);
        Optional<User> user = ParkingCreator.getUserInList(this.userCreatorId);
        if (!user.isPresent()) return;
        Picasso.get().load(user.get().getPictureUrl()).into(picture);
        ((TextView) layoutItem.findViewById(R.id.last_name)).setText(user.get().getLastName());
        ((TextView) layoutItem.findViewById(R.id.name)).setText(user.get().getFirstName());
    }

    @Override
    public void update(Observable o, Object arg) {
        updateData();
    }
}
