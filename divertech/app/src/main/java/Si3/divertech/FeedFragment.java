package Si3.divertech;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class FeedFragment extends Fragment {
    private final String TAG = "antoniu " + getClass().getSimpleName();
    private List<? extends Adaptable> items = new ArrayList<>();
    private Context context;
    private FeedAdapter adapter;

    public FeedFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        ListView listView = view.findViewById(R.id.feed);

        FeedType feedType = FeedType.values()[requireArguments().getInt(getString(R.string.FEED_TYPE))];

        listView.setOnItemClickListener((parent, viewC, position, id) -> {
            if (Objects.requireNonNull(feedType) == FeedType.EVENTS) {
                Intent intent = new Intent(getContext(), EventActivity.class);
                String eventId = String.valueOf(viewC.getTag());
                intent.putExtra("event", ListEvent.getEventMap().get(eventId));
                startActivity(intent);
            }
        });

        Map<String, ? extends Adaptable> map = (feedType == FeedType.NOTIFICATION) ? NotificationList.getNotificationMap() : ListEvent.getUserEventMap();

        adapter = new FeedAdapter(getContext(), map, feedType == FeedType.NOTIFICATION);
        listView.setAdapter(adapter);
        return view;
    }

    @Nullable
    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }
}